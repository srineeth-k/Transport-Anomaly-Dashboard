import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

function Outliers() {
  const [outliers, setOutliers] = useState([]);
  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState("risk");

  const navigate = useNavigate();

  useEffect(() => {
    api.get("/outliers").then((res) => {
      setOutliers(res.data);
    });
  }, []);

  const filteredOutliers = outliers
    .filter((trip) => {
      const text = `${trip.vehicleNo} ${trip.routeKey} ${trip.riskLevel}`;
      return text.toLowerCase().includes(search.toLowerCase());
    })
    .sort((a, b) => {
      if (sortBy === "dateDesc") {
        return new Date(b.tripStartTime) - new Date(a.tripStartTime);
      }

      if (sortBy === "dateAsc") {
        return new Date(a.tripStartTime) - new Date(b.tripStartTime);
      }

      const riskPriority = {
        HIGH: 3,
        MEDIUM: 2,
        LOW: 1,
      };

      return riskPriority[b.riskLevel] - riskPriority[a.riskLevel];
    });

  return (
    <div style={{ padding: "20px" }}>
      <h1>Outlier Trips</h1>

      <input
        type="text"
        placeholder="Search by vehicle, route, or risk level..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        style={{
          padding: "10px",
          marginRight: "10px",
          width: "350px",
          marginBottom: "20px",
          borderRadius: "8px",
          border: "1px solid #ccc",
        }}
      />

      <button
        onClick={() => {
          window.location.href ="https://transport-anomaly-dashboard-1.onrender.com/api/outliers/export";
        }}
        style={{
          padding: "10px 16px",
          marginBottom: "20px",
          marginRight: "15px",
          background: "#16a34a",
          color: "white",
          border: "none",
          borderRadius: "8px",
          cursor: "pointer",
        }}
      >
        Download Outlier Report
      </button>
      <select
        value={sortBy}
        onChange={(e) => setSortBy(e.target.value)}
        style={{
          padding: "10px",
          marginLeft: "10px",
          borderRadius: "8px",
          border: "1px solid #ccc",
        }}
      >
        <option value="risk">Risk High to Low</option>
        <option value="dateDesc">Date Newest First</option>
        <option value="dateAsc">Date Oldest First</option>
      </select>
      <table
        style={{
          width: "100%",
          borderCollapse: "collapse",
          background: "white",
        }}
      >
        <thead
          style={{
            background: "#2563eb",
            color: "white",
          }}
        >
          <tr>
            <th style={thStyle}>Vehicle</th>
            <th style={thStyle}>Dispatch Time</th>
            <th style={thStyle}>Route</th>
            <th style={thStyle}>Risk</th>
            <th style={thStyle}>Score</th>
            <th style={thStyle}>Extra KM</th>
            <th style={thStyle}>Reasons</th>
          </tr>
        </thead>

        <tbody>
          {filteredOutliers.map((trip, index) => (
            <tr
              key={index}
              onClick={() =>navigate(`/trip/${outliers.indexOf(trip)}`)}
              style={{
                cursor: "pointer",
                borderBottom: "1px solid #ddd",
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.background = "#f3f4f6";
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.background = "white";
              }}
            >
              <td style={tdStyle}>{trip.vehicleNo}</td>

              <td style={tdStyle}>
                {new Date(trip.tripStartTime).toLocaleString()}
              </td>

              <td style={tdStyle}>{trip.routeKey}</td>

              <td style={tdStyle}>
                <span
                  style={{
                    background:
                      trip.riskLevel === "HIGH"
                        ? "#dc2626"
                        : trip.riskLevel === "MEDIUM"
                          ? "#f59e0b"
                          : "#16a34a",
                    padding: "6px 10px",
                    borderRadius: "8px",
                    color: "white",
                    fontWeight: "bold",
                  }}
                >
                  {trip.riskLevel}
                </span>
              </td>

              <td style={tdStyle}>{trip.riskScore?.toFixed(2)}</td>

              <td style={tdStyle}>{trip.gpsExtraDistance?.toFixed(2)}</td>

              <td style={tdStyle}>
                {trip.anomalyReasons?.length > 0 ? (
                  trip.anomalyReasons.map((reason, i) => (
                    <div key={i}>• {reason}</div>
                  ))
                ) : (
                  <span>No reason available</span>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {filteredOutliers.length === 0 && (
        <p style={{ marginTop: "20px" }}>No matching outlier trips found.</p>
      )}
    </div>
  );
}

const thStyle = {
  padding: "12px",
  textAlign: "left",
};

const tdStyle = {
  padding: "12px",
  verticalAlign: "top",
};

export default Outliers;
