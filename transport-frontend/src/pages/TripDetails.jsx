import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api";

function TripDetails() {
  const { vehicleNo } = useParams();

  const [trip, setTrip] = useState(null);

  useEffect(() => {
    api.get(`/trip/${vehicleNo}`).then((res) => {
      setTrip(res.data);
    });
  }, [vehicleNo]);

  if (!trip) {
    return <h2>Loading...</h2>;
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>Trip Details</h1>

      <h2>{trip.vehicleNo}</h2>

      <p>
        <b>Dispatch Time: </b>

        {new Date(trip.tripStartTime).toLocaleString()}
      </p>

      <p>
        <b>Completion Time: </b>

        {new Date(trip.tripEndTime).toLocaleString()}
      </p>

      <p>
        <b>Route:</b> {trip.routeKey}
      </p>

      <p>
        <b>Risk Level:</b> {trip.riskLevel}
      </p>

      <p>
        <b>Risk Score:</b>
        {trip.riskScore?.toFixed(2)}
      </p>

      <p>
        <b>Actual GPS:</b>
        {trip.actualGPSDistance?.toFixed(2)}
        km
      </p>

      <p>
        <b>Shortest GPS:</b>
        {trip.gpsShortestDistance?.toFixed(2)}
        km
      </p>

      <p>
        <b>Extra Distance:</b>
        {trip.gpsExtraDistance?.toFixed(2)}
        km
      </p>

      <h3>Suggested Path</h3>

      <p>{trip.suggestedPath}</p>

      <h3>Reasons</h3>

      {trip.anomalyReasons?.map((reason, index) => (
        <p key={index}>• {reason}</p>
      ))}
    </div>
  );
}

export default TripDetails;
