from langchain_mcp_adapters.client import MultiServerMCPClient
from langchain.agents import create_agent
from llm.groq_client import llm
import json

async def create_fraud_agent():
    client = MultiServerMCPClient(
        {
            "fraudguard":{
                "transport":"streamable_http",
                "url":"http://localhost:8000/mcp"
            }
        }
    )

    tools =  await client.get_tools()
    print("\nAvailable Tools:")
    for tool in tools:
        print(tool.name)

    tools = [
       tool
       for tool in tools
       if tool.name != "report_tool"
    ]
    agent = create_agent(
        model=llm,
        tools=tools
    )

    return agent


async def investigation_alert(
        alert_id: int
):
    agent  = await create_fraud_agent()
    response = await agent.ainvoke(
        {
            "messages":[
                {
                "role":"user",
                "content":f"""
Investigation fraud alert {alert_id}.

Available tools:
- alert_tool
- transaction_tool
- history_tool
- account_risk_tool

Use whichever tools are required.

Instructions:
1. Fetch alert information.
2. Fetch transaction information.
3. Fetch account history if needed.
4. Fetch account risk if needed.
5. Analyze all gathered information.

Return ONLY valid JSON.

Example:

{{
  "alertId": {alert_id},
  "transactionId": 1,
  "riskScore": 95,
  "riskLevel": "HIGH",
  "summary": "...",
  "recommendation": "..."
}}


IMPORTANT:
Use Alert ID {alert_id}.
Do not invent any IDs.

"""
                }
            ]
        }
    )

    print("\nAgent response:")
    print(response)

    try:
        final_message = response["messages"][-1].content
        print("\nRAW CONTENT:")
        print(repr(final_message))


        report = json.loads(final_message)

        report["alertId"] = alert_id

        risk_score = report["riskScore"]

        if risk_score>=70:
            report["riskLevel"] = "HIGH"
        elif risk_score>=50:
            report["riskLevel"] = "MEDIUM"
        else:
            report["riskLevel"]="LOW"

        return report
    
    except Exception as e:
        print("\nJSON PARSE ERROR")
        print(e)

        return {
            "riskScore": 0,
            "summary": "Unable to parse agent response.",
            "recommendation": "Manual review required."
        }