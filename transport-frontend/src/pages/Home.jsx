import { useEffect, useState } from "react";

import api from "../api";

import SummaryCard from "../components/SummaryCard";
import { PieChart, Pie, Cell, Tooltip, Legend } from "recharts";

function Home() {
  const [summary, setSummary] = useState({});

  useEffect(() => {
    api.get("/summary").then((res) => {
      console.log(res.data);

      setSummary(res.data);
    });
  }, []);

  const riskData = [
    {
      name: "High Risk",
      value: summary.highRiskTrips || 0,
    },
    {
      name: "Medium Risk",
      value: summary.mediumRiskTrips || 0,
    },
    {
      name: "Low Risk",
      value: summary.lowRiskTrips || 0,
    },
  ];

  const COLORS = ["#991b1b", "#f59e0b", "#16a34a"];
  return (
    <div>
      <h1>Transport Dashboard</h1>

      <div
        style={{
          display: "flex",
          gap: "20px",
          flexWrap: "wrap",
        }}
      >
        <SummaryCard
          title="Total Trips"
          value={summary.totalTrips}
          color="#2563eb"
        />

        <SummaryCard
          title="Outlier Trips"
          value={summary.outlierTrips}
          color="#dc2626"
        />

        <SummaryCard
          title="High Risk"
          value={summary.highRiskTrips}
          color="#991b1b"
        />

        <SummaryCard
          title="Medium Risk"
          value={summary.mediumRiskTrips}
          color="#f59e0b"
        />

        <SummaryCard
          title="Low Risk"
          value={summary.lowRiskTrips}
          color="#16a34a"
        />
      </div>

      <div style={{ marginTop: "40px" }}>
        <h2>Risk Distribution</h2>

        <PieChart width={400} height={300}>
          <Pie
            data={riskData}
            dataKey="value"
            nameKey="name"
            cx="50%"
            cy="50%"
            outerRadius={100}
            label
          >
            {riskData.map((entry, index) => (
              <Cell key={index} fill={COLORS[index]} />
            ))}
          </Pie>

          <Tooltip />
          <Legend />
        </PieChart>
      </div>
    </div>
  );
}

export default Home;
