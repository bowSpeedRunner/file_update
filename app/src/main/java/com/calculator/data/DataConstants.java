package com.calculator.data;

/**
 * Data constants for the calculator app
 */
public final class DataConstants {
    private DataConstants() {
        // Prevent instantiation
    }

    // File names
    public static final String DATA_FILE_NAME = "data.json";

    // JSON keys
    public static final String KEY_CURRENT = "current";
    public static final String KEY_HISTORY = "history";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_MODE = "mode";
    public static final String KEY_CURSOR_POSITION = "cursor_position";
    public static final String KEY_ID = "id";
    public static final String KEY_EXPRESSION = "expression";
    public static final String KEY_RESULT = "result";
    public static final String KEY_CREATED_AT = "created_at";

    // Limits
    public static final int MAX_PLACEHOLDERS = 17;
    public static final int MAX_HISTORY_RECORDS = 100;
}
