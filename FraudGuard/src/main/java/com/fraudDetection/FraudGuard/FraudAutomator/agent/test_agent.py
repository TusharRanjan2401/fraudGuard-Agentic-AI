import asyncio
from agent.fraud_investigation_agent import investigation_alert

async def main():
    response= await investigation_alert(1)
    print("\nFinal Response:")
    print(response)

if __name__=="__main__":
    asyncio.run(main())