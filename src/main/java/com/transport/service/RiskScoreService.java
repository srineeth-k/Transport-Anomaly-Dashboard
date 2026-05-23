package com.transport.service;

import com.transport.model.Trip;

import java.util.*;

public class RiskScoreService {

    public void calculateRiskScores(List<Trip> trips) {

        Map<String, List<Trip>> routeMap = new HashMap<>();

        for (Trip trip : trips) {
            routeMap.computeIfAbsent(trip.routeKey, k -> new ArrayList<>()).add(trip);
        }

        for (String route : routeMap.keySet()) {
            List<Trip> routeTrips = routeMap.get(route);
            double avgDuration = 0;

            for (Trip trip : routeTrips) {
                avgDuration += trip.totalDurationMinutes;
            }
            avgDuration = avgDuration / routeTrips.size();

            for (Trip trip : routeTrips) {
                
            	if(!trip.shortestPathOutlier && !trip.gpsPatternOutlier && !trip.timeOutlier){
            			    trip.riskScore = 0;
            			    trip.riskLevel = "NORMAL";
            			    continue;
            			}
            	
            	double shortestDeviationPercent = 0;
                if (trip.actualGPSDistance > 0 && trip.gpsExtraDistance > 0) {
                    shortestDeviationPercent = (trip.gpsExtraDistance / trip.actualGPSDistance) * 100;
                }

                double gpsDeviationPercent = trip.gpsDeviationScore;

                double timeDeviationPercent = 0;

                if (avgDuration > 0) {
                    timeDeviationPercent = Math.abs(trip.totalDurationMinutes- avgDuration) / avgDuration * 100;
                }

                double risk = 0.5 * shortestDeviationPercent + 0.3*gpsDeviationPercent+ 0.2 * timeDeviationPercent;
                trip.riskScore = risk;

                if (risk >= 60) {
                    trip.riskLevel = "HIGH";
                } else if (risk >= 30) {
                    trip.riskLevel = "MEDIUM";
                } else {
                    trip.riskLevel = "LOW";
                }
            }
        }
    }
}