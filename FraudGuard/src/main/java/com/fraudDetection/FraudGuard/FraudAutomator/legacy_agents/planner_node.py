def planner_node(state):
    transaction = state["transaction"]
    amount = transaction["amount"]
    if amount>100000:
        print("High risk transaction detected")
        return{
            "investigation_needed":True
        }
    print("Low risk transaction")
    return {
        "investigation_needed":False
    }