package com.example.ecosort.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Start music after 300ms
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                startService(new Intent(this, MusicService.class));
            } catch (Exception e) {
                Log.e(TAG, "Music service failed", e);
            }
        }, 300);

        // Go to menu after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        }, 3000);
    }
}