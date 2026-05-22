package com.transport.analysis;

import java.util.List;

import com.transport.graph.RouteGraph;
import com.transport.graph.ShortestPathService;
import com.transport.model.Trip;

public class ShortestPathOutlierDetector {

    private RouteGraph routeGraph;
    private ShortestPathService pathService;

    public ShortestPathOutlierDetector(RouteGraph routeGraph) {
        this.routeGraph = routeGraph;
        this.pathService = new ShortestPathService();
    }

    public void detectShortestPathOutliers(List<Trip> trips) {

        for (Trip trip : trips) {
            String[] routeParts = trip.routeKey.split("->");
            if (routeParts.length < 2) continue;
            String source = routeParts[0].trim();
            String destination = routeParts[1].trim();
            double expectedDistance = pathService.findShortestDistance(routeGraph, source, destination);
            trip.expectedDistance =  expectedDistance;
            // IF ROUTE NOT FOUND
            if (expectedDistance == Double.MAX_VALUE) continue;

            double actualDistance = trip.totalDistance;
            double allowedDistance = expectedDistance * 1.30;

            System.out.println("\nTRIP = " + trip.routeKey);
            System.out.println("EXPECTED DISTANCE = " + expectedDistance);
            System.out.println("ACTUAL DISTANCE = " + actualDistance);

            // OUTLIER LOGIC
            if (actualDistance > allowedDistance) {
                trip.routeOutlier = true;
                System.out.println("SHORTEST PATH OUTLIER FOUND");
            }
        }
    }
}