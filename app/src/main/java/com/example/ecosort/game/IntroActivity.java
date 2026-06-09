package com.example.ecosort.game;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_learn);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MenuActivity.class));
                return true;
            } else if (id == R.id.nav_learn) {
                return true;
            } else if (id == R.id.nav_encyclopedia) {
                startActivity(new Intent(this, EncyclopediaActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadLocale() {
        String lang = getSharedPreferences("EcoSort", MODE_PRIVATE).getString("lang", "en");
        java.util.Locale locale = new java.util.Locale(lang);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
