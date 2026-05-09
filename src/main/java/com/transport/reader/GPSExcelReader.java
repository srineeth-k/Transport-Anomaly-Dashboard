package com.transport.reader;

import com.transport.model.GPSRecord;
import com.transport.util.VehicleUtil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.format.DateTimeFormatter;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GPSExcelReader {

    public List<GPSRecord> read(String filePath) {

        List<GPSRecord> records = new ArrayList<>();
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        try {

            FileInputStream fis = new FileInputStream(filePath);

            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet =workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                GPSRecord record = new GPSRecord();

                // =========================
                // VEHICLE NUMBER
                // =========================

                Cell vehicleCell = row.getCell(1);

                if (vehicleCell != null) {

                    String vehicle =
                            vehicleCell.toString();

                    record.vehicleNo =
                            VehicleUtil.normalize(vehicle);
                }

                // =========================
                // START TIME
                // =========================

                Cell startTimeCell = row.getCell(8);

                if (startTimeCell != null) {

                    String startTimeText =
                            startTimeCell.toString().trim();

                    // Validate timestamp format
                    if (startTimeText.matches(
                            "\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}"
                    )) {

                        record.startTime =
                                LocalDateTime.parse(
                                        startTimeText,
                                        formatter
                                );
                    }
                }

                // =========================
                // END TIME
                // =========================

                Cell endTimeCell = row.getCell(9);

                if (endTimeCell != null) {

                    String endTimeText =
                            endTimeCell.toString().trim();

                    if (endTimeText.matches(
                            "\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}"
                    )) {

                        record.endTime =
                                LocalDateTime.parse(
                                        endTimeText,
                                        formatter
                                );
                    }
                }

                // =========================
                // START LOCATION
                // =========================

                Cell startLocationCell =
                        row.getCell(10);

                if (startLocationCell != null) {

                    record.startLocation =
                            startLocationCell.toString();
                }

                // =========================
                // END LOCATION
                // =========================

                Cell endLocationCell =
                        row.getCell(12);

                if (endLocationCell != null) {

                    record.endLocation =
                            endLocationCell.toString();
                }

                // =========================
                // DISTANCE
                // =========================

                Cell distanceCell = row.getCell(14);

                if (distanceCell != null) {

                    String distanceText =
                            distanceCell.toString().trim();

                    if (!distanceText.isEmpty()) {

                        try {

                            record.distanceKm =
                                    Double.parseDouble(distanceText);

                        } catch (Exception e) {

                            record.distanceKm = 0;
                        }
                    }
                }

                // =========================
                // DURATION
                // =========================

                if (record.startTime != null &&
                        record.endTime != null) {

                    long minutes =
                            Duration.between(
                                    record.startTime,
                                    record.endTime
                            ).toMinutes();

                    record.durationMinutes = minutes;
                }

                // =========================
                // START LAT/LON
                // =========================

                Cell startLatLonCell =
                        row.getCell(20);

                if (startLatLonCell != null) {

                    String latLon =
                            startLatLonCell.toString();

                    String[] parts =
                            latLon.split(",");

                    if (parts.length == 2) {

                        record.startLatitude =
                                Double.parseDouble(parts[0].trim());

                        record.startLongitude =
                                Double.parseDouble(parts[1].trim());
                    }
                }

                // =========================
                // END LAT/LON
                // =========================

                Cell endLatLonCell =
                        row.getCell(21);

                if (endLatLonCell != null) {

                    String latLon =
                            endLatLonCell.toString();

                    String[] parts =
                            latLon.split(",");

                    if (parts.length == 2) {

                        record.endLatitude =
                                Double.parseDouble(parts[0].trim());

                        record.endLongitude =
                                Double.parseDouble(parts[1].trim());
                    }
                }

                records.add(record);
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return records;
    }
}