package com.example.ecosort.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecosort.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    // Difficulty constants passed to GameActivity
    public static final String EXTRA_DIFFICULTY = "difficulty";
    public static final int EASY     = 0;
    public static final int MEDIUM   = 1;
    public static final int HARD     = 2;
    public static final int SURVIVAL = 3;

    private boolean isMuted = false;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        isMuted = getSharedPreferences("EcoSort", MODE_PRIVATE).getBoolean("muted", false);
        TextView btnMute = findViewById(R.id.btn_mute_menu);
        updateMuteButton(btnMute);

        btnMute.setOnClickListener(v -> {
            isMuted = !isMuted;
            getSharedPreferences("EcoSort", MODE_PRIVATE).edit().putBoolean("muted", isMuted).apply();
            updateMuteButton(btnMute);

            Intent intent = new Intent(this, MusicService.class);
            intent.setAction(MusicService.ACTION_TOGGLE_MUTE);
            intent.putExtra(MusicService.EXTRA_MUTE_STATE, isMuted);
            startService(intent);
        });

        findViewById(R.id.btn_lang).setOnClickListener(v -> toggleLanguage());

        // Bottom Navigation
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_encyclopedia) {
                startActivity(new Intent(this, EncyclopediaActivity.class));
                return true;
            } else if (id == R.id.nav_learn) {
                startActivity(new Intent(this, IntroActivity.class));
                return true;
            }
            return false;
        });

        Button btnEasy     = findViewById(R.id.btn_easy);
        Button btnMedium   = findViewById(R.id.btn_medium);
        Button btnHard     = findViewById(R.id.btn_hard);
        Button btnSurvival = findViewById(R.id.btn_survival);

        btnEasy.setOnClickListener(v     -> startGame(EASY));
        btnMedium.setOnClickListener(v   -> startGame(MEDIUM));
        btnHard.setOnClickListener(v     -> startGame(HARD));
        btnSurvival.setOnClickListener(v -> startGame(SURVIVAL));

        displayHighScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayHighScore();
        // Re-highlight "Home" when returning from Library / Intro
        bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void displayHighScore() {
        int highScore = getSharedPreferences("EcoSort", MODE_PRIVATE).getInt("highscore", 0);
        TextView tvHighScore = findViewById(R.id.tv_high_score);
        if (tvHighScore != null) {
            tvHighScore.setText(getString(R.string.best_label, highScore));
        }
    }

    private void startGame(int difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivity(intent);
    }

    private void updateMuteButton(TextView btn) {
        btn.setText(isMuted ? "🔇" : "🔊");
    }

    private void toggleLanguage() {
        String current = getSharedPreferences("EcoSort", MODE_PRIVATE).getString("lang", "en");
        String next = current.equals("en") ? "ms" : "en";
        setLocale(next);
        recreate();
    }

    private void setLocale(String lang) {
        java.util.Locale locale = new java.util.Locale(lang);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        getSharedPreferences("EcoSort", MODE_PRIVATE).edit().putString("lang", lang).apply();
    }

    private void loadLocale() {
        String lang = getSharedPreferences("EcoSort", MODE_PRIVATE).getString("lang", "en");
        setLocale(lang);
    }
}