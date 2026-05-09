package com.transport.reader;

import com.transport.model.DispatchRecord;
import com.transport.util.VehicleUtil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DispatchExcelReader {

    public List<DispatchRecord> read(String filePath) {

        List<DispatchRecord> records = new ArrayList<>();

        try {

            // STEP 1 — Open file
            FileInputStream fis = new FileInputStream(filePath);

            // STEP 2 — Open workbook
            Workbook workbook = new XSSFWorkbook(fis);

            // STEP 3 — Get first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // STEP 4 — Loop through rows
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

                if (dateCell != null &&
                        dateCell.getCellType() == CellType.NUMERIC) {

                    Date date = dateCell.getDateCellValue();

                    LocalDate localDate = date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    record.dispatchDate = localDate;
                }

                // ==========================
                // VEHICLE NUMBER
                // ==========================

                Cell vehicleCell = row.getCell(1);

                if (vehicleCell != null) {

                    String vehicle = vehicleCell.getStringCellValue();

                    record.vehicleNo =
                            VehicleUtil.normalize(vehicle);
                }

                // ==========================
                // ORDER NUMBER
                // ==========================

                Cell orderCell = row.getCell(3);

                if (orderCell != null) {
                    record.orderNo =
                            orderCell.toString();
                }

                // ==========================
                // PRODUCT NAME
                // ==========================

                Cell productCell = row.getCell(4);

                if (productCell != null) {
                    record.productName =
                            productCell.toString();
                }

                // ==========================
                // FROM LOCATION
                // ==========================

                Cell fromCell = row.getCell(6);

                if (fromCell != null) {
                    record.fromName =
                            fromCell.toString();
                }

                // ==========================
                // TO LOCATION
                // ==========================

                Cell toCell = row.getCell(7);

                if (toCell != null) {
                    record.toName =
                            toCell.toString();
                }

                // ==========================
                // ONWARD / RETURN
                // ==========================

                Cell onwardCell = row.getCell(9);

                if (onwardCell != null) {
                    record.onwardReturn =
                            onwardCell.toString();
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