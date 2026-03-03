package com.calculator.calculator;

import android.util.Log;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Expression evaluator using exp4j library
 * Handles all mathematical calculations
 */
public class ExpressionEvaluator {
    private static final String TAG = "ExpressionEvaluator";

    // Custom factorial operator (postfix, 1 operand, high precedence)
    private static final Operator FACTORIAL = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
        @Override
        public double apply(double... args) {
            int n = (int) args[0];
            if (n < 0) return Double.NaN;
            if (n > 170) return Double.POSITIVE_INFINITY;

            double result = 1;
            for (int i = 2; i <= n; i++) {
                result *= i;
            }
            return result;
        }
    };

    // Result wrapper class
    public static class EvaluationResult {
        private final boolean success;
        private final double value;
        private final String error;

        private EvaluationResult(boolean success, double value, String error) {
            this.success = success;
            this.value = value;
            this.error = error;
        }

        public static EvaluationResult success(double value) {
            return new EvaluationResult(true, value, null);
        }

        public static EvaluationResult error(String error) {
            return new EvaluationResult(false, 0, error);
        }

        public boolean isSuccess() {
            return success;
        }

        public double getValue() {
            return value;
        }

        public String getError() {
            return error;
        }
    }

    /**
     * Evaluate a mathematical expression
     */
    public EvaluationResult evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return EvaluationResult.error("Empty expression");
        }

        try {
            // Preprocess the expression
            String processed = preprocessExpression(expression);

            // Build and evaluate
            Expression exp = new ExpressionBuilder(processed)
                    .operator(FACTORIAL)
                    .function(createTrigFunction("sin", true))
                    .function(createTrigFunction("cos", true))
                    .function(createTrigFunction("tan", true))
                    .function(createLogFunction("ln", Math.E))
                    .function(createLogFunction("log", 10))
                    .function(createSqrtFunction())
                    .build();

            double result = exp.evaluate();

            // Check for special values
            if (Double.isNaN(result)) {
                return EvaluationResult.error("Invalid result");
            }

            if (Double.isInfinite(result)) {
                return EvaluationResult.error("Result too large");
            }

            return EvaluationResult.success(result);

        } catch (ArithmeticException e) {
            Log.e(TAG, "Arithmetic error: " + e.getMessage());
            return EvaluationResult.error("Division by zero");
        } catch (Exception e) {
            Log.e(TAG, "Evaluation error: " + e.getMessage());
            return EvaluationResult.error("Invalid expression");
        }
    }

    /**
     * Check if expression is valid
     */
    public boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }

        try {
            String processed = preprocessExpression(expression);
            new ExpressionBuilder(processed)
                    .operator(FACTORIAL)
                    .build();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Preprocess expression: convert display symbols to calculation symbols
     */
    private String preprocessExpression(String expr) {
        String result = expr;

        // Convert display operators to exp4j operators
        result = result.replace("×", "*");
        result = result.replace("÷", "/");
        result = result.replace("−", "-");

        // Handle (-) negative number wrapper
        // (-5) -> (0-5) for exp4j to handle correctly
        result = result.replace("(-", "(0-");

        // Handle functions
        result = handleScientificFunctions(result);

        // Handle constants
        result = result.replace("π", String.valueOf(Math.PI));
        result = result.replace("e", String.valueOf(Math.E));

        // Handle percentages - convert % to /100
        result = handlePercentages(result);

        // Handle factorial - ensure it's properly placed
        // exp4j uses postfix notation for factorial

        return result;
    }

    /**
     * Handle scientific function calls
     */
    private String handleScientificFunctions(String expr) {
        String result = expr;

        // Convert function calls to exp4j format
        // Already in correct format for sin, cos, tan, ln, log, sqrt
        // Just ensure proper parentheses

        // Handle sqrt symbol
        result = result.replace("√", "sqrt");

        return result;
    }

    /**
     * Handle percentage calculations
     */
    private String handlePercentages(String expr) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < expr.length()) {
            if (expr.charAt(i) == '%') {
                // Find the number before %
                int start = i - 1;
                while (start >= 0 && (Character.isDigit(expr.charAt(start)) ||
                        expr.charAt(start) == '.' || expr.charAt(start) == ',')) {
                    start--;
                }
                start++;

                // Insert /100
                result.append("/100");
            } else {
                result.append(expr.charAt(i));
            }
            i++;
        }

        // Handle simple case
        String finalResult = result.toString();
        if (finalResult.contains("%")) {
            // Fallback: just replace all % with /100
            finalResult = finalResult.replace("%", "/100");
        }

        return finalResult;
    }

    /**
     * Create trigonometric function with degree-to-radian conversion
     */
    private Function createTrigFunction(String name, boolean useDegrees) {
        return new Function(name, 1) {
            @Override
            public double apply(double... args) {
                double angle = args[0];
                if (useDegrees) {
                    angle = Math.toRadians(angle);
                }
                switch (name) {
                    case "sin":
                        return Math.sin(angle);
                    case "cos":
                        return Math.cos(angle);
                    case "tan":
                        return Math.tan(angle);
                    default:
                        return Double.NaN;
                }
            }
        };
    }

    /**
     * Create logarithm function
     */
    private Function createLogFunction(String name, double base) {
        return new Function(name, 1) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(base);
            }
        };
    }

    /**
     * Create square root function
     */
    private Function createSqrtFunction() {
        return new Function("sqrt", 1) {
            @Override
            public double apply(double... args) {
                if (args[0] < 0) {
                    return Double.NaN;
                }
                return Math.sqrt(args[0]);
            }
        };
    }
}
