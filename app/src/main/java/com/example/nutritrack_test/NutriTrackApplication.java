package com.example.nutritrack_test;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class NutriTrackApplication extends Application {
    private static final String TAG = "NutriTrackApplication";
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        
        // Set up global exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(TAG, "Uncaught exception", ex);
                
                // Show a toast (might not work in all cases)
                try {
                    Toast.makeText(context, "An unexpected error occurred. Please restart the app.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error showing toast", e);
                }
                
                // Default handler
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, ex);
            }
        });
    }
    
    public static Context getAppContext() {
        return context;
    }
} 