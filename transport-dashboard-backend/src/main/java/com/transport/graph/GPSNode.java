package com.transport.graph;

public class GPSNode {

    public String id;
    public double latitude;
    public double longitude;
    public String placeName;


    public GPSNode(String id, double latitude, double longitude,String placeName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }
}