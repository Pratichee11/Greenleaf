package com.example.greenleaf;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkModeSwitch;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Load the saved state of the switch
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);  // false is the default value
        darkModeSwitch.setChecked(isDarkMode);

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state of the switch
                editor.putBoolean("darkMode", isChecked);
                editor.apply();

                // Apply the dark mode (you'll need to implement this)
                applyDarkMode(isChecked);
            }
        });
    }

    private void applyDarkMode(boolean isDarkMode) {
        // Implement your dark mode logic here.  This might involve:
        // 1.  Setting the app's theme (if you're using themes).
        // 2.  Changing the colors of views programmatically.
        // For example:
        if (isDarkMode) {
            // Set dark theme
            //setTheme(R.style.DarkTheme);
        } else {
            // Set light theme
            //setTheme(R.style.LightTheme);
        }
        // Recreate the activity to apply the theme changes.
        //recreate();
    }
}