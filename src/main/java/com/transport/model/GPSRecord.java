package com.transport.model;

import java.time.LocalDateTime;

public class GPSRecord {

    public String vehicleNo;

    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public String startLocation;
    public String endLocation;

    public double startLatitude;
    public double startLongitude;

    public double endLatitude;
    public double endLongitude;

    public double distanceKm;

    public long durationMinutes;

    @Override
    public String toString() {

        return "\nGPSRecord{" +
                "\nvehicleNo='" + vehicleNo + '\'' +
                "\nstartTime=" + startTime +
                "\nendTime=" + endTime +
                "\nstartLocation='" + startLocation + '\'' +
                "\nendLocation='" + endLocation + '\'' +
                "\nstartLatitude=" + startLatitude +
                "\nstartLongitude=" + startLongitude +
                "\nendLatitude=" + endLatitude +
                "\nendLongitude=" + endLongitude +
                "\ndistanceKm=" + distanceKm +
                "\ndurationMinutes=" + durationMinutes +
                "\n}";
    }
}