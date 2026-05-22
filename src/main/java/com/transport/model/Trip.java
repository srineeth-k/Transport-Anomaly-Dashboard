package com.transport.model;

import java.time.LocalDateTime;
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
    public boolean gpsOutlier;
    public DispatchRecord dispatchRecord;
    public LocalDateTime tripStartTime;
    public LocalDateTime tripEndTime;
    public double averageSpeed;
    public double averageLatitude;
    public double averageLongitude;
    public double gpsDeviationScore;
    public double expectedDistance;
    public double gpsShortestDistance;
    public double gpsExtraDistance;
    public String suggestedPath;
    public boolean shortestPathOutlier;
    public double actualGPSDistance;
    
    @Override
    public String toString(){
		return "\nTRIP {" +

                "\nvehicleNo='" + vehicleNo + '\'' +

                "\nrouteKey='" + routeKey + '\'' +

                "\nonwardReturn='" + onwardReturn + '\'' +

                "\ntripStartTime=" + tripStartTime +

                "\ntripEndTime=" + tripEndTime +

                "\ntotalDistance=" + totalDistance +

                "\ntotalDurationMinutes=" + totalDurationMinutes +

                "\naverageSpeed=" + averageSpeed +

                "\ngpsSegments=" + (gpsSegments == null ? 0 : gpsSegments.size()) +

                "\nrouteOutlier=" + routeOutlier +

                "\ntimeOutlier=" + timeOutlier +
                
                "\ngpsDeviationScore=" + gpsDeviationScore +
                
                 "\nactualGPSDistance=" + actualGPSDistance +

                 "\ngpsShortestDistance=" + gpsShortestDistance +

                 "\ngpsExtraDistance=" + gpsExtraDistance +

                 "\nsuggestedPath='" + suggestedPath + '\'' +

                 "\nshortestPathOutlier=" + shortestPathOutlier +

                "\n}";
    }
    }

