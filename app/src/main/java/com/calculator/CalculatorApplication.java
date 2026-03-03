package com.calculator;

import android.app.Application;

/**
 * Calculator Application entry point
 */
public class CalculatorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Any application-level initialization
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Cleanup if needed
    }
}
