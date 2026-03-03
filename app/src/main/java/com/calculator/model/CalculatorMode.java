package com.calculator.model;

/**
 * Calculator mode enumeration
 */
public enum CalculatorMode {
    BASIC("basic"),
    SCIENTIFIC("scientific");

    private final String value;

    CalculatorMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CalculatorMode fromValue(String value) {
        if (value == null) {
            return BASIC;
        }
        for (CalculatorMode mode : values()) {
            if (mode.value.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return BASIC;
    }
}
