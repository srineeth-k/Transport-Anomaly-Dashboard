package com.transport.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ShortestPathService {

    public double findShortestDistance(RouteGraph routeGraph, String source, String destination) {

        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));

        // INITIALIZE DISTANCES
        for (String city : routeGraph.getGraph().keySet()) {
            distances.put(city, Double.MAX_VALUE);
        }

        distances.put(source, 0.0);
        pq.add(new Node(source, 0.0));
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.city.equals(destination)) {
                return current.distance;
            }

            Map<String, Double> neighbors = routeGraph.getGraph().get(current.city);
            if (neighbors == null) continue;
            
            for (String nextCity : neighbors.keySet()) {
                double newDistance = current.distance + neighbors.get(nextCity);
                if (newDistance < distances.get(nextCity)) {
                    distances.put(nextCity, newDistance);
                    pq.add(new Node(nextCity, newDistance));
                }
            }
        }

        return Double.MAX_VALUE;
    }

    static class Node {
        String city;
        double distance;
        Node(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }
    }
}