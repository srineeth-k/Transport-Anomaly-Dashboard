package com.transport.main;

import com.transport.model.DispatchRecord;
import com.transport.reader.DispatchExcelReader;
import com.transport.model.GPSRecord;
import com.transport.reader.GPSExcelReader;

import java.util.List;

public class MainApp {

    public static void main(String[] args) {

//        DispatchExcelReader reader =
//                new DispatchExcelReader();
//
//        String filePath =
//                "C:\\Users\\Srineeth K\\Desktop\\pst project\\pst march.xlsx";
//
//        List<DispatchRecord> records =
//                reader.read(filePath);
//
//        System.out.println(
//                "Total Dispatch Records: "
//                        + records.size());
//
//        for (int i = 0; i < 5; i++) {
//
//            DispatchRecord r = records.get(i);
//
//            System.out.println("----------------");
//
//            System.out.println(r.vehicleNo);
//            System.out.println(r.dispatchDate);
//            System.out.println(r.fromName);
//            System.out.println(r.toName);
//            System.out.println(r.onwardReturn);
//        }
    	GPSExcelReader reader =
                new GPSExcelReader();

        String filePath =
                "C:\\Users\\Srineeth K\\Desktop\\pst project\\History-Report-AP39U9519-01-Mar-2026-12-00-AM-to-31-Mar-2026-03-21-PM.xlsx";

        List<GPSRecord> records =
                reader.read(filePath);

        System.out.println(
                "Total GPS Records: "
                        + records.size());

        for (int i = 0; i < 5; i++) {
        	
            System.out.println(records.get(i));
        }
    }
}