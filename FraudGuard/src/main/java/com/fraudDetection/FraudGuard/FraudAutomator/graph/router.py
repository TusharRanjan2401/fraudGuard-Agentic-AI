def route_investigation(state):

    if state["investigation_needed"]:
        return "fetch_history"
    
    return "generate_report"