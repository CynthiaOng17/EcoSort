package com.example.ecosort.game;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Pauses the background music when the whole app goes to the background
 * and resumes it when any activity becomes visible again.
 */
public class EcoSortApp extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "EcoSortApp";
    // Small delay so switching between activities (or recreate()) doesn't cause a music blip
    private static final long PAUSE_DELAY_MS = 700;

    private int startedCount = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable pauseRunnable = MusicService::pauseMusic;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        startedCount++;
        handler.removeCallbacks(pauseRunnable);
        if (startedCount == 1) {
            try {
                startService(new Intent(this, MusicService.class));
            } catch (Exception e) {
                Log.e(TAG, "Could not resume music", e);
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        startedCount = Math.max(0, startedCount - 1);
        if (startedCount == 0) {
            handler.postDelayed(pauseRunnable, PAUSE_DELAY_MS);
        }
    }

    @Override public void onActivityCreated(Activity a, Bundle b) {}
    @Override public void onActivityResumed(Activity a) {}
    @Override public void onActivityPaused(Activity a) {}
    @Override public void onActivitySaveInstanceState(Activity a, Bundle b) {}
    @Override public void onActivityDestroyed(Activity a) {}
}