
import React from "react";

const TransactionList = ({ transactions }) => {
  const getStatusColor = (status) => {
    if (status === "FLAGGED") return "bg-red-100 text-red-800";
    if (status === "COMPLETED") return "bg-green-100 text-green-800";
    return "";
  };

  return (
    <div className="overflow-auto h-96 bg-white shadow rounded-xl p-4">
      <table className="w-full table-auto border-collapse">
              <thead>
                      <tr className="bg-gray-200">
                          <th className="px-3 py-2 border">Sender</th>
                          <th className="px-3 py-2 border">Sender Account</th>
                          <th className="px-3 py-2 border">Receiver Account</th>
                          <th className="px-3 py-2 border">Amount</th>
                          <th className="px-3 py-2 border">Status</th>
                          <th className="px-3 py-2 border">Timestamp</th>
                      </tr>
                  
        </thead>
        <tbody>
          {transactions.length === 0 ? (
            <tr>
              <td colSpan="6" className="text-center py-4">
                No transactions
              </td>
            </tr>
          ) : (
            transactions.map((tx) => (
              <tr key={tx.id} className="hover:bg-gray-50">
                <td className="px-3 py-2 border">{tx.senderUsername || tx.sender}</td>
                <td className="px-3 py-2 border">{tx.senderAccount}</td>
                <td className="px-3 py-2 border">{tx.receiverAccount}</td>
                <td className="px-3 py-2 border">{tx.amount}</td>
                <td className={`px-3 py-2 border font-semibold ${getStatusColor(tx.status)}`}>
                  {tx.status}
                </td>
                <td className="px-3 py-2 border">{new Date(tx.timestamp).toLocaleString()}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default TransactionList;
