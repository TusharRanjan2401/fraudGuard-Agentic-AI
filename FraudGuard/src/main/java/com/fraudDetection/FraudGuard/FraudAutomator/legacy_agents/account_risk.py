def account_risk_node(state):
    history = state["history"]
    transaction_count = len(history)
    risk_score = min(
        transaction_count*2,
        100
    )