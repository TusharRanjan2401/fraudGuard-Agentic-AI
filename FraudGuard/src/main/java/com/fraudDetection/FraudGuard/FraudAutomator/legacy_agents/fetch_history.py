from services.api_client import get_user_history

def fetch_history_node(state):
    sender_account = (
        state["transaction"]
        ["senderAccount"]
    )
    history = get_user_history(
        sender_account
    )
    return {
        "history":history
    }