package com.calculator.calculator;

import com.calculator.data.DataConstants;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Expression formatter
 * Handles thousand separators, scientific notation, etc.
 */
public final class ExpressionFormatter {
    private static final DecimalFormat THOUSAND_FORMAT;
    private static final DecimalFormat SCIENTIFIC_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        THOUSAND_FORMAT = new DecimalFormat("#,##0.##########", symbols);
        SCIENTIFIC_FORMAT = new DecimalFormat("0.############E0", symbols);
    }

    private ExpressionFormatter() {
    }

    /**
     * Format number with thousand separators
     */
    public static String formatWithThousandSeparator(String number) {
        if (number == null || number.isEmpty()) {
            return number;
        }

        try {
            // Remove existing commas
            String cleanNumber = number.replace(",", "");

            // Check if it's a valid number
            if (!isValidNumber(cleanNumber)) {
                return number;
            }

            // Handle negative numbers
            boolean isNegative = cleanNumber.startsWith("-") || cleanNumber.startsWith("−");
            if (isNegative) {
                cleanNumber = cleanNumber.substring(1);
            }

            // Split by decimal point
            String[] parts = cleanNumber.split("\\.");
            String integerPart = parts[0];
            String decimalPart = parts.length > 1 ? parts[1] : null;

            // Format integer part with commas
            StringBuilder formatted = new StringBuilder();
            int count = 0;
            for (int i = integerPart.length() - 1; i >= 0; i--) {
                if (count > 0 && count % 3 == 0) {
                    formatted.insert(0, ",");
                }
                formatted.insert(0, integerPart.charAt(i));
                count++;
            }

            // Add decimal part if exists
            if (decimalPart != null) {
                formatted.append(".").append(decimalPart);
            }

            // Add negative sign back
            if (isNegative) {
                formatted.insert(0, "-");
            }

            return formatted.toString();

        } catch (Exception e) {
            return number;
        }
    }

    /**
     * Format number in scientific notation
     */
    public static String formatScientificNotation(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Error";
        }
        return SCIENTIFIC_FORMAT.format(value);
    }

    /**
     * Format expression for display
     * Handles max placeholders and scientific notation
     */
    public static String formatForDisplay(String input, int maxPlaceholders) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Count placeholders
        int placeholders = InputProcessor.countPlaceholders(input);

        if (placeholders > maxPlaceholders) {
            // Need to convert to scientific notation
            try {
                // Extract the number part if it's a result
                double value = Double.parseDouble(input.replace(",", ""));
                return formatScientificNotation(value);
            } catch (NumberFormatException e) {
                return input;
            }
        }

        return input;
    }

    /**
     * Format result number for display
     */
    public static String formatResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return "Error";
        }

        // Check if we need scientific notation
        String strResult;
        if (Math.abs(result) >= 1e17 || (Math.abs(result) < 1e-10 && result != 0)) {
            strResult = SCIENTIFIC_FORMAT.format(result);
        } else if (result == (long) result) {
            strResult = String.valueOf((long) result);
        } else {
            strResult = String.valueOf(result);
            // Limit decimal places to avoid floating point errors
            if (strResult.length() > 17) {
                strResult = String.format(Locale.US, "%.10f", result);
                // Remove trailing zeros
                strResult = strResult.replaceAll("0+$", "").replaceAll("\\.$", "");
            }
        }

        // Apply thousand separators
        return formatWithThousandSeparator(strResult);
    }

    /**
     * Check if string is a valid number
     */
    private static boolean isValidNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Remove formatting from number string
     */
    public static String unformatNumber(String formatted) {
        if (formatted == null) {
            return "";
        }
        return formatted.replace(",", "");
    }
}
