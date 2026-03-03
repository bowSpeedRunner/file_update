package com.calculator.calculator;

/**
 * Negation handler
 * Handles toggling negative/positive for numbers
 */
public final class NegationHandler {
    private NegationHandler() {
    }

    /**
     * Toggle negation for the number at cursor position
     */
    public static String toggleNegation(String expression, int cursorPosition) {
        if (expression == null || expression.isEmpty()) {
            return expression;
        }

        // Find the last number's start position
        int start = findLastNumberStart(expression, cursorPosition);
        if (start == -1) {
            return expression;
        }

        // Find the number's end
        int end = findNumberEnd(expression, start);

        // Check if the number is already wrapped with (-)
        if (isCurrentlyNegated(expression, start)) {
            // Remove the (-) wrapper
            // Pattern: (-X) -> X
            int wrapperStart = start - 2;  // Position of '('
            int wrapperEnd = end + 1;       // Position of ')'

            if (wrapperStart >= 0 && wrapperEnd <= expression.length() &&
                    expression.charAt(wrapperStart) == '(' &&
                    expression.charAt(wrapperStart + 1) == '-' &&
                    expression.charAt(wrapperEnd - 1) == ')') {

                // Extract just the number part
                String numberPart = expression.substring(start, end);
                return expression.substring(0, wrapperStart) + numberPart + expression.substring(wrapperEnd);
            }
        } else {
            // Check if there's already a minus sign at start
            char firstChar = expression.charAt(start);
            if (firstChar == '-' || firstChar == '−') {
                // Remove the minus sign
                return expression.substring(0, start) + expression.substring(start + 1);
            } else {
                // Add (-) wrapper
                String numberPart = expression.substring(start, end);
                return expression.substring(0, start) + "(-" + numberPart + ")" + expression.substring(end);
            }
        }

        return expression;
    }

    /**
     * Find the start position of the last number before cursor
     */
    public static int findLastNumberStart(String expression, int cursorPosition) {
        if (expression == null || expression.isEmpty()) {
            return -1;
        }

        int pos = Math.min(cursorPosition, expression.length()) - 1;

        // Skip if cursor is at beginning
        if (pos < 0) {
            return -1;
        }

        // Check if we're inside parentheses for negative number
        // Look for pattern (-N)
        int parenDepth = 0;
        int tempPos = pos;

        // First, move to the end of current number if we're in middle of it
        while (tempPos >= 0 && (Character.isDigit(expression.charAt(tempPos)) ||
                expression.charAt(tempPos) == '.' || expression.charAt(tempPos) == ',')) {
            tempPos--;
        }
        int numberEnd = tempPos + 1;

        // Check if this is a negated number in parentheses
        if (numberEnd > 2 && expression.charAt(numberEnd - 1) == ')') {
            // Look for matching (-
            int searchPos = numberEnd - 2;
            parenDepth = 1;
            while (searchPos >= 0 && parenDepth > 0) {
                char c = expression.charAt(searchPos);
                if (c == ')') parenDepth++;
                else if (c == '(') parenDepth--;
                searchPos--;
            }

            if (searchPos >= 1 && parenDepth == 0 &&
                    expression.charAt(searchPos + 1) == '(' &&
                    (expression.charAt(searchPos + 2) == '-' || expression.charAt(searchPos + 2) == '−')) {
                return searchPos + 1;  // Return position of '('
            }
        }

        // Find the actual start of the number
        pos = numberEnd - 1;
        while (pos >= 0) {
            char c = expression.charAt(pos);
            if (Character.isDigit(c) || c == '.' || c == ',') {
                pos--;
            } else if ((c == '-' || c == '−') &&
                    (pos == 0 || InputProcessor.isOperator(expression.charAt(pos - 1)) ||
                            expression.charAt(pos - 1) == '(')) {
                // This is a negative sign
                return pos;
            } else {
                break;
            }
        }

        // Check if we found a valid number
        if (pos < numberEnd - 1) {
            return pos + 1;
        }

        return -1;
    }

    /**
     * Find the end position of a number starting at given position
     */
    private static int findNumberEnd(String expression, int start) {
        if (expression == null || start < 0 || start >= expression.length()) {
            return start;
        }

        // Check if starting with (-) pattern
        if (expression.charAt(start) == '(' && start + 1 < expression.length() &&
                (expression.charAt(start + 1) == '-' || expression.charAt(start + 1) == '−')) {
            // Find matching closing parenthesis
            int depth = 1;
            int pos = start + 2;
            while (pos < expression.length() && depth > 0) {
                char c = expression.charAt(pos);
                if (c == '(') depth++;
                else if (c == ')') depth--;
                pos++;
            }
            return pos;
        }

        // Regular number
        int pos = start;
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
     * Check if the number at start position is currently negated (wrapped with (-))
     */
    public static boolean isCurrentlyNegated(String expression, int startPos) {
        if (startPos < 0 || startPos >= expression.length()) {
            return false;
        }

        // Check if this is a (-) wrapped number
        if (expression.charAt(startPos) == '(' &&
                startPos + 1 < expression.length() &&
                (expression.charAt(startPos + 1) == '-' || expression.charAt(startPos + 1) == '−')) {

            // Find the matching closing parenthesis
            int depth = 1;
            int pos = startPos + 2;
            while (pos < expression.length() && depth > 0) {
                char c = expression.charAt(pos);
                if (c == '(') depth++;
                else if (c == ')') depth--;
                pos++;
            }

            return depth == 0;
        }

        return false;
    }
}
