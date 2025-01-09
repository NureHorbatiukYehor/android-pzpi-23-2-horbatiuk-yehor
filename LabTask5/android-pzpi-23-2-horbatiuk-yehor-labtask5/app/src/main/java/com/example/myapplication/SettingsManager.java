package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREF_NAME = "app_settings";
    private static final String KEY_THEME = "theme_mode";
    private static final String KEY_FONT_SIZE = "font_size";

    private SharedPreferences preferences;

    public SettingsManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setDarkTheme(boolean isDark) {
        preferences.edit().putBoolean(KEY_THEME, isDark).apply();
    }

    public boolean isDarkTheme() {
        return preferences.getBoolean(KEY_THEME, false);
    }

    public void setFontSize(int size) {
        preferences.edit().putInt(KEY_FONT_SIZE, size).apply();
    }

    public int getFontSize() {
        return preferences.getInt(KEY_FONT_SIZE, 1); // 0 - small, 1 - medium, 2 - large
    }

    public float getFontScale() {
        switch (getFontSize()) {
            case 0: return 0.8f;
            case 2: return 1.2f;
            default: return 1.0f;
        }
    }
}

