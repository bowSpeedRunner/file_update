package com.calculator.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * History record entity
 * Represents a single calculation history entry
 */
public class HistoryRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat ISO_FORMAT;

    static {
        ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);
        ISO_FORMAT.setTimeZone(TimeZone.getDefault());
    }

    private long id;
    private String expression;
    private String result;
    private String createdAt;

    public HistoryRecord() {
    }

    public HistoryRecord(long id, String expression, String result, String createdAt) {
        this.id = id;
        this.expression = expression;
        this.result = result;
        this.createdAt = createdAt;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Create a new history record with auto-generated id and timestamp
     */
    public static HistoryRecord create(String expression, String result) {
        HistoryRecord record = new HistoryRecord();
        record.setId(System.currentTimeMillis());
        record.setExpression(expression);
        record.setResult(result);
        record.setCreatedAt(ISO_FORMAT.format(new Date()));
        return record;
    }

    /**
     * Get display time in readable format
     */
    public String getDisplayTime() {
        try {
            Date date = ISO_FORMAT.parse(createdAt);
            if (date == null) return "";

            SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
            return displayFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryRecord that = (HistoryRecord) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
