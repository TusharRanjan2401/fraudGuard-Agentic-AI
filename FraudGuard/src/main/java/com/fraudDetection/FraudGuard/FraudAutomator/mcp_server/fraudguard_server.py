from mcp.server.fastmcp import FastMCP
from services.api_client import (
    login,
    get_alert,
    get_transaction,
    get_user_history,
    save_report,

)

mcp = FastMCP("FraudGuard Server")

@mcp.tool()
def alert_tool(alert_id:int):
    """
    Fetch fraud alert details by alert id.
    """
    return get_alert(alert_id)

@mcp.tool()
def transaction_tool(transaction_id:int):
    """
    Fetch transaction details by transaction id.
    """
    return get_transaction(transaction_id)

@mcp.tool()
def history_tool(account_number:str):
    """
    Fetch transaction history for an account.
    """
    return get_user_history(account_number)


@mcp.tool()
def report_tool(payload:dict):
    """
    Save investigation report.
    """
    return save_report(payload)

@mcp.tool()
def account_risk_tool(account_number:str):
    """
    Analyze historical transactions and return a risk score.
    """
    history = get_user_history(account_number)
    transaction_count = len(history)
    risk_score = min(transaction_count*5,100)

    return {
        "accountNumber":account_number,
        "transactionCount":transaction_count,
        "riskScore":risk_score
    }


if __name__=="__main__":
    login()
    mcp.run(
        transport="streamable-http"
    )