from llm.groq_client import llm
import json

def generate_report_node(state):
    alert = state["alert"]

    transaction = state["transaction"]

    history = state["history"]

    prompt = f"""
You are a banking fraud investigator.
Analyze:
Alert:{alert}
Transaction:{transaction}
User History: {history}
Generate:
1. Risk Score(0-100)
2. Summary
3.Recommendation

Return JSON only.
"""
    report = llm.invoke(prompt)
    response = json.loads(report.content)
    print(response)
    return {
        "report":response
    }