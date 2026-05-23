package com.transport.service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.transport.model.DispatchRecord;
import com.transport.model.GPSRecord;
import com.transport.model.Trip;

public class TripMatcher {
	public List<Trip>buildTrips(List<DispatchRecord> dispatches, List<GPSRecord> gpsRecords){
		List<Trip> trips=new ArrayList<>();
		
		//TRACKS ONLY UNIQUE TRIPS(ENSURES SAME TRIPS WILL NOT BE ADDED INTO  OUTPUT)
		Set<String> uniqueTrips =  new HashSet<>();
		
		for(DispatchRecord dispatch : dispatches) {
			List<GPSRecord> matchedGPS = new ArrayList<>();
			
			//checking for matching gps record
			for(GPSRecord gps: gpsRecords) {
				if(gps.startTime==null) {
					continue;
				}
				boolean sameVehicle= dispatch.vehicleNo.equalsIgnoreCase(gps.vehicleNo);
				
				LocalDate gpsDate= gps.startTime.toLocalDate();
				boolean sameDate=dispatch.dispatchDate.equals(gpsDate);
				
				if(sameVehicle && sameDate) {
					matchedGPS.add(gps);
				}
			}
			if(matchedGPS.isEmpty())continue;
			
			Trip trip=new Trip();
			
			trip.vehicleNo=dispatch.vehicleNo;
			trip.routeKey= dispatch.fromName + " -> " + dispatch.toName;
			trip.onwardReturn = dispatch.onwardReturn;
			trip.dispatchRecord = dispatch;
			trip.gpsSegments = matchedGPS;
			trip.tripStartTime = matchedGPS.get(0).startTime;
			trip.tripEndTime = matchedGPS.get(matchedGPS.size()-1).endTime;
			
	        double totalDistance = 0;
	        long totalMinutes = 0;
	        double totalLat = 0;
	        double totalLon = 0;
	        int gpsPointCount = 0;
	        double actualGPSDistance = 0;
	        
	        for (GPSRecord gps : matchedGPS) {     	
	            totalDistance += gps.distanceKm;
	            totalMinutes += gps.durationMinutes;
	            
	            totalLat += gps.startLatitude;
	            totalLon += gps.startLongitude;
	            gpsPointCount++;

	            totalLat += gps.endLatitude;
	            totalLon += gps.endLongitude;
	            gpsPointCount++;
	            
	            actualGPSDistance += calculateDistance(
	                    gps.startLatitude,
	                    gps.startLongitude,
	                    gps.endLatitude,
	                    gps.endLongitude
	            );
	        }
	        trip.totalDistance = totalDistance;
	        trip.totalDurationMinutes = totalMinutes;
	        trip.actualGPSDistance = actualGPSDistance;
	        
	        if (gpsPointCount > 0) {
	            trip.averageLatitude =  totalLat / gpsPointCount;
	            trip.averageLongitude = totalLon / gpsPointCount;
	        }
	        
	        System.out.println(
	                "TRIP GPS CENTER = "
	                        + trip.averageLatitude
	                        + " , "
	                        + trip.averageLongitude
	        );
	        // AVG SPEED
	        if (totalMinutes > 0) {
	            trip.averageSpeed = totalDistance /(totalMinutes / 60.0);
	        }
	        
	        //CHECKING FOR UNIQUE TRIPS
	        String uniqueKey =trip.vehicleNo + "_" + trip.routeKey + "_" + trip.tripStartTime;
	        if (!uniqueTrips.contains(uniqueKey)) {
	            uniqueTrips.add(uniqueKey);
	            trips.add(trip);
	        }
	    }
	    return trips;
		}
	
	private double calculateDistance(
	        double lat1,
	        double lon1,
	        double lat2,
	        double lon2
	) {
	    double R = 6371;

	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);

	    double a =
	            Math.sin(dLat / 2) * Math.sin(dLat / 2)
	            +
	            Math.cos(Math.toRadians(lat1))
	            * Math.cos(Math.toRadians(lat2))
	            * Math.sin(dLon / 2)
	            * Math.sin(dLon / 2);

	    double c =
	            2 * Math.atan2(
	                    Math.sqrt(a),
	                    Math.sqrt(1 - a)
	            );

	    return R * c;
	}
		
	}

