package com.transport.dto;

import java.util.List;

public class TripResponseDTO {

    public String vehicleNo;
    public String routeKey;
    public String onwardReturn;

    public String tripStartTime;
    public String tripEndTime;

    public double totalDistance;
    public long totalDurationMinutes;
    public double averageSpeed;

    public double actualGPSDistance;
    public double gpsShortestDistance;
    public double gpsExtraDistance;
    public double gpsDeviationScore;

    public boolean routeOutlier;
    public boolean timeOutlier;
    public boolean gpsPatternOutlier;
    public boolean shortestPathOutlier;

    public double riskScore;
    public String riskLevel;

    public String suggestedPath;

    public List<String> anomalyReasons;
}