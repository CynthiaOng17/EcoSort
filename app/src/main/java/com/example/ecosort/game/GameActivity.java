package com.example.ecosort.game;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    // ── Game config ────────────────────────────────────────────────────────────
    private static final int MAX_LIVES    = 3;
    private static final int POINTS_RIGHT = 10;
    private static final int POINTS_WRONG = -5;

    // Fall animation durations per difficulty (ms)
    private static final int[] FALL_DURATIONS = {1800, 1100, 700, 1500};
    // Total items per difficulty
    private static final int[] ITEM_COUNTS    = {10, 20, 30, Integer.MAX_VALUE};

    // ── Views ──────────────────────────────────────────────────────────────────
    private TextView     tvScore, tvTimer, tvLives, tvFallingItem, tvStreak;
    private View         pollutionOverlay;
    private LinearLayout binOrganic, binRecycle, binHazardous;
    private View         binsRow;

    // ── State ──────────────────────────────────────────────────────────────────
    private int difficulty;
    private int score       = 0;
    private int lives       = MAX_LIVES;
    private int correctCount = 0;
    private int wrongCount   = 0;
    private int currentIndex = 0;
    private int fallDuration;
    private int totalItems;
    private int pointsPerCorrect;
    private int streak = 0;
    private int multiplier = 1;

    private List<WasteItem> gameItems;
    private WasteItem       currentItem;
    private ObjectAnimator  fallAnimator;
    private CountDownTimer  gameTimer;
    private long            elapsedSeconds = 0;
    private boolean         isMuted = false;

    // Drag tracking
    private float dragStartX, dragStartY;

    // Power-up state
    private boolean isMagnetActive = false;
    private int magnetUsesLeft = 0;
    private boolean isFreezeActive = false;

    // Eco tips shown on result screen
    private String[] getEcoTips() {
        return getResources().getStringArray(R.array.eco_tips);
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bindViews();

        difficulty = getIntent().getIntExtra(MenuActivity.EXTRA_DIFFICULTY, MenuActivity.EASY);
        fallDuration   = FALL_DURATIONS[difficulty];
        totalItems     = ITEM_COUNTS[difficulty];

        // Scoring based on difficulty
        pointsPerCorrect = (difficulty == MenuActivity.EASY) ? 100
                : (difficulty == MenuActivity.MEDIUM) ? 150
                : 200;

        buildItemList();
        
        // Hide game item until start button is pressed
        tvFallingItem.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fallAnimator != null) fallAnimator.cancel();
        if (gameTimer   != null) gameTimer.cancel();
    }

    // ── Setup ──────────────────────────────────────────────────────────────────
    private void bindViews() {
        tvScore          = findViewById(R.id.tv_score);
        tvTimer          = findViewById(R.id.tv_timer);
        tvLives          = findViewById(R.id.tv_lives);
        tvFallingItem    = findViewById(R.id.tv_falling_item);
        tvStreak         = findViewById(R.id.tv_streak);
        pollutionOverlay = findViewById(R.id.pollution_overlay);
        binOrganic       = findViewById(R.id.bin_organic);
        binRecycle       = findViewById(R.id.bin_recycle);
        binHazardous     = findViewById(R.id.bin_hazardous);
        binsRow          = findViewById(R.id.bins_row);

        findViewById(R.id.btn_exit).setOnClickListener(v -> finish());

        isMuted = getSharedPreferences("EcoSort", MODE_PRIVATE).getBoolean("muted", false);
        TextView btnMute = findViewById(R.id.btn_mute);
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

        View instructionOverlay = findViewById(R.id.instruction_overlay);
        findViewById(R.id.btn_start_game).setOnClickListener(v -> {
            instructionOverlay.setVisibility(View.GONE);
            startGameTimer();
            showNextItem();
        });

        // Drag listener on the falling item
        tvFallingItem.setOnTouchListener(this::onItemTouch);
    }

    private void buildItemList() {
        for (WasteItem item : WasteItem.ALL) {
            item.isCorrectlySorted = false;
        }

        List<WasteItem> pool = new ArrayList<>(Arrays.asList(WasteItem.ALL));
        Collections.shuffle(pool);
        
        if (difficulty == MenuActivity.SURVIVAL) {
            gameItems = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Collections.shuffle(pool);
                gameItems.addAll(new ArrayList<>(pool));
            }
        } else {
            gameItems = pool.subList(0, Math.min(totalItems, pool.size()));
        }
    }

    // ── Timer ──────────────────────────────────────────────────────────────────
    private void startGameTimer() {
        gameTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override public void onTick(long ms) {
                elapsedSeconds++;
                long m = elapsedSeconds / 60, s = elapsedSeconds % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "⏱ %d:%02d", m, s));
            }
            @Override public void onFinish() {}
        }.start();
    }

    // ── Item falling ───────────────────────────────────────────────────────────
    private void showNextItem() {
        if (currentIndex >= gameItems.size()) {
            endGame();
            return;
        }

        // Randomly spawn power-ups (5% chance)
        if (Math.random() < 0.05) {
            spawnPowerUp();
            return;
        }

        currentItem = gameItems.get(currentIndex++);
        tvFallingItem.setText(currentItem.emoji);
        tvFallingItem.setTag(null); // Not a power-up

        startFallingAnimation();
    }

    private void spawnPowerUp() {
        String[] powers = {"❤️", "❄️", "✨"};
        int rand = (int)(Math.random() * powers.length);
        String power = powers[rand];
        
        tvFallingItem.setText(power);
        tvFallingItem.setTag(power); // Mark as power-up
        
        startFallingAnimation();
    }

    private void startFallingAnimation() {
        tvFallingItem.post(() -> {
            ViewGroup parent = (ViewGroup) tvFallingItem.getParent();
            int screenW = parent.getWidth();
            int itemW   = tvFallingItem.getWidth();
            int startX  = (int) (Math.random() * (screenW - itemW));
            int startY  = findViewById(R.id.hud).getHeight();
            int endY    = binsRow.getTop() - tvFallingItem.getHeight();

            tvFallingItem.setX(startX);
            tvFallingItem.setY(startY);
            tvFallingItem.setVisibility(View.VISIBLE);

            long duration = isFreezeActive ? (long) fallDuration * 2 : fallDuration;

            fallAnimator = ObjectAnimator.ofFloat(tvFallingItem, "y", startY, endY);
            fallAnimator.setDuration(duration);
            fallAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    if (tvFallingItem.getTag() != null) {
                        // Power-up missed, just go to next
                        tvFallingItem.setVisibility(View.INVISIBLE);
                        showNextItem();
                    } else {
                        handleSort(-1);
                    }
                }
            });
            
            if (isMagnetActive && tvFallingItem.getTag() == null) {
                magnetUsesLeft--;
                if (magnetUsesLeft <= 0) isMagnetActive = false;
                handleSort(currentItem.category);
            } else {
                fallAnimator.start();
            }
        });
    }

    // ── Drag & Drop ────────────────────────────────────────────────────────────
    private boolean onItemTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (fallAnimator != null) {
                    fallAnimator.removeAllListeners();
                    fallAnimator.cancel();
                }
                v.bringToFront();
                dragStartX = event.getX();
                dragStartY = event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float newX = v.getX() + event.getX() - dragStartX;
                float newY = v.getY() + event.getY() - dragStartY;
                v.setX(newX);
                v.setY(newY);
                return true;

            case MotionEvent.ACTION_UP:
                if (v.getTag() != null) {
                    applyPowerUp((String)v.getTag());
                    v.setVisibility(View.INVISIBLE);
                    showNextItem();
                } else {
                    checkDrop(v, event.getRawX(), event.getRawY());
                }
                return true;
        }
        return false;
    }

    private void applyPowerUp(String type) {
        switch(type) {
            case "❤️":
                if (lives < MAX_LIVES) lives++;
                break;
            case "❄️":
                isFreezeActive = true;
                tvFallingItem.postDelayed(() -> isFreezeActive = false, 5000);
                break;
            case "✨":
                isMagnetActive = true;
                magnetUsesLeft = 3;
                break;
        }
        updateHud();
    }

    private void checkDrop(View v, float rawX, float rawY) {
        int[] loc = new int[2];

        binOrganic.getLocationOnScreen(loc);
        if (rawX > loc[0] && rawX < loc[0] + binOrganic.getWidth()
                && rawY > loc[1] && rawY < loc[1] + binOrganic.getHeight()) {
            handleSort(WasteItem.ORGANIC);
            return;
        }

        binRecycle.getLocationOnScreen(loc);
        if (rawX > loc[0] && rawX < loc[0] + binRecycle.getWidth()
                && rawY > loc[1] && rawY < loc[1] + binRecycle.getHeight()) {
            handleSort(WasteItem.RECYCLABLE);
            return;
        }

        binHazardous.getLocationOnScreen(loc);
        if (rawX > loc[0] && rawX < loc[0] + binHazardous.getWidth()
                && rawY > loc[1] && rawY < loc[1] + binHazardous.getHeight()) {
            handleSort(WasteItem.HAZARDOUS);
            return;
        }

        resumeFall(v);
    }

    private void resumeFall(View v) {
        int endY = binsRow.getTop() - v.getHeight();
        long remaining = (long) ((1 - (v.getY() / endY)) * fallDuration);
        long finalRemaining = Math.max(remaining, 200);

        fallAnimator = ObjectAnimator.ofFloat(v, "y", v.getY(), endY);
        fallAnimator.setDuration(finalRemaining);
        fallAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(android.animation.Animator a) {
                handleSort(-1);
            }
        });
        fallAnimator.start();
    }

    // ── Scoring ────────────────────────────────────────────────────────────────
    private void handleSort(int binCategory) {
        tvFallingItem.setVisibility(View.INVISIBLE);
        if (fallAnimator != null) fallAnimator.cancel();

        boolean correct = (binCategory == currentItem.category);
        currentItem.isCorrectlySorted = correct;

        if (correct) {
            streak++;
            multiplier = (streak >= 10) ? 3 : (streak >= 5) ? 2 : 1;
            score += (pointsPerCorrect * multiplier);
            correctCount++;
            flashBin(binCategory);
            
            getSharedPreferences("EcoSort_Library", MODE_PRIVATE)
                .edit().putBoolean(currentItem.name, true).apply();
                
            if (difficulty == MenuActivity.SURVIVAL && correctCount % 10 == 0) {
                fallDuration = Math.max(400, fallDuration - 100);
            }
        } else {
            streak = 0;
            multiplier = 1;
            lives -= 1;
            wrongCount++;
            showPollution();
            vibrateDevice();
        }

        updateHud();

        if (lives <= 0) {
            endGame();
        } else {
            tvFallingItem.postDelayed(this::showNextItem, 600);
        }
    }

    private void vibrateDevice() {
        android.os.Vibrator v = (android.os.Vibrator) getSystemService(android.content.Context.VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                v.vibrate(android.os.VibrationEffect.createOneShot(150, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(150);
            }
        }
    }

    private void updateHud() {
        tvScore.setText(getString(R.string.score_label, score));
        if (multiplier > 1) {
            tvStreak.setVisibility(View.VISIBLE);
            tvStreak.setText(getString(R.string.multiplier_msg, multiplier, streak));
        } else {
            tvStreak.setVisibility(streak > 0 ? View.VISIBLE : View.GONE);
            tvStreak.setText(getString(R.string.streak_msg, streak));
        }

        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < MAX_LIVES; i++) hearts.append(i < lives ? "❤️" : "🖤");
        tvLives.setText(hearts);
        
        if (isMagnetActive) tvStreak.append("\n" + getString(R.string.magnet_active, magnetUsesLeft));
        if (isFreezeActive) tvStreak.append("\n" + getString(R.string.freeze_active));
    }

    private void flashBin(int category) {
        View bin = category == WasteItem.ORGANIC    ? binOrganic
                : category == WasteItem.RECYCLABLE ? binRecycle
                : binHazardous;
        bin.animate().scaleX(1.15f).scaleY(1.15f).setDuration(150)
                .withEndAction(() -> bin.animate().scaleX(1f).scaleY(1f).setDuration(150).start())
                .start();
    }

    private void showPollution() {
        pollutionOverlay.setVisibility(View.VISIBLE);
        pollutionOverlay.animate().alpha(0f).setDuration(800)
                .withEndAction(() -> {
                    pollutionOverlay.setVisibility(View.GONE);
                    pollutionOverlay.setAlpha(1f);
                }).start();
    }

    private void endGame() {
        if (gameTimer != null) gameTimer.cancel();
        String[] tips = getEcoTips();
        String tip = tips[(int)(Math.random() * tips.length)];
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_SCORE,    score);
        intent.putExtra(ResultActivity.EXTRA_CORRECT,  correctCount);
        intent.putExtra(ResultActivity.EXTRA_WRONG,    wrongCount);
        intent.putExtra(ResultActivity.EXTRA_TIP,      tip);
        intent.putExtra(ResultActivity.EXTRA_ITEMS,    new ArrayList<>(gameItems));
        intent.putExtra(MenuActivity.EXTRA_DIFFICULTY, difficulty);
        startActivity(intent);
        finish();
    }

    private void updateMuteButton(TextView btn) {
        btn.setText(isMuted ? "🔇" : "🔊");
    }

    private void setLocale(String lang) {
        java.util.Locale locale = new java.util.Locale(lang);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void loadLocale() {
        String lang = getSharedPreferences("EcoSort", MODE_PRIVATE).getString("lang", "en");
        setLocale(lang);
    }
}