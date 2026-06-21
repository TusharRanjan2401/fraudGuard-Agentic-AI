import { useEffect, useState } from "react";
import axios from "axios";
import { useSelector } from "react-redux";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import Navbar from "../components/Navbar";

const Dashboard = () => {
  const [transactions, setTransactions] = useState([]);
  const token = localStorage.getItem("token");
  const user = JSON.parse(atob(token.split(".")[1]));

  const fetchTransactions = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/v1/transactions/sender/${user.accountNumber}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setTransactions(response.data);
    } catch (err) {
     
    }
  };

  useEffect(() => {
    fetchTransactions();

    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      client.subscribe("/topic/transactions/{username}", (message) => {
        const data = JSON.parse(message.body);
        if (data.senderAccount === user.accountNumber) {
            setTransactions((prev) => {
              const exists = prev.some((tx) => tx.id === data.id);
              if (exists) return prev; 
              return [data, ...prev];
            });
        }
      });
    };

    client.activate();
    return () => client.deactivate();
  }, []);

  return (
    <div>
      <Navbar />
      <div className="min-h-screen bg-gray-300 p-6">
        <h2 className="text-3xl font-bold mb-6">My Transactions</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {transactions.map((tx) => (
            <div
              key={tx.id}
              className="bg-white p-5 rounded-xl shadow-md hover:shadow-xl transition transform hover:-translate-y-1"
              >
                  
              <p>
                <strong>Receiver:</strong> {tx.receiverAccount}
              </p>
              <p>
                <strong>Amount:</strong> ${tx.amount}
              </p>
              <p>
                <strong>Timestamp:</strong> {new Date(tx.timestamp).toLocaleString()}
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
