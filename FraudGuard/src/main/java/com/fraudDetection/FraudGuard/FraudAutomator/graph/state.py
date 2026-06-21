from typing import TypedDict

class InvestigationState(TypedDict):
    alert_id:int 
    # alert:dict
    # transaction: dict
    # history:dict
    # blacklist_result:dict
    # account_risk:int
    report:dict
    # investigation_needed: bool
    pdf_path:str

