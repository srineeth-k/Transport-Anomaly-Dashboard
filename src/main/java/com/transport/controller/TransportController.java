package com.transport.controller;

import com.transport.dto.SummaryDTO;
import com.transport.model.Trip;
import com.transport.service.TransportAnalysisService;

import org.springframework.web.bind.annotation.*;

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
    public List<Trip> getTrips() {
        return service.analyzeTrips();
    }

    @GetMapping("/outliers")
    public List<Trip> getOutliers() {
        return service.analyzeTrips()
                .stream()
                .filter(t -> t.routeOutlier)
                .toList();
    }

    @GetMapping("/summary")
    public SummaryDTO getSummary() {
        return service.getSummary();
    }
}