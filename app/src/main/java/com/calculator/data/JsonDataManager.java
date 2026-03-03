package com.calculator.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * JSON file read/write manager
 */
public class JsonDataManager {
    private static final String TAG = "JsonDataManager";

    private final Context context;
    private final String fileName;

    public JsonDataManager(Context context) {
        this.context = context.getApplicationContext();
        this.fileName = DataConstants.DATA_FILE_NAME;
    }

    /**
     * Load JSON data from file
     * Returns empty object if file doesn't exist
     */
    public JSONObject loadJsonFile() {
        File file = new File(context.getFilesDir(), fileName);

        if (!file.exists()) {
            return createEmptyJson();
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return new JSONObject(sb.toString());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error loading JSON file", e);
            return createEmptyJson();
        }
    }

    /**
     * Save JSON data to file
     */
    public void saveJsonFile(JSONObject data) {
        File file = new File(context.getFilesDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            String jsonStr = data.toString(2);
            fos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error saving JSON file", e);
        }
    }

    /**
     * Get the file path for debugging
     */
    public String getFilePath() {
        return new File(context.getFilesDir(), fileName).getAbsolutePath();
    }

    /**
     * Check if data file exists
     */
    public boolean fileExists() {
        return new File(context.getFilesDir(), fileName).exists();
    }

    /**
     * Delete the data file
     */
    public boolean deleteFile() {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists() && file.delete();
    }

    /**
     * Create empty JSON structure
     */
    private JSONObject createEmptyJson() {
        try {
            JSONObject root = new JSONObject();
            JSONObject current = new JSONObject();
            current.put(DataConstants.KEY_CONTENT, "");
            current.put(DataConstants.KEY_MODE, "basic");
            current.put(DataConstants.KEY_CURSOR_POSITION, 0);
            root.put(DataConstants.KEY_CURRENT, current);
            root.put(DataConstants.KEY_HISTORY, new org.json.JSONArray());
            return root;
        } catch (JSONException e) {
            Log.e(TAG, "Error creating empty JSON", e);
            return new JSONObject();
        }
    }
}
