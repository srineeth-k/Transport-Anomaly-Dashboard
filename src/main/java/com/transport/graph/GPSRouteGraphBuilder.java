package com.transport.graph;

import com.transport.model.GPSRecord;
import com.transport.model.Trip;

import java.util.List;

public class GPSRouteGraphBuilder {

    private GPSNodeClusterer clusterer = new GPSNodeClusterer();

    public RouteGraph buildGraphFromTrips(List<Trip> trips) {

        RouteGraph graph = new RouteGraph();

        for (Trip trip : trips) {
            if (trip.gpsSegments == null || trip.gpsSegments.isEmpty()) {
                continue;
            }

            for (GPSRecord gps : trip.gpsSegments) {
                GPSNode startNode =  clusterer.getOrCreateNode(gps.startLatitude, gps.startLongitude,
                                gps.startLocation );

                GPSNode endNode = clusterer.getOrCreateNode( gps.endLatitude, gps.endLongitude,
                                gps.endLocation );

                if (startNode == null || endNode == null) {
                    continue;
                }

                if (startNode.id.equals(endNode.id)) {
                    continue;
                }

                double distance = haversine(startNode.latitude,startNode.longitude, endNode.latitude,
                                endNode.longitude  );

                graph.addEdge(startNode.id,endNode.id, distance );
            }
        }

        System.out.println("GPS NODES CREATED = " + clusterer.getNodes().size());

        return graph;
    }

    public GPSNode findNearestNode(double lat, double lon) {

        GPSNode nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (GPSNode node : clusterer.getNodes()) {
            double distance = haversine(lat, lon, node.latitude, node.longitude);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = node;
            }
        }

        return nearest;
    }
    
    public GPSNode findNodeById(String nodeId) {

        for (GPSNode node :
                clusterer.getNodes()) {

            if (node.id.equals(nodeId)) {
                return node;
            }
        }

        return null;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}