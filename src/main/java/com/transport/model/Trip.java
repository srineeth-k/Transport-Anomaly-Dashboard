package com.transport.model;

import java.util.List;

public class Trip {

    public String vehicleNo;

    public String routeKey;

    public String onwardReturn;

    public List<GPSRecord> gpsSegments;

    public double totalDistance;

    public long totalDurationMinutes;

    public boolean routeOutlier;

    public boolean timeOutlier;

}