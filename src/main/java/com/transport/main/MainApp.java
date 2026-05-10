package com.transport.main;

import com.transport.model.DispatchRecord;
import com.transport.reader.DispatchExcelReader;
import com.transport.model.GPSRecord;
import com.transport.reader.GPSExcelReader;
import com.transport.service.TripMatcher;
import com.transport.model.Trip;

import java.util.List;

public class MainApp {

	public static void main(String[] args) {

	    // =========================
	    // READ DISPATCH FILE
	    // =========================

	    DispatchExcelReader dispatchReader = new DispatchExcelReader();

	    String dispatchFile = "C:\\Users\\Srineeth K\\Desktop\\pst project\\pst march.xlsx";

	    List<DispatchRecord> dispatchRecords = dispatchReader.read(dispatchFile);

	    System.out.println(
	            "Total Dispatch Records = "
	                    + dispatchRecords.size()
	    );

	    // =========================
	    // READ GPS FILE
	    // =========================

	    GPSExcelReader gpsReader =
	            new GPSExcelReader();

	    String gpsFile =
	            "C:\\Users\\Srineeth K\\Desktop\\pst project\\History-Report-AP39U9519-01-Mar-2026-12-00-AM-to-31-Mar-2026-03-21-PM.xlsx";

	    List<GPSRecord> gpsRecords =
	            gpsReader.read(gpsFile);

	    System.out.println(
	            "Total GPS Records = "
	                    + gpsRecords.size()
	    );

	    // =========================
	    // BUILD TRIPS
	    // =========================

	    TripMatcher matcher =
	            new TripMatcher();

	    List<Trip> trips =
	            matcher.buildTrips(
	                    dispatchRecords,
	                    gpsRecords
	            );

	    // =========================
	    // PRINT TRIPS
	    // =========================

	    System.out.println(
	            "\nTOTAL TRIPS = "
	                    + trips.size()
	    );

	    for (Trip trip : trips) {

	        System.out.println(trip);
	    }
	}
}