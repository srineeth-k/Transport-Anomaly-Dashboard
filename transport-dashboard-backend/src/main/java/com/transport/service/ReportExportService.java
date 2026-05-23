package com.transport.service;

import com.transport.model.Trip;

import java.io.FileWriter;
import java.util.List;

public class ReportExportService {

    public void exportToCSV(List<Trip> trips, String filePath) {

        try {

            FileWriter writer = new FileWriter(filePath);

            writer.write(
                    "Vehicle,Route,OnwardReturn,TripStart,TripEnd," +
                    "TotalDistance,DurationMinutes,AverageSpeed," +
                    "ActualGPSDistance,GPSShortestDistance,GPSExtraDistance," +
                    "GPSDeviationScore,RouteOutlier,TimeOutlier," +
                    "GPSPatternOutlier,ShortestPathOutlier," +
                    "RiskScore,RiskLevel,SuggestedPath,Reasons\n"
            );

            for (Trip trip : trips) {
                writer.write(
                        safe(trip.vehicleNo) + "," +
                        quote(trip.routeKey) + "," +
                        safe(trip.onwardReturn) + "," +
                        safe(String.valueOf(trip.tripStartTime)) + "," +
                        safe(String.valueOf(trip.tripEndTime)) + "," +
                        trip.totalDistance + "," +
                        trip.totalDurationMinutes + "," +
                        trip.averageSpeed + "," +
                        trip.actualGPSDistance + "," +
                        trip.gpsShortestDistance + "," +
                        trip.gpsExtraDistance + "," +
                        trip.gpsDeviationScore + "," +
                        trip.routeOutlier + "," +
                        trip.timeOutlier + "," +
                        trip.gpsPatternOutlier + "," +
                        trip.shortestPathOutlier + "," +
                        trip.riskScore + "," +
                        safe(trip.riskLevel) + "," +
                        quote(trip.suggestedPath) + "," +
                        quote(String.valueOf(trip.anomalyReasons)) +
                        "\n"
                );
            }

            writer.close();

            System.out.println("CSV report exported successfully: " + filePath );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String safe(String value) {

        if (value == null) {
            return "";
        }

        return value.replace(",", " ");
    }

    private String quote(String value) {

        if (value == null) {
            return "\"\"";
        }

        value = value.replace("\"", "\"\"");

        return "\"" + value + "\"";
    }
}