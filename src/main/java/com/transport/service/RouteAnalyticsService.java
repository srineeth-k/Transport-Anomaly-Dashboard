package com.transport.service;

import com.transport.model.Trip;
import java.util.*;

public class RouteAnalyticsService {

    public void analyzeRoutes(List<Trip> trips) {

        // MAP TO STORE ROUTE -> LIST OF TRIPS
        Map<String, List<Trip>> routeMap = new HashMap<>();

        // GROUP ALL TRIPS BASED ON ROUTE
        for (Trip trip : trips) {
            if (!routeMap.containsKey(trip.routeKey)) {
                routeMap.put(trip.routeKey, new ArrayList<>());
            }
            routeMap.get(trip.routeKey).add(trip);
        }

        // ANALYZE EACH ROUTE
        for (String route : routeMap.keySet()) {

            // GET ALL TRIPS OF CURRENT ROUTE
            List<Trip> routeTrips = routeMap.get(route);
            double totalDistance = 0;
            long totalDuration = 0;

            // CALCULATE TOTAL DISTANCE AND DURATION
            for (Trip trip : routeTrips) {
                totalDistance += trip.totalDistance;
                totalDuration += trip.totalDurationMinutes;
            }
            double avgDistance = totalDistance / routeTrips.size();
            double avgDuration = (double) totalDuration / routeTrips.size();

            System.out.println("\n===================");
            System.out.println("ROUTE : " + route);
            System.out.println("TOTAL TRIPS : " + routeTrips.size());
            System.out.println("AVG DISTANCE : " + avgDistance);
            System.out.println("AVG DURATION : " + avgDuration + " mins");
        }
    }
}