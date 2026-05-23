package com.transport.graph;

import com.transport.model.GPSRecord;
import com.transport.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class GPSNodeClusterer {

    private List<GPSNode> nodes = new ArrayList<>();
    private int nodeCounter = 1;

    private static final double CLUSTER_RADIUS_KM = 0.5;

    public GPSNode getOrCreateNode(double lat, double lon,String placeName) {

        if (lat == 0 || lon == 0) {
            return null;
        }

        for (GPSNode node : nodes) {
            double distance = haversine(lat,lon,node.latitude,node.longitude);

            if (distance <= CLUSTER_RADIUS_KM) {
                return node;
            }
        }
        GPSNode newNode = new GPSNode( "NODE_" + nodeCounter++, lat, lon, placeName );

        nodes.add(newNode);

        return newNode;
    }

    public List<GPSNode> getNodes() {
        return nodes;
    }

    private double haversine(double lat1, double lon1, double lat2,double lon2) {

    	double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a =Math.sin(dLat / 2) * Math.sin(dLat / 2)+ Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1 - a));
        return R * c;
    }
}