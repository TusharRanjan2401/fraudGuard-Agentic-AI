import { useEffect, useState } from "react";
import axios from "axios";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import Navbar from "../components/Navbar";

const AdminDashboard = () => {
  const [alerts, setAlerts] = useState([]);
  const token = localStorage.getItem("token");
  const [selectedReport, setSelectedReport] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const fetchAlerts = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/v1/alerts/all",
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );
      setAlerts(response.data);
    } catch (err) {}
  };

  useEffect(() => {
    fetchAlerts();

    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      client.subscribe("/topic/alerts", (message) => {
        const data = JSON.parse(message.body);
        setAlerts((prev) => [data, ...prev]);
      });

      client.subscribe("/topic/nonfraud", (message) => {
        const data = JSON.parse(message.body);
        setAlerts((prev) => [data, ...prev]);
      });
    };

    client.activate();
    return () => client.deactivate();
  }, []);

  const viewInvestigation = async (alertId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/v1/reports/alert/${alertId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      setSelectedReport(response.data);
      setShowModal(true);
    } catch (error) {
      alert("Investigation report not available yet.");
      console.log("Error fetching report", error);
    }
  };

  // const downloadPdf = async (alertId) => {
  //   try {
  //     const response = await axios.get(
  //       `http://localhost:8080/api/v1/reports/pdf/${alertId}`,
  //       {
  //         responseType: "blob",
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //         },
  //       },
  //     );
  //     const url = window.URL.createObjectURL(new Blob([response.data]));
  //     const link = document.createElement("a");
  //     link.href = url;
  //     link.setAttribute("download", `report_${alertId}.pdf`);
  //     document.body.appendChild(link);
  //     link.click();
  //     link.remove();
  //   } catch (error) {
  //     console.log(error);
  //     alert("Failed to download PDF.");
  //   }
  // };

  return (
    <div>
      <Navbar />
      <div className="min-h-screen bg-gray-300 p-6">
        <h2 className="text-3xl font-bold mb-6">Admin Alerts</h2>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          {/* Cards for stats */}
          <div className="bg-red-100 p-5 rounded-xl shadow-md">
            <h3 className="font-bold text-xl mb-2">Fraud Transactions</h3>
            <p className="text-red-600 font-bold text-2xl">
              {alerts.filter((tx) => tx.status === "FRAUD").length}
            </p>
          </div>
          <div className="bg-green-100 p-5 rounded-xl shadow-md">
            <h3 className="font-bold text-xl mb-2">Safe Transactions</h3>
            <p className="text-green-600 font-bold text-2xl">
              {alerts.filter((tx) => tx.status === "SAFE").length}
            </p>
          </div>
          <div className="bg-blue-200 p-5 rounded-xl shadow-md">
            <h3 className="font-bold text-xl mb-2">Total Alerts</h3>
            <p className="text-blue-600 font-bold text-2xl">{alerts.length}</p>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="min-w-full bg-white rounded-lg shadow-md">
            <thead>
              <tr className="bg-gray-200">
                <th className="py-2 px-4">Alert ID</th>
                <th className="py-2 px-4">Sender</th>
                <th className="py-2 px-4">Receiver</th>
                <th className="py-2 px-4">Amount</th>
                <th className="py-2 px-4">Status</th>
                <th className="py-2 px-4">Timestamp</th>
                <th className="py-2 px-4">Actions</th>
              </tr>
            </thead>
            <tbody>
              {alerts.map((tx) => (
                <tr
                  key={tx.id}
                  className={
                    tx.status === "FRAUD" ? "bg-red-100" : "bg-green-50"
                  }
                >
                  <td className="py-2 px-4">{tx.id}</td>
                  <td className="py-2 px-4">{tx.senderAccount}</td>
                  <td className="py-2 px-4">{tx.receiverAccount}</td>
                  <td className="py-2 px-4">${tx.amount}</td>
                  <td
                    className={`py-2 px-4 font-bold ${
                      tx.status === "FRAUD" ? "text-red-600" : "text-green-600"
                    }`}
                  >
                    {tx.status}
                  </td>
                  <td className="py-2 px-4">
                    {tx.createdAt
                      ? new Date(tx.createdAt).toLocaleString()
                      : "N/A"}
                  </td>
                  <td className="py-2 px-4">
                    {tx.status === "FRAUD" ? (
                      <button
                        onClick={() => viewInvestigation(tx.id)}
                        className="bg-blue-500 text-white px-3 py-1 rounded"
                      >
                        View Report
                      </button>
                    ) : (
                      <span className="text-gray-500 font-medium">
                        No Investigation
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      {showModal && selectedReport && (
        <div className="fixed inset-0 bg-black/50 flex justify-center items-center">
          <div className="bg-white p-6 rounded-xl w-[700px]">
            <h2 className="text-2xl font-bold mb-4">
              Fraud Investigation Report
            </h2>
            <div>
              <strong>Alert ID:</strong> {selectedReport.alertId}
            </div>

            <div>
              <strong>Transaction ID:</strong> {selectedReport.transactionId}
            </div>

            <div>
              <strong>Created At:</strong>{" "}
              {new Date(selectedReport.createdAt).toLocaleString()}
            </div>
            <div>
              <strong>Risk Score:</strong> {selectedReport.riskScore}
              <div>
                <strong>Summary:</strong>
                <p className="mt-2">{selectedReport.summary}</p>
              </div>
              <div>
                <strong>Recommendation:</strong>
                <p className="mt-2">{selectedReport.recommendation}</p>
              </div>
            </div>
            <button
              className="mt-4 bg-red-500 text-white px-4 py-2 rounded"
              onClick={() => setShowModal(false)}
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
