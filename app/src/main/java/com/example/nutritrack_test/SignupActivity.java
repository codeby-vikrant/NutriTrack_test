package com.example.nutritrack_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {
    private TextInputEditText usernameInput;
    private TextInputEditText ageInput;
    private AutoCompleteTextView genderDropdown;
    private TextInputEditText heightInput;
    private TextInputEditText weightInput;
    private AutoCompleteTextView activityLevelDropdown;
    private AutoCompleteTextView goalDropdown;
    private AutoCompleteTextView dietaryPreferenceDropdown;
    private TextInputEditText allergiesInput;
    private MaterialButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setupDropdowns();
        setupSubmitButton();
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.username_input);
        ageInput = findViewById(R.id.age_input);
        genderDropdown = findViewById(R.id.gender_dropdown);
        heightInput = findViewById(R.id.height_input);
        weightInput = findViewById(R.id.weight_input);
        activityLevelDropdown = findViewById(R.id.activity_level_dropdown);
        goalDropdown = findViewById(R.id.goal_dropdown);
        dietaryPreferenceDropdown = findViewById(R.id.dietary_preference_dropdown);
        allergiesInput = findViewById(R.id.allergies_input);
        submitButton = findViewById(R.id.submit_button);
    }

    private void setupDropdowns() {
        // Gender options
        String[] genders = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders);
        genderDropdown.setAdapter(genderAdapter);

        // Activity level options
        String[] activityLevels = new String[]{
                "Sedentary", "Lightly Active", "Moderately Active",
                "Very Active", "Extra Active"
        };
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, activityLevels);
        activityLevelDropdown.setAdapter(activityAdapter);

        // Goal options
        String[] goals = new String[]{
                "Weight Loss", "Weight Maintenance", "Weight Gain",
                "Muscle Gain", "Better Health"
        };
        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, goals);
        goalDropdown.setAdapter(goalAdapter);

        // Dietary preference options
        String[] dietaryPreferences = new String[]{
                "No Restriction", "Vegetarian", "Vegan", "Jain",
                "Gluten-Free", "Keto"
        };
        ArrayAdapter<String> dietaryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, dietaryPreferences);
        dietaryPreferenceDropdown.setAdapter(dietaryAdapter);
    }

    private void setupSubmitButton() {
        submitButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUserProfile();
                startMainActivity();
            }
        });
    }

    private boolean validateInputs() {
        // Add validation logic here
        return true; // Temporary return for now
    }

    private void saveUserProfile() {
        // Add logic to save user profile
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
} 