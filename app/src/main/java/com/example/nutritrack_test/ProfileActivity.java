package com.example.nutritrack_test;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.nutritrack_test.network.NetworkManager;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import java.text.DecimalFormat;
import android.widget.LinearLayout;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private TextInputEditText heightInput, weightInput, targetCaloriesInput, targetProteinInput;
    private TextView bmiResultTextView;
    private ChipGroup dietPreferencesChipGroup;
    private MaterialButton generatePlanButton, calculateBmiButton;
    private CircularProgressIndicator progressIndicator;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Add try-catch block to catch any exceptions during layout inflation
        try {
            setContentView(R.layout.activity_profile);
        } catch (Exception e) {
            Log.e(TAG, "Error setting content view", e);
            Toast.makeText(this, "Error initializing UI: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); // Close the activity if layout inflation fails
            return;
        }

        try {
            // Setup toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }

            // Initialize views
            heightInput = findViewById(R.id.heightInput);
            weightInput = findViewById(R.id.weightInput);
            bmiResultTextView = findViewById(R.id.bmiResult);
            calculateBmiButton = findViewById(R.id.calculateBmiButton);
            dietPreferencesChipGroup = findViewById(R.id.dietPreferencesChipGroup);
            targetCaloriesInput = findViewById(R.id.targetCaloriesInput);
            targetProteinInput = findViewById(R.id.targetProteinInput);
            generatePlanButton = findViewById(R.id.generatePlanButton);
            progressIndicator = findViewById(R.id.progressIndicator);

            // Initialize NetworkManager
            networkManager = NetworkManager.getInstance(this);
            
            // Ensure chips are properly initialized
            ensureDietPreferenceChips();

            // Setup BMI calculation if views are available
            if (calculateBmiButton != null && heightInput != null && weightInput != null) {
                setupBmiCalculation();
            }

            // Setup generate plan button if available
            if (generatePlanButton != null) {
                generatePlanButton.setOnClickListener(v -> generateDietPlan());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing ProfileActivity", e);
            Toast.makeText(this, "Error initializing profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            
            // Don't finish the activity, try to continue with limited functionality
            // Create a simple layout if the main layout failed
            if (findViewById(R.id.toolbar) == null) {
                LinearLayout rootLayout = new LinearLayout(this);
                rootLayout.setOrientation(LinearLayout.VERTICAL);
                rootLayout.setPadding(16, 16, 16, 16);
                
                TextView errorText = new TextView(this);
                errorText.setText("Error loading profile. Please try again later.");
                errorText.setTextSize(18);
                
                Button backButton = new Button(this);
                backButton.setText("Go Back");
                backButton.setOnClickListener(v -> finish());
                
                rootLayout.addView(errorText);
                rootLayout.addView(backButton);
                
                setContentView(rootLayout);
            }
        }
    }

    private void setupBmiCalculation() {
        calculateBmiButton.setOnClickListener(v -> calculateBmi());

        // Auto-calculate BMI when both height and weight are entered
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(heightInput.getText()) && !TextUtils.isEmpty(weightInput.getText())) {
                    calculateBmi();
                }
            }
        };

        heightInput.addTextChangedListener(textWatcher);
        weightInput.addTextChangedListener(textWatcher);
    }

    private void calculateBmi() {
        try {
            double height = Double.parseDouble(heightInput.getText().toString()) / 100; // convert cm to m
            double weight = Double.parseDouble(weightInput.getText().toString());
            
            if (height <= 0 || weight <= 0) {
                bmiResultTextView.setText("Invalid input");
                return;
            }
            
            double bmi = weight / (height * height);
            DecimalFormat df = new DecimalFormat("#.##");
            String bmiValue = df.format(bmi);
            String bmiCategory = getBmiCategory(bmi);
            
            bmiResultTextView.setText(bmiValue + " (" + bmiCategory + ")");
        } catch (NumberFormatException e) {
            bmiResultTextView.setText("Invalid input");
        } catch (Exception e) {
            Log.e(TAG, "Error calculating BMI", e);
            bmiResultTextView.setText("Error calculating BMI");
        }
    }
    
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private void generateDietPlan() {
        if (!isNetworkAvailable()) {
            Snackbar.make(generatePlanButton, 
                "Network is not available. Please check your internet connection.", 
                Snackbar.LENGTH_LONG).show();
            return;
        }

        String selectedDiet = getSelectedDiet();
        String targetCalories = targetCaloriesInput.getText().toString();

        if (selectedDiet == null) {
            Snackbar.make(generatePlanButton, "Please select a diet preference", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (targetCalories.isEmpty()) {
            Snackbar.make(generatePlanButton, "Please enter target calories", Snackbar.LENGTH_SHORT).show();
            return;
        }

        try {
            progressIndicator.setVisibility(View.VISIBLE);
            
            JSONObject requestData = new JSONObject();
            requestData.put("diet_type", selectedDiet);
            requestData.put("target_calories", Integer.parseInt(targetCalories));
            
            if (!TextUtils.isEmpty(targetProteinInput.getText())) {
                requestData.put("target_protein", Integer.parseInt(targetProteinInput.getText().toString()));
            }

            networkManager.generateMealPlan(requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressIndicator.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, 
                            "Diet plan generated successfully", 
                            Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressIndicator.setVisibility(View.GONE);
                        String errorMessage = "Network Error";
                        if (error.networkResponse != null) {
                            errorMessage += " - Status Code: " + error.networkResponse.statusCode;
                        }
                        Log.e(TAG, "Error generating plan: " + errorMessage, error);
                        Snackbar.make(generatePlanButton,
                            "Failed to generate diet plan: " + errorMessage,
                            Snackbar.LENGTH_LONG).show();
                    }
                });
        } catch (Exception e) {
            progressIndicator.setVisibility(View.GONE);
            Log.e(TAG, "Error generating diet plan", e);
            Snackbar.make(generatePlanButton, "Error generating diet plan: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getSelectedDiet() {
        if (dietPreferencesChipGroup == null) {
            return "vegetarian"; // Default value if chip group is null
        }
        
        int selectedChipId = dietPreferencesChipGroup.getCheckedChipId();
        if (selectedChipId != -1) {
            Chip selectedChip = findViewById(selectedChipId);
            if (selectedChip != null) {
                return selectedChip.getText().toString().toLowerCase();
            }
        }
        return null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Ensures that chip group has diet preference chips if missing from XML
     */
    private void ensureDietPreferenceChips() {
        if (dietPreferencesChipGroup == null) {
            Log.e(TAG, "Diet preferences chip group is null");
            return;
        }
        
        // If the chip group is empty, add chips programmatically
        if (dietPreferencesChipGroup.getChildCount() == 0) {
            Log.d(TAG, "Adding diet preference chips programmatically");
            
            // Create and add Vegetarian chip
            Chip vegetarianChip = new Chip(this);
            vegetarianChip.setId(View.generateViewId());
            vegetarianChip.setText("Vegetarian");
            vegetarianChip.setCheckable(true);
            vegetarianChip.setCheckedIconVisible(true);
            try {
                vegetarianChip.setChipIconResource(R.drawable.ic_vegetarian);
            } catch (Exception e) {
                Log.w(TAG, "Could not set vegetarian icon", e);
            }
            dietPreferencesChipGroup.addView(vegetarianChip);
            
            // Create and add Vegan chip
            Chip veganChip = new Chip(this);
            veganChip.setId(View.generateViewId());
            veganChip.setText("Vegan");
            veganChip.setCheckable(true);
            veganChip.setCheckedIconVisible(true);
            try {
                veganChip.setChipIconResource(R.drawable.ic_vegan);
            } catch (Exception e) {
                Log.w(TAG, "Could not set vegan icon", e);
            }
            dietPreferencesChipGroup.addView(veganChip);
            
            // Create and add Jain chip
            Chip jainChip = new Chip(this);
            jainChip.setId(View.generateViewId());
            jainChip.setText("Jain");
            jainChip.setCheckable(true);
            jainChip.setCheckedIconVisible(true);
            try {
                jainChip.setChipIconResource(R.drawable.ic_jain);
            } catch (Exception e) {
                Log.w(TAG, "Could not set jain icon", e);
            }
            dietPreferencesChipGroup.addView(jainChip);
            
            // Create and add Non-Vegetarian chip
            Chip nonVegChip = new Chip(this);
            nonVegChip.setId(View.generateViewId());
            nonVegChip.setText("Non-Vegetarian");
            nonVegChip.setCheckable(true);
            nonVegChip.setCheckedIconVisible(true);
            try {
                nonVegChip.setChipIconResource(R.drawable.ic_non_veg);
            } catch (Exception e) {
                Log.w(TAG, "Could not set non-veg icon", e);
            }
            dietPreferencesChipGroup.addView(nonVegChip);
            
            // Create and add Keto chip
            Chip ketoChip = new Chip(this);
            ketoChip.setId(View.generateViewId());
            ketoChip.setText("Keto");
            ketoChip.setCheckable(true);
            ketoChip.setCheckedIconVisible(true);
            dietPreferencesChipGroup.addView(ketoChip);
        }
    }
} 