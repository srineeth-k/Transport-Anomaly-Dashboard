package com.transport.analysis;

import com.transport.graph.*;
import com.transport.model.GPSRecord;
import com.transport.model.Trip;

import java.util.List;

public class GPSShortestPathAnalyzer {

    private RouteGraph graph;
    private GPSRouteGraphBuilder graphBuilder;
    private ShortestPathService shortestPathService;
    

    public GPSShortestPathAnalyzer(RouteGraph graph, GPSRouteGraphBuilder graphBuilder) {
        this.graph = graph;
        this.graphBuilder = graphBuilder;
        this.shortestPathService = new ShortestPathService();
    }

    public void analyze(List<Trip> trips) {

        for (Trip trip : trips) {
            if (trip.gpsSegments == null || trip.gpsSegments.isEmpty()) {
                continue;
            }
            
            trip.suggestedPath = "NO VALID GPS PATH FOUND";
            
            GPSRecord first = trip.gpsSegments.get(0);
            GPSRecord last = trip.gpsSegments.get(trip.gpsSegments.size() - 1);
                      
            
            GPSNode startNode = graphBuilder.findNearestNode(first.startLatitude, first.startLongitude);
            GPSNode endNode = graphBuilder.findNearestNode(last.endLatitude, last.endLongitude);
            
            if (startNode == null || endNode == null) {
                continue;      }
            if (startNode.id.equals(endNode.id)) {
                continue;  }

            double shortestDistance = shortestPathService.findShortestDistance(graph, startNode.id, endNode.id);

            if (shortestDistance == Double.MAX_VALUE) {
                continue;    }
            
            if (shortestDistance <= 0) {
                continue; }
            
            if (shortestDistance < trip.actualGPSDistance * 0.30) {
                continue;
            }

            trip.gpsShortestDistance = shortestDistance;
            trip.gpsExtraDistance = trip.actualGPSDistance - shortestDistance;

            List<String> path = shortestPathService.findShortestPath(graph, startNode.id, endNode.id);

            StringBuilder readablePath = new StringBuilder();

            for (String nodeId : path) {
                GPSNode node = graphBuilder.findNodeById(nodeId);

                if (node != null && node.placeName != null) {
                    readablePath.append(node.placeName);
                } else {
                    readablePath.append(nodeId);
                }
                readablePath.append(" -> ");
            }

            if (readablePath.length() >= 4) {
                readablePath.setLength(
                        readablePath.length() - 4
                );
            }

            trip.suggestedPath = readablePath.toString();

            double upperLimit = shortestDistance * 1.40;
            double lowerLimit = shortestDistance * 0.70;	

            if (trip.actualGPSDistance > upperLimit) {
                trip.shortestPathOutlier = true;
                trip.routeOutlier = true;
            }
        }
    }
}