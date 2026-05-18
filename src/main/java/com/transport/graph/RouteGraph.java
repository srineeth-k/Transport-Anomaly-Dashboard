package com.transport.graph;

import java.util.HashMap;
import java.util.Map;

public class RouteGraph {
	private Map<String, Map<String,Double>> graph=new HashMap<>();
	
	public void addEdge(String source, String destination , Double distance) {
		graph.putIfAbsent(source,new HashMap<>());
		graph.get(source).put(destination, distance);
		
		// UNDIRECTED GRAPH
        graph.putIfAbsent(destination,new HashMap<>());
        graph.get(destination).put(source,distance);
	}
	
	public Map<String, Map<String, Double>> getGraph() {
        return graph;
    }
}
