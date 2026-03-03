package com.calculator.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date and time utility
 */
public final class DateTimeUtils {
    private static final SimpleDateFormat ISO_FORMAT;
    private static final SimpleDateFormat DISPLAY_FORMAT;

    static {
        ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);
        ISO_FORMAT.setTimeZone(TimeZone.getDefault());

        DISPLAY_FORMAT = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
    }

    private DateTimeUtils() {
    }

    /**
     * Get current time in ISO 8601 format
     */
    public static String nowISO() {
        return ISO_FORMAT.format(new Date());
    }

    /**
     * Get current time in display format
     */
    public static String nowDisplay() {
        return DISPLAY_FORMAT.format(new Date());
    }

    /**
     * Format ISO 8601 string to display format
     */
    public static String formatForDisplay(String isoString) {
        try {
            Date date = ISO_FORMAT.parse(isoString);
            if (date == null) {
                return "";
            }
            return DISPLAY_FORMAT.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Parse ISO 8601 string to Date
     */
    public static Date parseISO(String isoString) {
        try {
            return ISO_FORMAT.parse(isoString);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get current timestamp in milliseconds
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
