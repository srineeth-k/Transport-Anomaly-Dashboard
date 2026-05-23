package com.transport.reader;

import com.transport.model.DispatchRecord;
import com.transport.util.VehicleUtil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DispatchExcelReader {

    // ==========================================
    // OLD SUPPORT
    // FOR LOCAL FILE PATHS
    // ==========================================
    public List<DispatchRecord> read(String filePath) {
        try {
            // STEP 1 — Open file
            FileInputStream fis = new FileInputStream(filePath);
            // STEP 2 — Reuse InputStream reader
            return read(fis);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ==========================================
    // NEW SUPPORT
    // FOR DEPLOYMENT / RESOURCE FILES
    // ==========================================

    public List<DispatchRecord> read(InputStream inputStream) {

        List<DispatchRecord> records = new ArrayList<>();

        try {
            // STEP 1 — Open workbook
            Workbook workbook = new XSSFWorkbook(inputStream);
            // STEP 2 — Get first sheet
            Sheet sheet = workbook.getSheetAt(0);
            // STEP 3 — Loop rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                // Skip empty rows
                if (row == null)
                    continue;
                DispatchRecord record = new DispatchRecord();

                // ==========================
                // READ DATE
                // ==========================

                Cell dateCell = row.getCell(0);
                if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC) {
                    Date date = dateCell.getDateCellValue();
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    record.dispatchDate = localDate;
                }

                // ==========================
                // VEHICLE NUMBER
                // ==========================

                Cell vehicleCell = row.getCell(1);
                if (vehicleCell != null) {
                    String vehicle = vehicleCell.getStringCellValue();
                    record.vehicleNo = VehicleUtil.normalize(vehicle);
                }

                // ==========================
                // ORDER NUMBER
                // ==========================
                Cell orderCell = row.getCell(3);
                if (orderCell != null) {
                    record.orderNo = orderCell.toString();
                }

                // ==========================
                // PRODUCT NAME
                // ==========================
                Cell productCell = row.getCell(4);
                if (productCell != null) {
                    record.productName = productCell.toString();
                }

                // ==========================
                // FROM LOCATION
                // ==========================
                Cell fromCell = row.getCell(6);
                if (fromCell != null) {
                    record.fromName = fromCell.toString();
                }

                // ==========================
                // TO LOCATION
                // ==========================
                Cell toCell = row.getCell(7);
                if (toCell != null) {
                    record.toName = toCell.toString();
                }

                // ==========================
                // ONWARD / RETURN
                // ==========================
                Cell onwardCell = row.getCell(9);
                if (onwardCell != null) {
                    record.onwardReturn = onwardCell.toString();
                }

                // ADD RECORD
                records.add(record);

            }

            workbook.close();
            inputStream.close();

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return records;

    }

}