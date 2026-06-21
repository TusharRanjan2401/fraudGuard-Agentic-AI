import asyncio
from langchain_mcp_adapters.client import MultiServerMCPClient

async def main():
    client = MultiServerMCPClient(
        {
            "fraudguard":{
                "transport":"streamable_http",
                "url":"http://localhost:8000/mcp"
            }
        }
    )

    tools = await client.get_tools()

    print(tools)

asyncio.run(main())