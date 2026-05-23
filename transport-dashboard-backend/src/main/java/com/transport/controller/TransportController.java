package com.transport.controller;

import com.transport.dto.SummaryDTO;
import com.transport.dto.TripResponseDTO;
import com.transport.service.TransportAnalysisService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransportController {

    private final TransportAnalysisService service;

    public TransportController(TransportAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/trips")
    public List<TripResponseDTO> getTrips() {
        return service.analyzeTripDTOs();
    }

    @GetMapping("/outliers")
    public List<TripResponseDTO> getOutliers() {
        return service.analyzeTripDTOs()
                .stream()
                .filter(t -> t.routeOutlier)
                .toList();
    }

    @GetMapping("/trip/{index}")
    public TripResponseDTO getTripByIndex(
            @PathVariable int index) {

        List<TripResponseDTO> outliers = service.analyzeTripDTOs()
                .stream()
                .filter(t -> t.routeOutlier)
                .toList();

        if (index < 0 || index >= outliers.size()) {
            return null;
        }

        return outliers.get(index);
    }

    @GetMapping("/summary")
    public SummaryDTO getSummary() {
        return service.getSummary();
    }

    @GetMapping("/outliers/export")
    public ResponseEntity<String> exportOutliers() {

        List<TripResponseDTO> outliers = service.analyzeTripDTOs()
                .stream()
                .filter(t -> t.routeOutlier)
                .toList();

        StringBuilder csv = new StringBuilder();

        csv.append(
                "Vehicle,Route,DispatchTime,CompletionTime,RiskLevel,RiskScore,ExtraKM,GPSDeviation,SuggestedPath,Reasons\n");

        for (TripResponseDTO trip : outliers) {

            csv.append(safe(trip.vehicleNo)).append(",");
            csv.append(quote(trip.routeKey)).append(",");
            csv.append(safe(trip.tripStartTime)).append(",");
            csv.append(safe(trip.tripEndTime)).append(",");
            csv.append(safe(trip.riskLevel)).append(",");
            csv.append(trip.riskScore).append(",");
            csv.append(trip.gpsExtraDistance).append(",");
            csv.append(trip.gpsDeviationScore).append(",");
            csv.append(quote(trip.suggestedPath)).append(",");
            csv.append(quote(String.join(" | ", trip.anomalyReasons))).append("\n");
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=outlier_report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.toString());
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ");
    }

    private String quote(String value) {
        if (value == null) {
            return "\"\"";
        }
        value = value.replace("\"", "\"\"");
        return "\"" + value + "\"";
    }
}