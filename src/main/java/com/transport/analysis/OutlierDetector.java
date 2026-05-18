package com.transport.analysis;

import com.transport.model.Trip;
import java.util.*;

public class OutlierDetector {
	public void detectOutliers(List<Trip> trips) {
		Map<String, List<Trip>> routeMap = new HashMap<>();
		
		//GROUPING TRIPS BY ROUTE
		for(Trip trip :trips) {
			routeMap.computeIfAbsent(trip.routeKey, k -> new ArrayList<>()).add(trip);
		}
		
		//PROCESSING EACH ROUTE
		for(String route: routeMap.keySet()) {
			List<Trip> routeTrips =routeMap.get(route);
			if (routeTrips.size() < 2)
                continue;
			double totalDistance = 0;
            double totalDuration = 0;
            
            //calculating avg distance and time
            for(Trip trip:routeTrips){
            	totalDistance+=trip.totalDistance;
            	totalDuration+=trip.totalDurationMinutes;
            	}
            double avgDistance=totalDistance/routeTrips.size();
            double avgDuration=totalDuration/routeTrips.size();
            for(Trip trip:routeTrips){
            	if(trip.totalDistance>avgDistance*1.3){
            		trip.routeOutlier=true;
            	}
            	if(trip.totalDurationMinutes>avgDuration*1.5){
            		trip.timeOutlier=true;
            	}
            	}
		}
	}
}
