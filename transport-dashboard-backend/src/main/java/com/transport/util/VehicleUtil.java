package com.transport.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VehicleUtil {

    public static String normalize(String vehicle) {

        if (vehicle == null)
            return "";

        // Remove spaces and special characters
        vehicle = vehicle
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();

        // Regex to capture actual vehicle number
        Pattern pattern =
                Pattern.compile("[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}");

        Matcher matcher = pattern.matcher(vehicle);

        if (matcher.find()) {
            return matcher.group();
        }

        return vehicle;
    }
}