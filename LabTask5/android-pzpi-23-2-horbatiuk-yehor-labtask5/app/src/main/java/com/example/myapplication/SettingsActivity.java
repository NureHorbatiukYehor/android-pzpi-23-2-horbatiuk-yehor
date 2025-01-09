package com.example.myapplication;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;

public class SettingsActivity extends AppCompatActivity {
    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingsManager = new SettingsManager(this);
        if (settingsManager.isDarkTheme()) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch themeSwitch = findViewById(R.id.theme_switch);
        RadioGroup fontSizeGroup = findViewById(R.id.font_size_group);


        themeSwitch.setChecked(settingsManager.isDarkTheme());
        fontSizeGroup.check(getFontSizeRadioId(settingsManager.getFontSize()));

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setDarkTheme(isChecked);

            recreate();
        });

        fontSizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int fontSize = getFontSizeValue(checkedId);
            settingsManager.setFontSize(fontSize);

            recreate();
        });
    }

    @Override
    public void recreate() {
        final Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        super.recreate();
        overridePendingTransition(0, 0);
    }

    private int getFontSizeRadioId(int fontSize) {
        switch (fontSize) {
            case 0: return R.id.radio_small;
            case 2: return R.id.radio_large;
            default: return R.id.radio_medium;
        }
    }

    private int getFontSizeValue(int radioId) {
        if (radioId == R.id.radio_small) return 0;
        if (radioId == R.id.radio_large) return 2;
        return 1;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("settings_changed", true);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}
