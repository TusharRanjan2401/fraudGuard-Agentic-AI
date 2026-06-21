import React from "react";

const AlertList = ({ alerts }) => {
  return (
    <div className="overflow-auto h-96 bg-white shadow rounded p-2">
      <table className="w-full table-auto border-collapse">
        <thead>
          <tr className="bg-gray-200">
            <th className="px-2 py-1 border">Message</th>
            <th className="px-2 py-1 border">Type</th>
            <th className="px-2 py-1 border">Timestamp</th>
          </tr>
        </thead>
        <tbody>
          {alerts.length === 0 ? (
            <tr>
              <td colSpan="3" className="text-center py-4">
                No alerts yet
              </td>
            </tr>
          ) : (
            alerts.map((alert, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="px-2 py-1 border">{alert.message}</td>
                <td className="px-2 py-1 border">{alert.type}</td>
                <td className="px-2 py-1 border">{new Date(alert.timestamp).toLocaleString()}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default AlertList;
