package com.calculator.calculator;

/**
 * Input validator
 * Validates input characters and expressions
 */
public final class InputValidator {
    private InputValidator() {
    }

    /**
     * Check if character is a valid input
     */
    public static boolean isValidInput(char c) {
        return Character.isDigit(c) ||
                c == '.' ||
                c == '+' ||
                c == '-' ||
                c == '−' ||
                c == '×' ||
                c == '*' ||
                c == '÷' ||
                c == '/' ||
                c == '%' ||
                c == '(' ||
                c == ')' ||
                c == '^' ||
                c == '!';
    }

    /**
     * Check if expression is valid for evaluation
     */
    public static boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }

        // Check for balanced parentheses
        int depth = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') depth++;
            else if (c == ')') depth--;
            if (depth < 0) return false;
        }
        if (depth != 0) return false;

        // Check for valid ending (can't end with operator)
        char lastChar = expression.charAt(expression.length() - 1);
        if (InputProcessor.isOperator(lastChar) && lastChar != ')') {
            return false;
        }

        return true;
    }

    /**
     * Check if expression has a complete number
     */
    public static boolean hasCompleteNumber(String expression) {
        if (expression == null || expression.isEmpty()) {
            return false;
        }

        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
            if (InputProcessor.isOperator(c) || c == '(' || c == ')') {
                break;
            }
        }

        return false;
    }

    /**
     * Check if expression can accept a function
     */
    public static boolean canAcceptFunction(String expression, int cursorPos) {
        if (expression == null || expression.isEmpty()) {
            return true;
        }

        if (cursorPos == 0) {
            return true;
        }

        char prevChar = expression.charAt(cursorPos - 1);
        return InputProcessor.isOperator(prevChar) || prevChar == '(';
    }

    /**
     * Check if expression can accept parentheses
     */
    public static boolean canAcceptParenthesis(String expression, int cursorPos, boolean isOpen) {
        if (expression == null || expression.isEmpty()) {
            return isOpen;
        }

        if (cursorPos == 0) {
            return isOpen;
        }

        char prevChar = expression.charAt(cursorPos - 1);

        if (isOpen) {
            // Open parenthesis can follow operator, (, or be at start
            return InputProcessor.isOperator(prevChar) || prevChar == '(';
        } else {
            // Close parenthesis can follow digit or )
            return Character.isDigit(prevChar) || prevChar == ')';
        }
    }
}
