package com.transport.main;

import com.transport.model.DispatchRecord;
import com.transport.reader.DispatchExcelReader;
import com.transport.model.GPSRecord;
import com.transport.reader.GPSExcelReader;
import com.transport.service.TripMatcher;
import com.transport.model.Trip;
import com.transport.service.RouteAnalyticsService;
import com.transport.analysis.OutlierDetector;
import com.transport.analysis.GPSRouteDeviationDetector;
import com.transport.analysis.ShortestPathOutlierDetector;
import com.transport.graph.RouteGraph;
import com.transport.graph.GPSRouteGraphBuilder;
import com.transport.analysis.GPSShortestPathAnalyzer;

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

        // =========================
        // OUTLIER ANALYTICS
        // =========================

        OutlierDetector detector = new OutlierDetector();
        detector.detectOutliers(trips);

        GPSRouteDeviationDetector gpsDetector = new GPSRouteDeviationDetector();
        gpsDetector.detectGPSDeviation(trips);

        // =========================
        // GPS GRAPH BUILDING + SHORTEST PATH ANALYSIS
        // =========================

        GPSRouteGraphBuilder gpsGraphBuilder = new GPSRouteGraphBuilder();
        RouteGraph gpsGraph = gpsGraphBuilder.buildGraphFromTrips(trips);

        GPSShortestPathAnalyzer gpsShortestAnalyzer = new GPSShortestPathAnalyzer(gpsGraph, gpsGraphBuilder);
        gpsShortestAnalyzer.analyze(trips);

        // =========================
        // FINAL OUTLIERS
        // =========================

        System.out.println("\nOUTLIERS FOUND:");

        for (Trip trip : trips) {
            if (trip.routeOutlier || trip.timeOutlier || trip.gpsOutlier || trip.shortestPathOutlier || trip.gpsDeviationScore > 30) {
                System.out.println(trip);
            }
        }
    }
}