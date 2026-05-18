package com.transport.main;

import com.transport.model.DispatchRecord;

import com.transport.reader.DispatchExcelReader;
import com.transport.model.GPSRecord;
import com.transport.reader.GPSExcelReader;
import com.transport.service.TripMatcher;
import com.transport.model.Trip;
import com.transport.service.RouteAnalyticsService;
import com.transport.analysis.OutlierDetector;
import com.transport.analysis.ShortestPathOutlierDetector;
import com.transport.graph.RouteGraph;
import com.transport.graph.ShortestPathService;
import com.transport.analysis.GPSRouteDeviationDetector;

import java.util.ArrayList;
import java.util.List;

public class MainApp {

    public static void main(String[] args) {

        // =========================
        // READ DISPATCH FILE
        // =========================

        DispatchExcelReader dispatchReader = new DispatchExcelReader();
        String dispatchFile = "C:\\Users\\Srineeth K\\Desktop\\pst project\\pst march.xlsx";
        List<DispatchRecord> dispatchRecords = dispatchReader.read(dispatchFile);

        System.out.println("Total Dispatch Records = " + dispatchRecords.size());

        // =========================
        // READ GPS FILES
        // =========================

        String[] gpsFiles = {
                "C:\\Users\\Srineeth K\\Desktop\\pst project\\gps\\History-Report-AP39U9519.xlsx",
                "C:\\Users\\Srineeth K\\Desktop\\pst project\\gps\\History-Report-AP39U9529.xlsx",
                "C:\\Users\\Srineeth K\\Desktop\\pst project\\gps\\History-Report-AP39U9629.xlsx",
                "C:\\Users\\Srineeth K\\Desktop\\pst project\\gps\\History-Report-AP39U9649.xlsx"
        };

        List<GPSRecord> allGPSRecords = new ArrayList<>();
        GPSExcelReader gpsReader = new GPSExcelReader();

        for (String file : gpsFiles) {
            List<GPSRecord> records = gpsReader.read(file);
            allGPSRecords.addAll(records);
            System.out.println("Loaded GPS File : " + file);
            System.out.println("Records Added : " + records.size());
        }

        System.out.println("Total GPS Records = " + allGPSRecords.size());

        // =========================
        // BUILD TRIPS
        // =========================

        TripMatcher matcher = new TripMatcher();
        List<Trip> trips = matcher.buildTrips(dispatchRecords, allGPSRecords);

        // =========================
        // PRINT TRIPS
        // =========================

        System.out.println("\nTOTAL TRIPS = " + trips.size());
        for (Trip trip : trips) {
            System.out.println(trip);
        }

        // =========================
        // ROUTE ANALYTICS
        // =========================

        RouteAnalyticsService analytics = new RouteAnalyticsService();
        analytics.analyzeRoutes(trips);
    	
    	RouteGraph graph =
    	        new RouteGraph();

    	// SAMPLE ROUTES

    	graph.addEdge(
    	        "BAYYAVARAM",
    	        "VISAKHAPATNAM",
    	        25.0
    	);

    	graph.addEdge(
    	        "VISAKHAPATNAM",
    	        "PENDURTHI",
    	        20.0
    	);

    	graph.addEdge(
    	        "VISAKHAPATNAM",
    	        "MADHURAVADA (U)",
    	        15.0
    	);

    	graph.addEdge(
    	        "VISAKHAPATNAM",
    	        "DEVARAPALLE",
    	        75.0
    	);

    	graph.addEdge(
    	        "DEVARAPALLE",
    	        "BHANJANAGAR",
    	        140.0
    	);

    	graph.addEdge(
    	        "BHANJANAGAR",
    	        "BALUGAON",
    	        60.0
    	);

    	graph.addEdge(
    	        "BALUGAON",
    	        "CHHATRAPUR",
    	        30.0
    	);

    	graph.addEdge(
    	        "CHHATRAPUR",
    	        "BERHAMPUR",
    	        20.0
    	);

    	graph.addEdge(
    	        "BERHAMPUR",
    	        "ICHAPURAM",
    	        55.0
    	);

    	graph.addEdge(
    	        "ICHAPURAM",
    	        "PALAKONDA",
    	        95.0
    	);

    	graph.addEdge(
    	        "PALAKONDA",
    	        "RAYAGADA",
    	        120.0
    	);
        
        // =========================
        // OUTLIER ANALYTICS
        // =========================
        OutlierDetector detector = new OutlierDetector();
        detector.detectOutliers(trips);
        
        GPSRouteDeviationDetector gpsDetector =  new GPSRouteDeviationDetector();
        gpsDetector.detectGPSDeviation(trips);
    	
    	ShortestPathOutlierDetector
    	shortestDetector =
    	new ShortestPathOutlierDetector(
    	        graph
    	);

    	shortestDetector
    	.detectShortestPathOutliers(
    	        trips
    	);
        
        System.out.println("\nOUTLIERS FOUND:");
        for (Trip trip : trips) {
            if (trip.routeOutlier || trip.timeOutlier || trip.gpsOutlier) {
                System.out.println(trip);
            }
        }

        
        
    }
}