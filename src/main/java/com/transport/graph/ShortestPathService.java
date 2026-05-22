package com.transport.graph;

import java.util.*;

public class ShortestPathService {

    // SHORTEST DISTANCE ONLY
    public double findShortestDistance(RouteGraph routeGraph, String source, String destination) {

        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));
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

            for (String next : neighbors.keySet()) {
                double newDistance = current.distance + neighbors.get(next);
                if (newDistance < distances.get(next)) {
                    distances.put(next, newDistance);
                    pq.add(new Node(next, newDistance));
                }
            }
        }

        return Double.MAX_VALUE;
    }

    // 2. SHORTEST PATH (NEW)

    public List<String> findShortestPath(RouteGraph routeGraph, String source, String destination) {

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));

        for (String city : routeGraph.getGraph().keySet()) {
            distances.put(city, Double.MAX_VALUE);
        }

        distances.put(source, 0.0);
        pq.add(new Node(source, 0.0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.city.equals(destination)) {
                break;
            }
            Map<String, Double> neighbors = routeGraph.getGraph().get(current.city);
            if (neighbors == null) continue;

            for (String next : neighbors.keySet()) {
                double newDistance = current.distance + neighbors.get(next);
                if (newDistance < distances.get(next)) {
                    distances.put(next, newDistance);
                    previous.put(next, current.city);
                    pq.add(new Node(next, newDistance));
                }
            }
        }

        List<String> path = new ArrayList<>();
        String current = destination;
        if (!previous.containsKey(destination) && !source.equals(destination)) {
            return new ArrayList<>();
        }

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        if (path.isEmpty() || !path.get(0).equals(source)) {
            return new ArrayList<>();
        }

        return path;
    }

    // NODE CLASS
    static class Node {
        String city;
        double distance;

        Node(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }
    }
}