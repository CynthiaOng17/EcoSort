package com.example.ecosort.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecosort.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE   = "score";
    public static final String EXTRA_CORRECT = "correct";
    public static final String EXTRA_WRONG   = "wrong";
    public static final String EXTRA_TIP     = "tip";
    public static final String EXTRA_ITEMS   = "game_items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int    score   = getIntent().getIntExtra(EXTRA_SCORE,   0);
        int    correct = getIntent().getIntExtra(EXTRA_CORRECT, 0);
        int    wrong   = getIntent().getIntExtra(EXTRA_WRONG,   0);
        String tip     = getIntent().getStringExtra(EXTRA_TIP);
        int    diff    = getIntent().getIntExtra(MenuActivity.EXTRA_DIFFICULTY, MenuActivity.EASY);

        @SuppressWarnings("unchecked")
        List<WasteItem> items = (ArrayList<WasteItem>) getIntent().getSerializableExtra(EXTRA_ITEMS);

        TextView tvEmoji  = findViewById(R.id.tv_result_emoji);
        TextView tvTitle  = findViewById(R.id.tv_result_title);
        TextView tvSub    = findViewById(R.id.tv_result_subtitle);
        TextView tvRight  = findViewById(R.id.tv_correct);
        TextView tvWrong  = findViewById(R.id.tv_wrong);
        TextView tvFinal  = findViewById(R.id.tv_final_score);
        TextView tvTip    = findViewById(R.id.tv_eco_tip);
        TextView tvNewRecord = findViewById(R.id.tv_new_highscore);
        LinearLayout llSummary = findViewById(R.id.ll_item_summary);

        // High Score logic
        int currentHighScore = getSharedPreferences("EcoSort", MODE_PRIVATE).getInt("highscore", 0);
        if (score > currentHighScore) {
            getSharedPreferences("EcoSort", MODE_PRIVATE).edit().putInt("highscore", score).apply();
            tvNewRecord.setVisibility(android.view.View.VISIBLE);
        } else {
            tvNewRecord.setVisibility(android.view.View.GONE);
        }

        // Populate item summary
        if (items != null) {
            Set<String> seen = new HashSet<>();
            for (WasteItem item : items) {
                if (seen.contains(item.name)) continue;
                seen.add(item.name);

                TextView tv = new TextView(this);
                String categoryStr = item.category == WasteItem.ORGANIC ? getString(R.string.organic)
                        : item.category == WasteItem.RECYCLABLE ? getString(R.string.recycle)
                        : getString(R.string.hazardous);

                int color;
                if (!item.isCorrectlySorted) {
                    color = 0xFFEF5350; // High-visibility Red for errors
                } else {
                    color = item.category == WasteItem.ORGANIC ? 0xFFC5E1A5  // Bright Light Green
                            : item.category == WasteItem.RECYCLABLE ? 0xFF90CAF9 // Bright Sky Blue
                            : 0xFFFF9800; // Bright Vivid Orange
                }

                tv.setText(item.emoji + " " + item.name + " → " + categoryStr + (item.isCorrectlySorted ? "" : " ❌"));
                tv.setTextColor(color);
                tv.setTextSize(18);
                tv.setPadding(0, 4, 0, 4);
                llSummary.addView(tv);
            }
        }

        // Dynamic result message based on accuracy
        int total = correct + wrong;
        float accuracy = total > 0 ? (correct * 100f / total) : 0;

        if (accuracy >= 80) {
            tvEmoji.setText("🌍");
            tvTitle.setText(R.string.eco_champion);
            tvSub.setText(R.string.malaysia_proud);
        } else if (accuracy >= 50) {
            tvEmoji.setText("♻️");
            tvTitle.setText(R.string.good_effort);
            tvSub.setText(R.string.keep_sorting);
        } else {
            tvEmoji.setText("🌫️");
            tvTitle.setText(R.string.keep_practising);
            tvSub.setText(R.string.pollution_rising);
        }

        tvRight.setText(String.valueOf(correct));
        tvWrong.setText(String.valueOf(wrong));
        tvFinal.setText(String.valueOf(score));
        tvTip.setText(tip != null ? tip : "");

        // ── High-Visibility Color Updates ──
        tvRight.setTextColor(0xFF2E7D32); // Vibrant Green
        tvWrong.setTextColor(0xFFD32F2F); // Crimson Red

        Button btnPlayAgain = findViewById(R.id.btn_play_again);
        Button btnMenu      = findViewById(R.id.btn_main_menu);
        Button btnShare     = findViewById(R.id.btn_share);

        btnShare.setOnClickListener(v -> {
            String message = getString(R.string.share_message, score);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
        });

        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(MenuActivity.EXTRA_DIFFICULTY, diff);
            startActivity(intent);
            finish();
        });

        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
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
}