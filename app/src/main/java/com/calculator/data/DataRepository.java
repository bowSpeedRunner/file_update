package com.calculator.data;

import android.content.Context;
import android.util.Log;

import com.calculator.model.CalculatorMode;
import com.calculator.model.CalculatorState;
import com.calculator.model.HistoryRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data repository - unified data access entry point
 * Singleton pattern
 */
public class DataRepository {
    private static final String TAG = "DataRepository";
    private static DataRepository instance;

    private final JsonDataManager jsonDataManager;

    private DataRepository(Context context) {
        jsonDataManager = new JsonDataManager(context);
    }

    /**
     * Get singleton instance
     */
    public static synchronized DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(context);
        }
        return instance;
    }

    /**
     * Load current calculator state
     */
    public CalculatorState loadCurrentState() {
        try {
            JSONObject root = jsonDataManager.loadJsonFile();
            JSONObject current = root.getJSONObject(DataConstants.KEY_CURRENT);

            String content = current.optString(DataConstants.KEY_CONTENT, "");
            String modeStr = current.optString(DataConstants.KEY_MODE, "basic");
            int cursorPosition = current.optInt(DataConstants.KEY_CURSOR_POSITION, 0);

            CalculatorMode mode = CalculatorMode.fromValue(modeStr);

            return new CalculatorState(content, mode, cursorPosition);

        } catch (JSONException e) {
            Log.e(TAG, "Error loading calculator state", e);
            return new CalculatorState();
        }
    }

    /**
     * Save current calculator state
     */
    public void saveCurrentState(CalculatorState state) {
        try {
            JSONObject root = jsonDataManager.loadJsonFile();

            JSONObject current = new JSONObject();
            current.put(DataConstants.KEY_CONTENT, state.getContent());
            current.put(DataConstants.KEY_MODE, state.getMode().getValue());
            current.put(DataConstants.KEY_CURSOR_POSITION, state.getCursorPosition());

            root.put(DataConstants.KEY_CURRENT, current);
            jsonDataManager.saveJsonFile(root);

        } catch (JSONException e) {
            Log.e(TAG, "Error saving calculator state", e);
        }
    }

    /**
     * Load all history records
     */
    public List<HistoryRecord> loadHistory() {
        try {
            JSONObject root = jsonDataManager.loadJsonFile();
            JSONArray historyArray = root.optJSONArray(DataConstants.KEY_HISTORY);

            if (historyArray == null) {
                return new ArrayList<>();
            }

            List<HistoryRecord> history = new ArrayList<>();
            for (int i = 0; i < historyArray.length(); i++) {
                JSONObject item = historyArray.getJSONObject(i);
                HistoryRecord record = new HistoryRecord();
                record.setId(item.optLong(DataConstants.KEY_ID, 0));
                record.setExpression(item.optString(DataConstants.KEY_EXPRESSION, ""));
                record.setResult(item.optString(DataConstants.KEY_RESULT, ""));
                record.setCreatedAt(item.optString(DataConstants.KEY_CREATED_AT, ""));
                history.add(record);
            }

            // Sort by id descending (newest first)
            Collections.sort(history, (a, b) -> Long.compare(b.getId(), a.getId()));
            return history;

        } catch (JSONException e) {
            Log.e(TAG, "Error loading history", e);
            return new ArrayList<>();
        }
    }

    /**
     * Add a new history record
     */
    public void addHistoryRecord(HistoryRecord record) {
        try {
            JSONObject root = jsonDataManager.loadJsonFile();
            JSONArray historyArray = root.optJSONArray(DataConstants.KEY_HISTORY);

            if (historyArray == null) {
                historyArray = new JSONArray();
            }

            // Check for duplicates
            if (isDuplicate(record.getExpression(), record.getResult())) {
                return;
            }

            // Create new record JSON
            JSONObject recordJson = new JSONObject();
            recordJson.put(DataConstants.KEY_ID, record.getId());
            recordJson.put(DataConstants.KEY_EXPRESSION, record.getExpression());
            recordJson.put(DataConstants.KEY_RESULT, record.getResult());
            recordJson.put(DataConstants.KEY_CREATED_AT, record.getCreatedAt());

            // Add to beginning of array
            JSONArray newArray = new JSONArray();
            newArray.put(recordJson);
            for (int i = 0; i < historyArray.length() && i < DataConstants.MAX_HISTORY_RECORDS - 1; i++) {
                newArray.put(historyArray.get(i));
            }

            root.put(DataConstants.KEY_HISTORY, newArray);
            jsonDataManager.saveJsonFile(root);

        } catch (JSONException e) {
            Log.e(TAG, "Error adding history record", e);
        }
    }

    /**
     * Delete history records by IDs
     */
    public void deleteHistoryRecords(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        try {
            JSONObject root = jsonDataManager.loadJsonFile();
            JSONArray historyArray = root.optJSONArray(DataConstants.KEY_HISTORY);

            if (historyArray == null) {
                return;
            }

            JSONArray newArray = new JSONArray();
            for (int i = 0; i < historyArray.length(); i++) {
                JSONObject item = historyArray.getJSONObject(i);
                long id = item.optLong(DataConstants.KEY_ID, 0);

                if (!ids.contains(id)) {
                    newArray.put(item);
                }
            }

            root.put(DataConstants.KEY_HISTORY, newArray);
            jsonDataManager.saveJsonFile(root);

        } catch (JSONException e) {
            Log.e(TAG, "Error deleting history records", e);
        }
    }

    /**
     * Clear all history
     */
    public void clearAllHistory() {
        try {
            JSONObject root = jsonDataManager.loadJsonFile();
            root.put(DataConstants.KEY_HISTORY, new JSONArray());
            jsonDataManager.saveJsonFile(root);
        } catch (JSONException e) {
            Log.e(TAG, "Error clearing history", e);
        }
    }

    /**
     * Check if a record with the same expression and result already exists
     */
    public boolean isDuplicate(String expression, String result) {
        List<HistoryRecord> history = loadHistory();

        // Only check the most recent record
        if (!history.isEmpty()) {
            HistoryRecord latest = history.get(0);
            return latest.getExpression().equals(expression) &&
                    latest.getResult().equals(result);
        }

        return false;
    }
}
