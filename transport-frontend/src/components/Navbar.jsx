import { Link } from "react-router-dom";

function Navbar() {
  return (
    <nav
      style={{
        padding: "20px",
        display: "flex",
        gap: "20px",
        background: "#ddd",
      }}
    >
      <Link to="/">Dashboard</Link>

      <Link to="/outliers">Outliers</Link>
    </nav>
  );
}

export default Navbar;
