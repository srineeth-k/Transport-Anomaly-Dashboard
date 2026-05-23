package com.transport.service;

import java.util.List;

import com.transport.model.Trip;

public class AnomalyReasonService {

    public void generateReasons(List<Trip> trips){

        for(Trip trip : trips){

            if (trip.shortestPathOutlier) {
                trip.anomalyReasons.add(
                    "Vehicle traveled "
                    + String.format("%.2f", trip.gpsExtraDistance)
                    + " km more than the learned shortest GPS path."
                );
            }

            if (trip.gpsPatternOutlier) {
                trip.anomalyReasons.add(
                    "GPS movement pattern deviated from normal route behavior by "
                    + String.format("%.2f", trip.gpsDeviationScore)
                    + "%."
                );
            }

            if (trip.timeOutlier) {
                trip.anomalyReasons.add(
                    "Trip duration was higher than the normal duration for this route."
                );
            }

        }
    }
}