import asyncio
from agent.fraud_investigation_agent import investigation_alert

def investigation_agent_node(state):
    report = asyncio.run(
        investigation_alert(
            state["alert_id"]
        )
    )

    return {
        "report":report
    }