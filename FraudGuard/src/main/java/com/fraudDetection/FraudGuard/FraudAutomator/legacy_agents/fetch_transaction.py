from services.api_client import get_transaction

def fetch_transaction_node(state):
    transaction_id = state["alert"]["transactionId"]
    transaction = get_transaction(
        transaction_id
    )

    return {
        "transaction":transaction
    }