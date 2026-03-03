package com.calculator.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Number formatting utility
 */
public final class NumberFormatter {
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#,##0.##########", SYMBOLS);
    private static final DecimalFormat SCIENTIFIC_FORMAT = new DecimalFormat("0.############E0", SYMBOLS);

    private NumberFormatter() {
    }

    /**
     * Format number with thousand separators
     */
    public static String formatWithThousandSeparator(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Error";
        }
        return THOUSAND_FORMAT.format(value);
    }

    /**
     * Format number in scientific notation
     */
    public static String formatScientific(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Error";
        }
        return SCIENTIFIC_FORMAT.format(value);
    }

    /**
     * Parse formatted number string to double
     */
    public static double parseFormatted(String formatted) {
        if (formatted == null || formatted.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(formatted.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Check if number should be displayed in scientific notation
     */
    public static boolean shouldUseScientificNotation(double value) {
        if (value == 0) {
            return false;
        }
        return Math.abs(value) >= 1e17 || Math.abs(value) < 1e-10;
    }
}
