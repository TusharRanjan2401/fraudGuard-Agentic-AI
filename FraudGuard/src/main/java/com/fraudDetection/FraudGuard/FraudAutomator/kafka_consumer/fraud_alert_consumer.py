import json
import os
from kafka import KafkaConsumer
from graph.graph_builder import build_graph
from dotenv import load_dotenv
from services.api_client import login

load_dotenv()
login()

KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS")
KAFKA_TOPIC = os.getenv("KAFKA_TOPIC")
KAFKA_GROUP_ID= os.getenv("KAFKA_GROUP_ID")

consumer = KafkaConsumer(
    KAFKA_TOPIC,
    bootstrap_servers = KAFKA_BOOTSTRAP_SERVERS,
    value_deserializer= lambda x:
        json.loads(x.decode("utf-8")),
    auto_offset_reset="earliest",
    group_id=KAFKA_GROUP_ID
)

graph = build_graph()

print("Fraud Automator Started...")

for message in consumer:
    event = message.value
    alert_id = event["alertId"]

    print(f"Received Alert Id: {alert_id}")

    try:
        graph.invoke(
            {
                "alert_id":alert_id
            }
        )
        print(f"Investigation Completed for Alert {alert_id}")

    except Exception as e:
        print(f"Investigation Failed: {e}")