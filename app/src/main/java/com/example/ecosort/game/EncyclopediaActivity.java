package com.example.ecosort.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.R;

public class EncyclopediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia);

        GridView gridView = findViewById(R.id.gv_encyclopedia);
        SharedPreferences prefs = getSharedPreferences("EcoSort_Library", MODE_PRIVATE);

        gridView.setAdapter(new BaseAdapter() {
            @Override public int getCount() { return WasteItem.ALL.length; }
            @Override public Object getItem(int i) { return WasteItem.ALL[i]; }
            @Override public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                }
                WasteItem item = WasteItem.ALL[i];
                boolean discovered = prefs.getBoolean(item.name, false);

                TextView tv = view.findViewById(android.R.id.text1);
                tv.setText(discovered ? item.emoji : "❓");
                tv.setTextSize(30);
                tv.setGravity(android.view.Gravity.CENTER);
                tv.setBackgroundColor(0x22FFFFFF);
                tv.setPadding(0, 20, 0, 20);

                view.setOnClickListener(v -> {
                    if (discovered) {
                        new AlertDialog.Builder(EncyclopediaActivity.this)
                            .setTitle(item.emoji + " " + item.name)
                            .setMessage(item.fact)
                            .setPositiveButton(R.string.cool_btn, null)
                            .show();
                    } else {
                        Toast.makeText(EncyclopediaActivity.this, R.string.locked_item_msg, Toast.LENGTH_SHORT).show();
                    }
                });

                return view;
            }
        });

        findViewById(R.id.btn_back_menu).setOnClickListener(v -> finish());
    }

    private void loadLocale() {
        String lang = getSharedPreferences("EcoSort", MODE_PRIVATE).getString("lang", "en");
        java.util.Locale locale = new java.util.Locale(lang);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}