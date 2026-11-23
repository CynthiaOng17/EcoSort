# ♻️ EcoSort

**Sort waste. Save the planet.**

EcoSort is an educational Android game that teaches players how to separate household waste according to Malaysian recycling practices. Items fall from the top of the screen, and the player must drag each one into the correct bin before it hits the ground.

The app is fully bilingual (English and Bahasa Malaysia).

---

## ✨ Features

- **Drag-and-drop gameplay**: drag falling emoji items into one of three bins:
  - 🟣 **Organic**: food scraps, peels, plants
  - ♻️ **Recycle**: bottles, paper, cans, plastic, e-waste
  - ☣️ **Hazardous**: batteries, chemicals, bulbs, medical waste
- **4 difficulty modes** (see table below), including an endless **Survival** mode that speeds up as you improve
- **Streak multiplier**: build a streak of correct answers for 2x and 3x points
- **Power-ups** that randomly fall during play (5% chance per item)
- **Waste Library (Encyclopedia)**: items are unlocked once you sort them correctly, revealing a fun eco fact
- **Learn / Intro screen**: a short guide to recycling in Malaysia (including SWCorp and e-waste collection points)
- **Result screen** with accuracy-based feedback, a review of every item in the round, a random eco tip, and a *New Record* banner
- **Share your score** as a screenshot image through any app on the device
- **Language toggle** (🌐): switch between English and Bahasa Malaysia at any time
- **Background music** with a persistent mute button
- **Haptic feedback** (vibration) and a pollution overlay when you sort incorrectly
- **Persistent high score** and unlocked-item progress

---

## 🎮 How to Play

1. Choose a difficulty from the main menu.
2. Read the instructions and tap **START GAME**.
3. Touch and drag the falling item into the correct bin.
4. Sort correctly to earn points. Sort incorrectly (or let an item hit the ground) and you lose a life ❤️.
5. The game ends when you run out of lives or finish all the items.

### Difficulty Modes

| Mode | Items | Fall Time | Points per Correct |
|------|-------|-----------|--------------------|
| 🌱 Easy | 10 | 1.8 s | 100 |
| ⚡ Medium | 20 | 1.1 s | 150 |
| 🔥 Hard | 30 | 0.7 s | 200 |
| ♾️ Survival | Endless | 1.5 s (speeds up every 10 correct, minimum 0.4 s) | 200 |

You start every game with **3 lives**.

### Streak Multiplier

| Streak | Multiplier |
|--------|------------|
| 0–4 | 1x |
| 5–9 | 2x |
| 10+ | 3x |

A wrong answer resets the streak.

### Power-ups

| Icon | Name | Effect |
|------|------|--------|
| ❤️ | Heart | Restores one life (up to the maximum of 3) |
| ❄️ | Freeze | Items fall at half speed for 5 seconds |
| ✨ | Magnet | Automatically sorts the next 3 items correctly |

Power-ups are collected by tapping/dragging them. If you miss one, nothing is lost.

---

## 🛠️ Tech Stack

- **Language:** Java
- **Platform:** Android (native)
- **UI:** XML layouts, Material Components (`Theme.MaterialComponents.DayNight.NoActionBar`), `BottomNavigationView`
- **Animations:** `ObjectAnimator` / `ViewPropertyAnimator`
- **Audio:** `MediaPlayer` running inside a `Service`
- **Storage:** `SharedPreferences`
- **Sharing:** `FileProvider` + `Intent.ACTION_SEND`
- **Package:** `com.example.ecosort`

---

## 📁 Project Structure

```
com.example.ecosort.game
├── SplashActivity.java        # Splash screen, starts background music
├── MenuActivity.java          # Main menu: difficulty, mute, language toggle, high score
├── IntroActivity.java         # "Learn" screen about recycling in Malaysia
├── EncyclopediaActivity.java  # Waste Library grid of unlockable items
├── GameActivity.java          # Core gameplay (falling items, drag & drop, scoring, power-ups)
├── ResultActivity.java        # Results, round summary, eco tip, share score
├── MusicService.java          # Background music + mute handling
└── WasteItem.java             # Item model and the full catalogue (58 items, EN + MS)

res/
├── layout/       # activity_*.xml
├── drawable/     # bin cards, button backgrounds, icons
├── menu/         # bottom_nav_menu.xml
├── values/       # strings (English), colors, themes
├── values-ms/    # strings (Bahasa Malaysia)
├── xml/          # file_paths, backup_rules, data_extraction_rules
└── raw/          # bg_music (background music file)
```

---

## 🗂️ Data & Preferences

The app stores data locally using `SharedPreferences`:

| File | Key | Purpose |
|------|-----|---------|
| `EcoSort` | `highscore` | Best score |
| `EcoSort` | `lang` | Selected language (`en` / `ms`) |
| `EcoSort` | `muted` | Music mute state |
| `EcoSort_Library` | *item name* | Whether an item has been unlocked in the Waste Library |

No network access is required and no personal data is collected.

---

## 🚀 Getting Started

### Requirements

- [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
- An Android emulator or a physical Android device

### Build & Run

1. Clone this repository:
   ```bash
   git clone https://github.com/<your-username>/EcoSort.git
   ```
2. Open the project in Android Studio.
3. Make sure a music file named `bg_music` (e.g. `bg_music.mp3`) exists in `app/src/main/res/raw/`.
4. Let Gradle sync, then click **Run ▶** to launch on an emulator or device.

> **Note:** The game screen is locked to portrait orientation, and vibration requires the `VIBRATE` permission (already declared in the manifest).

---

## 🌏 Localization

- English strings live in `res/values/strings.xml`.
- Bahasa Malaysia strings live in `res/values-ms/strings.xml`.
- Item names and eco facts are stored directly in `WasteItem.java` (`name` / `nameMs`, `fact` / `factMs`).

To add another language, add a new `values-xx/strings.xml`, add the translated fields to `WasteItem`, and extend the language toggle in `MenuActivity`.

---

## ➕ Adding New Waste Items

Open `WasteItem.java` and add a new entry to the `ALL` array under the right category:

```text
new WasteItem("🍉", "Watermelon rind", "Kulit tembikai", ORGANIC,
        "Fact in English.", "Fakta dalam Bahasa Malaysia."),
```

Categories: `ORGANIC`, `RECYCLABLE`, `HAZARDOUS`. The item will automatically appear in the game and the Waste Library.

---

## 👩‍💻 Author

**Cynthia Ong Ka**

<!-- Add your course / university / contact details here -->

---

## 📄 License

<!-- Choose a license (e.g. MIT) or state that this is a coursework project. -->
This project was created for educational purposes.
