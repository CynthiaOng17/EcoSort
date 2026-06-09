package com.example.ecosort.game;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.example.ecosort.R;

public class MusicService extends Service {
    public static final String ACTION_TOGGLE_MUTE = "com.example.ecosort.TOGGLE_MUTE";
    public static final String EXTRA_MUTE_STATE = "mute_state";
    
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                
                // Read saved mute state from preferences
                boolean isMuted = getSharedPreferences("EcoSort", Context.MODE_PRIVATE)
                        .getBoolean("muted", false);
                float vol = isMuted ? 0.0f : 1.0f;
                mediaPlayer.setVolume(vol, vol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_TOGGLE_MUTE.equals(intent.getAction())) {
            boolean isMuted = intent.getBooleanExtra(EXTRA_MUTE_STATE, false);
            if (mediaPlayer != null) {
                float vol = isMuted ? 0.0f : 1.0f;
                mediaPlayer.setVolume(vol, vol);
            }
        }
        
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
