function SummaryCard({ title, value, color }) {
  return (
    <div
      style={{
        background: color,
        color: "white",
        padding: "20px",
        width: "220px",
        borderRadius: "12px",
        boxShadow: "0px 4px 10px rgba(0,0,0,0.2)",
      }}
    >
      <h3>{title}</h3>

      <h1>{value}</h1>
    </div>
  );
}

export default SummaryCard;
