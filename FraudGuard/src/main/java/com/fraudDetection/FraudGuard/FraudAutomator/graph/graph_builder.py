from langgraph.graph import StateGraph, START, END

from graph.state import InvestigationState
from nodes.save_report import save_report_node
from nodes.investigation_agent_node import investigation_agent_node
from nodes.pdf_generation_node import pdf_generation_node

def build_graph():
    builder = StateGraph(InvestigationState)

    builder.add_node("investigation_agent",investigation_agent_node)
    builder.add_node("save_report",save_report_node)
    builder.add_node("pdf_generation", pdf_generation_node)

    builder.add_edge(START,"investigation_agent")
    builder.add_edge("investigation_agent","pdf_generation")
    builder.add_edge("pdf_generation","save_report")
    builder.add_edge("save_report",END)

    return builder.compile()
