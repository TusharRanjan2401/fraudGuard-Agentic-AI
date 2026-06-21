from services.api_client import login
from graph.graph_builder import build_graph

def main():
    login()
    graph = build_graph()
    result = graph.invoke(
        {
            "alert_id":alert_id
        }
    )
    print("\nFinal state:")
    print(result)

if __name__=="__main__":
    main()