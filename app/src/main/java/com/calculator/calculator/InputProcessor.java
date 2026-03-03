package com.calculator.calculator;

import com.calculator.data.DataConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Input processor
 * Handles operator input, decimal points, percent, etc.
 */
public final class InputProcessor {
    private InputProcessor() {
    }

    /**
     * Process operator input - handle consecutive operators
     */
    public static String processOperatorInput(String current, char operator) {
        if (current == null || current.isEmpty()) {
            // Can start with minus for negative numbers
            if (operator == '−' || operator == '-') {
                return String.valueOf(operator);
            }
            return "";
        }

        char lastChar = current.charAt(current.length() - 1);

        // If last char is an operator, replace it
        if (isOperator(lastChar)) {
            // But if we're adding minus after an operator, keep both (for negative numbers)
            if ((operator == '−' || operator == '-') && lastChar != '−' && lastChar != '-') {
                return current + operator;
            }
            return current.substring(0, current.length() - 1) + operator;
        }

        // If last char is left parenthesis, can add minus
        if (lastChar == '(' && (operator == '−' || operator == '-')) {
            return current + operator;
        }

        return current + operator;
    }

    /**
     * Check if a decimal point can be added at cursor position
     */
    public static boolean canAddDecimalPoint(String current, int cursorPos) {
        if (current == null || current.isEmpty()) {
            return true;
        }

        // Find the number that cursor is in
        int start = findNumberStart(current, cursorPos);
        int end = findNumberEnd(current, cursorPos);

        String currentNumber = current.substring(start, Math.min(end, current.length()));

        // Check if decimal point already exists in current number
        return !currentNumber.contains(".");
    }

    /**
     * Handle percent operation
     */
    public static String handlePercent(String current) {
        if (current == null || current.isEmpty()) {
            return current;
        }

        // Find the last number
        int start = findNumberStart(current, current.length());
        String numberStr = current.substring(start);

        try {
            double number = Double.parseDouble(numberStr.replace(",", ""));
            double result = number / 100.0;
            return current.substring(0, start) + formatResult(result);
        } catch (NumberFormatException e) {
            return current;
        }
    }

    /**
     * Get max input length considering current content
     */
    public static int getMaxInputLength(String current) {
        return DataConstants.MAX_PLACEHOLDERS;
    }

    /**
     * Count placeholders in expression
     */
    public static int countPlaceholders(String expression) {
        if (expression == null || expression.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) || c == '.' || c == '-' ||
                    c == '+' || c == '×' || c == '÷' || c == '−') {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if can add more placeholders
     */
    public static boolean canAddPlaceholder(String current) {
        return countPlaceholders(current) < DataConstants.MAX_PLACEHOLDERS;
    }

    /**
     * Check if character is an operator
     */
    public static boolean isOperator(char c) {
        return c == '+' || c == '−' || c == '-' || c == '×' || c == '*' || c == '÷' || c == '/';
    }

    /**
     * Find the start of the number at cursor position
     */
    public static int findNumberStart(String expression, int cursorPos) {
        if (expression == null || expression.isEmpty()) {
            return 0;
        }

        int pos = Math.min(cursorPos, expression.length()) - 1;

        while (pos >= 0) {
            char c = expression.charAt(pos);
            if (Character.isDigit(c) || c == '.' || c == ',') {
                pos--;
            } else if ((c == '-' || c == '−') &&
                    (pos == 0 || isOperator(expression.charAt(pos - 1)) || expression.charAt(pos - 1) == '(')) {
                // This is a negative sign at start of number
                pos--;
            } else {
                break;
            }
        }

        return pos + 1;
    }

    /**
     * Find the end of the number at cursor position
     */
    public static int findNumberEnd(String expression, int cursorPos) {
        if (expression == null || expression.isEmpty()) {
            return 0;
        }

        int pos = Math.min(cursorPos, expression.length());

        while (pos < expression.length()) {
            char c = expression.charAt(pos);
            if (Character.isDigit(c) || c == '.' || c == ',') {
                pos++;
            } else {
                break;
            }
        }

        return pos;
    }

    /**
     * Format result for display
     */
    private static String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }
}
