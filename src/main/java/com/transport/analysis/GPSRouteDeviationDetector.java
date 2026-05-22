
package com.transport.analysis;

import com.transport.model.Trip;
import java.util.*;

public class GPSRouteDeviationDetector {

    public void detectGPSDeviation(List<Trip> trips){

        Map<String,List<Trip>> routeMap=new HashMap<>();
        for(Trip trip:trips){
            routeMap.computeIfAbsent(trip.routeKey,k->new ArrayList<>()).add(trip);
        }

        for(String route:routeMap.keySet()){
            List<Trip> routeTrips=routeMap.get(route);

            if(routeTrips.size()<2)continue;
            double totalLat=0;
            double totalLon=0;

            for(Trip trip:routeTrips){
                totalLat+=trip.averageLatitude;
                totalLon+=trip.averageLongitude;
            }
            double routeAvgLat=totalLat/routeTrips.size();
            double routeAvgLon=totalLon/routeTrips.size();

            for(Trip trip:routeTrips){
                double deviation=calculateDistance(
                    trip.averageLatitude,
                    trip.averageLongitude,
                    routeAvgLat,
                    routeAvgLon);
                trip.gpsDeviationScore=deviation;

                if(deviation>30){
                    trip.gpsOutlier=true;
                }
            }
        }
    }

    private double calculateDistance(double lat1,double lon1,double lat2,double lon2){

        double R=6371;
        double dLat=Math.toRadians(lat2-lat1);
        double dLon=Math.toRadians(lon2-lon1);

        double a=Math.sin(dLat/2)*Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) 
        	* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2)*Math.sin(dLon/2);

        double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return R*c;
    }
}