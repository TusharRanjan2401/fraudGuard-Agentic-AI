from services.api_client import get_alert

def fetch_alert_node(state):
    alert = get_alert(
        state["alert_id"]
    )

    return {"alert":alert }