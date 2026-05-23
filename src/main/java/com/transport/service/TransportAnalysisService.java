package com.transport.service;

import org.springframework.stereotype.Service;

import com.transport.analysis.GPSRouteDeviationDetector;
import com.transport.analysis.GPSShortestPathAnalyzer;
import com.transport.analysis.OutlierDetector;
import com.transport.dto.SummaryDTO;
import com.transport.graph.GPSRouteGraphBuilder;
import com.transport.graph.RouteGraph;
import com.transport.model.DispatchRecord;
import com.transport.model.GPSRecord;
import com.transport.model.Trip;
import com.transport.reader.DispatchExcelReader;
import com.transport.reader.GPSExcelReader;

import java.util.*;

@Service
public class TransportAnalysisService {

    public List<Trip> analyzeTrips() {

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
        AnomalyReasonService anomalyService = new AnomalyReasonService();
        anomalyService.generateReasons(trips);

        RiskScoreService riskService = new RiskScoreService();
        riskService.calculateRiskScores(trips);

        trips.sort((a, b) -> Double.compare(b.riskScore, a.riskScore));

        ReportExportService reportService = new ReportExportService();

        reportService.exportToCSV(trips, "C:\\Users\\Srineeth K\\Desktop\\pst project\\trip_report.csv");

        System.out.println("\nOUTLIERS FOUND:");

        for (Trip trip : trips) {
            trip.routeOutlier = trip.shortestPathOutlier || trip.gpsPatternOutlier || trip.timeOutlier;
            if (trip.routeOutlier) {
                System.out.println(trip);
            }
        }
        return trips;
    }

    public SummaryDTO getSummary() {

        List<Trip> trips = analyzeTrips();
        SummaryDTO summary = new SummaryDTO();
        summary.totalTrips = trips.size();

        for (Trip trip : trips) {
            if (trip.routeOutlier) {
                summary.outlierTrips++;
            }

            if ("HIGH".equalsIgnoreCase(trip.riskLevel)) {
                summary.highRiskTrips++;

            } else if ("MEDIUM".equalsIgnoreCase(trip.riskLevel)) {
                summary.mediumRiskTrips++;
            } else if ("LOW".equalsIgnoreCase(trip.riskLevel)) {
                summary.lowRiskTrips++;

            }
        }

        return summary;
    }

}