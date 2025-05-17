package com.example.nutritrack_test;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nutritrack_test.models.FoodItem;
import com.example.nutritrack_test.models.MealLog;
import com.example.nutritrack_test.network.NetworkManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {

    public static final String EXTRA_FOOD_ITEM = "extra_food_item";
    
    private FoodItem foodItem;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        
        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Food Details");
        }
        
        // Initialize network manager
        networkManager = NetworkManager.getInstance(this);
        
        // Get food item from intent
        String foodItemJson = getIntent().getStringExtra(EXTRA_FOOD_ITEM);
        if (foodItemJson != null) {
            foodItem = new Gson().fromJson(foodItemJson, FoodItem.class);
            setupFoodDetails();
        } else {
            Toast.makeText(this, "Error loading food details", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        // Setup add to log button
        FloatingActionButton addToLogButton = findViewById(R.id.addToLogButton);
        addToLogButton.setOnClickListener(v -> addFoodToMealLog());
    }

    private void setupFoodDetails() {
        // Set food name and region
        TextView foodNameTextView = findViewById(R.id.foodName);
        TextView foodRegionTextView = findViewById(R.id.foodRegion);
        foodNameTextView.setText(foodItem.getName());
        foodRegionTextView.setText(foodItem.getRegion());
        
        // Set food image
        ImageView foodImageView = findViewById(R.id.foodImage);
        if (foodItem.getImageUrl() != null && !foodItem.getImageUrl().isEmpty()) {
            Glide.with(this)
                .load(foodItem.getImageUrl())
                .placeholder(R.drawable.food_placeholder)
                .into(foodImageView);
        }
        
        // Set diet type
        Chip dietTypeChip = findViewById(R.id.dietTypeChip);
        dietTypeChip.setText(foodItem.getDietType());
        
        // Set cooking time
        TextView cookingTimeTextView = findViewById(R.id.cookingTime);
        if (foodItem.getCookingTime() > 0) {
            cookingTimeTextView.setVisibility(View.VISIBLE);
            cookingTimeTextView.setText(foodItem.getCookingTime() + " mins");
        } else {
            cookingTimeTextView.setVisibility(View.GONE);
        }
        
        // Set description
        TextView descriptionTextView = findViewById(R.id.foodDescription);
        descriptionTextView.setText(foodItem.getDescription());
        
        // Set nutrition values
        TextView caloriesTextView = findViewById(R.id.caloriesValue);
        TextView proteinTextView = findViewById(R.id.proteinValue);
        TextView carbsTextView = findViewById(R.id.carbsValue);
        TextView fatTextView = findViewById(R.id.fatValue);
        TextView fiberTextView = findViewById(R.id.fiberValue);
        TextView sugarTextView = findViewById(R.id.sugarValue);
        
        caloriesTextView.setText(String.format(Locale.getDefault(), "%.0f kcal", foodItem.getCalories()));
        proteinTextView.setText(String.format(Locale.getDefault(), "%.1fg", foodItem.getProtein()));
        carbsTextView.setText(String.format(Locale.getDefault(), "%.1fg", foodItem.getCarbs()));
        fatTextView.setText(String.format(Locale.getDefault(), "%.1fg", foodItem.getFat()));
        fiberTextView.setText(String.format(Locale.getDefault(), "%.1fg", foodItem.getFiber()));
        sugarTextView.setText(String.format(Locale.getDefault(), "%.1fg", foodItem.getSugar()));
        
        // Set micronutrients
        TextView sodiumTextView = findViewById(R.id.sodiumValue);
        TextView potassiumTextView = findViewById(R.id.potassiumValue);
        TextView vitaminATextView = findViewById(R.id.vitaminAValue);
        TextView vitaminCTextView = findViewById(R.id.vitaminCValue);
        TextView calciumTextView = findViewById(R.id.calciumValue);
        TextView ironTextView = findViewById(R.id.ironValue);
        
        sodiumTextView.setText(String.format(Locale.getDefault(), "%.0fmg", foodItem.getSodium()));
        potassiumTextView.setText(String.format(Locale.getDefault(), "%.0fmg", foodItem.getPotassium()));
        vitaminATextView.setText(String.format(Locale.getDefault(), "%.0f%%", foodItem.getVitaminA()));
        vitaminCTextView.setText(String.format(Locale.getDefault(), "%.0f%%", foodItem.getVitaminC()));
        calciumTextView.setText(String.format(Locale.getDefault(), "%.0f%%", foodItem.getCalcium()));
        ironTextView.setText(String.format(Locale.getDefault(), "%.0f%%", foodItem.getIron()));
        
        // Set ingredients and preparation steps
        TextView ingredientsTextView = findViewById(R.id.ingredientsText);
        TextView preparationStepsTextView = findViewById(R.id.preparationStepsText);
        
        if (foodItem.getIngredients() != null && !foodItem.getIngredients().isEmpty()) {
            ingredientsTextView.setText(foodItem.getIngredients());
        } else {
            ingredientsTextView.setText("No ingredients information available");
        }
        
        if (foodItem.getPreparationSteps() != null && !foodItem.getPreparationSteps().isEmpty()) {
            preparationStepsTextView.setText(foodItem.getPreparationSteps());
        } else {
            preparationStepsTextView.setText("No preparation steps available");
        }
    }
    
    private void addFoodToMealLog() {
        // Create a new meal log entry
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        
        // Create MealLog with required parameters
        MealLog mealLog = new MealLog(
            0,  // log_id - set to 0 for new entries
            1,  // user_id - using default user ID 1
            currentDate,
            foodItem.getMealType(),
            foodItem.getName(),
            1.0f, // default quantity
            (float)foodItem.getCalories(),
            (float)foodItem.getProtein(),
            (float)foodItem.getCarbs(),
            (float)foodItem.getFat(),
            ""   // no notes
        );
        
        // You would normally save this to your database or send to server
        // For now, just show a confirmation message
        Snackbar.make(findViewById(android.R.id.content), 
                      "Added " + foodItem.getName() + " to your meal log", 
                      Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 