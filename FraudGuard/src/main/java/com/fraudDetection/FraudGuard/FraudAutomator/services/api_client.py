import requests
import os 
from dotenv import load_dotenv

load_dotenv()

BASE_URL=os.getenv("BASE_URL")

ADMIN_EMAIL=os.getenv("ADMIN_EMAIL")

ADMIN_USERNAME=os.getenv("ADMIN_USERNAME") 

ADMIN_PASSWORD=os.getenv("ADMIN_PASSWORD")

session = requests.Session()

def login():
    """
    Login to FraudGuard and store JWT in session headers
    """
    response = requests.post(
         f"{BASE_URL}/auth/v1/login",
         json = {
              "username":ADMIN_USERNAME,
              "password":ADMIN_PASSWORD
          }
     )
    response.raise_for_status()
    token = response.json()["token"]
    session.headers.update(
         {
              "Authorization": f"Bearer {token}"
         }
    )
    print("Successfully Authenticated!")
    return token


def get_alert(alert_id):
    response = session.get(
        f"{BASE_URL}/api/v1/alerts/{alert_id}"  
    )
    response.raise_for_status()
    return response.json()

def get_transaction(transaction_id):
     response = session.get(
        f"{BASE_URL}/api/v1/transactions/{transaction_id}" 
    )
     response.raise_for_status()
     return response.json()

def get_user_history(account_number):
     response = session.get(
        f"{BASE_URL}/api/v1/transactions/history/{account_number}" 
    )
     response.raise_for_status()
     return response.json()

def save_report(payload):
      print(session.headers)
      response = session.post(
        f"{BASE_URL}/api/v1/reports",
        json=payload
    )
      print("STATUS:", response.status_code)
      print("BODY:", response.text)
      response.raise_for_status()
      
      return response.json()