from services.api_client import save_report

def save_report_node(state):
    payload = {

    "alertId": state["alert_id"],

    "transactionId": state["report"]["transactionId"],

    "riskScore":
        state["report"]["riskScore"],

    "summary":
        state["report"]["summary"],

    "recommendation":
        state["report"]["recommendation"],
        
    "pdfPath": state["pdf_path"]
}
    result = save_report(payload)
    return {
        "saved_report":result
    }