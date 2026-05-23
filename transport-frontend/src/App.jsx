import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import Outliers from "./pages/Outliers";
import TripDetails from "./pages/TripDetails";

import Navbar from "./components/Navbar";

function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        <Route path="/" element={<Home />} />

        <Route path="/outliers" element={<Outliers />} />

        <Route path="/trip/:vehicleNo" element={<TripDetails />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
