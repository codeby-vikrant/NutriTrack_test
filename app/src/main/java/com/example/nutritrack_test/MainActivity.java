package com.example.nutritrack_test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nutritrack_test.adapter.MealCategoryAdapter;
import com.example.nutritrack_test.models.MealCategory;
import com.example.nutritrack_test.models.FoodItem;
import com.example.nutritrack_test.network.NetworkManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MealCategoryAdapter adapter;
    private List<MealCategory> mealCategories;
    private List<MealCategory> originalMealCategories;
    private FloatingActionButton generatePlanButton;
    private CircularProgressIndicator loadingIndicator;
    private NetworkManager networkManager;

    
    // Search and filter views
    private TextInputEditText searchEditText;
    private MaterialButton toggleFiltersButton;
    private View filterOptionsLayout;
    private SeekBar caloriesSeekBar;
    private SeekBar proteinSeekBar;
    private TextView maxCaloriesLabel;
    private TextView minProteinLabel;
    private ChipGroup dietTypeChipGroup;
    private Chip chipVegetarian;
    private Chip chipVegan;
    private Chip chipGlutenFree;
    private MaterialButton applyFiltersButton;
    
    // Filter values
    private String searchQuery = "";
    private int maxCalories = 500;
    private int minProtein = 0;
    private List<String> selectedDietTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Set background drawable
        getWindow().setBackgroundDrawableResource(R.drawable.healthy_food_pattern);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        generatePlanButton = findViewById(R.id.generatePlanButton);
        loadingIndicator = findViewById(R.id.loading_indicator);
        
        // Initialize search and filter views
        initSearchAndFilterViews();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealCategories = initializeMealCategories();
        originalMealCategories = new ArrayList<>();
        adapter = new MealCategoryAdapter(mealCategories);
        recyclerView.setAdapter(adapter);

        // Setup generate plan button
        generatePlanButton.setOnClickListener(v -> generateNewDietPlan());

        // Initialize NetworkManager
        networkManager = NetworkManager.getInstance(this);

        // Load foods
        loadFoods();
    }
    
    private void initSearchAndFilterViews() {
        searchEditText = findViewById(R.id.searchEditText);
        toggleFiltersButton = findViewById(R.id.toggleFiltersButton);
        filterOptionsLayout = findViewById(R.id.filterOptionsLayout);
        caloriesSeekBar = findViewById(R.id.caloriesSeekBar);
        proteinSeekBar = findViewById(R.id.proteinSeekBar);
        maxCaloriesLabel = findViewById(R.id.maxCaloriesLabel);
        minProteinLabel = findViewById(R.id.minProteinLabel);
        dietTypeChipGroup = findViewById(R.id.dietTypeChipGroup);
        chipVegetarian = findViewById(R.id.chipVegetarian);
        chipVegan = findViewById(R.id.chipVegan);
        chipGlutenFree = findViewById(R.id.chipGlutenFree);
        applyFiltersButton = findViewById(R.id.applyFiltersButton);
        
        // Setup search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                searchQuery = s.toString().toLowerCase().trim();
                applyFilters();
            }
        });
        
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                applyFilters();
                return true;
            }
            return false;
        });
        
        // Setup filter toggle
        toggleFiltersButton.setOnClickListener(v -> {
            if (filterOptionsLayout.getVisibility() == View.VISIBLE) {
                filterOptionsLayout.setVisibility(View.GONE);
                toggleFiltersButton.setText("Show Filters");
            } else {
                filterOptionsLayout.setVisibility(View.VISIBLE);
                toggleFiltersButton.setText("Hide Filters");
            }
        });
        
        // Setup seekbars
        caloriesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxCalories = progress;
                maxCaloriesLabel.setText(String.format(Locale.getDefault(), "Max Calories: %d kcal", maxCalories));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        
        proteinSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minProtein = progress;
                minProteinLabel.setText(String.format(Locale.getDefault(), "Min Protein: %dg", minProtein));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        
        // Setup diet type chips
        chipVegetarian.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedDietTypes("Vegetarian", isChecked);
        });
        
        chipVegan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedDietTypes("Vegan", isChecked);
        });
        
        chipGlutenFree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedDietTypes("Gluten Free", isChecked);
        });
        
        // Apply filters button
        applyFiltersButton.setOnClickListener(v -> applyFilters());
    }
    
    private void updateSelectedDietTypes(String dietType, boolean isSelected) {
        if (isSelected && !selectedDietTypes.contains(dietType)) {
            selectedDietTypes.add(dietType);
        } else if (!isSelected && selectedDietTypes.contains(dietType)) {
            selectedDietTypes.remove(dietType);
        }
    }
    
    private void applyFilters() {
        // Create a deep copy of the original categories
        if (originalMealCategories.isEmpty()) {
            // First filter, save original data
            deepCopyMealCategories(mealCategories, originalMealCategories);
        }
        
        // Start with the original data
        List<MealCategory> filteredCategories = new ArrayList<>();
        deepCopyMealCategories(originalMealCategories, filteredCategories);
        
        // Apply filters to each category
        for (MealCategory category : filteredCategories) {
            List<FoodItem> filteredFoods = new ArrayList<>();
            
            for (FoodItem food : category.getFoodItems()) {
                boolean matchesSearch = searchQuery.isEmpty() || 
                    food.getName().toLowerCase().contains(searchQuery) ||
                    food.getDescription().toLowerCase().contains(searchQuery);
                
                boolean matchesCalories = food.getCalories() <= maxCalories;
                boolean matchesProtein = food.getProtein() >= minProtein;
                
                boolean matchesDietType = selectedDietTypes.isEmpty() || 
                    selectedDietTypes.contains(food.getDietType());
                
                if (matchesSearch && matchesCalories && matchesProtein && matchesDietType) {
                    filteredFoods.add(food);
                }
            }
            
            category.setFoodItems(filteredFoods);
        }
        
        // Update the adapter with filtered data
        mealCategories.clear();
        deepCopyMealCategories(filteredCategories, mealCategories);
        adapter.notifyDataSetChanged();
    }
    
    private void deepCopyMealCategories(List<MealCategory> source, List<MealCategory> destination) {
        destination.clear();
        for (MealCategory category : source) {
            MealCategory newCategory = new MealCategory(category.getCategoryName());
            for (FoodItem food : category.getFoodItems()) {
                newCategory.addFoodItem(food);
            }
            destination.add(newCategory);
        }
    }

    private List<MealCategory> initializeMealCategories() {
        List<MealCategory> categories = new ArrayList<>();
        categories.add(new MealCategory("Breakfast"));
        categories.add(new MealCategory("Lunch"));
        categories.add(new MealCategory("Evening Snacks"));
        categories.add(new MealCategory("Dinner"));
        return categories;
    }

    private void generateNewDietPlan() {
        // Show loading spinner with animation
        loadingIndicator.setVisibility(View.VISIBLE);
        loadingIndicator.setAlpha(0f);
        loadingIndicator.animate()
            .alpha(1f)
            .setDuration(300)
            .start();
        
        // Get currently selected diet type (if any)
        String dietType = "";
        if (!selectedDietTypes.isEmpty()) {
            dietType = selectedDietTypes.get(0);
        }
        
        // Create a final copy for use in inner class
        final String finalDietType = dietType;
        
        // Default to 1800 calories or use maxCalories as target
        int targetCalories = maxCalories > 0 ? maxCalories * 4 : 1800;
        
        // Make a toast to indicate the user we're generating a plan
        Toast.makeText(this, "Generating a " + targetCalories + " calorie" + 
                       (finalDietType.isEmpty() ? "" : " " + finalDietType) + " meal plan...", 
                       Toast.LENGTH_SHORT).show();
                       
        // Clear current categories
        for (MealCategory category : mealCategories) {
            category.getFoodItems().clear();
        }
        
        // Reload all foods from the server
        networkManager.getAllFoods(
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Process the response and distribute foods into meal categories
                    try {
                        // Create pools of foods by meal type
                        List<FoodItem> breakfastFoods = new ArrayList<>();
                        List<FoodItem> lunchFoods = new ArrayList<>();
                        List<FoodItem> snackFoods = new ArrayList<>();
                        List<FoodItem> dinnerFoods = new ArrayList<>();
                        
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject foodJson = response.getJSONObject(i);
                            
                            // Skip if diet type doesn't match
                            if (!finalDietType.isEmpty() && !foodJson.getString("diet_type").equalsIgnoreCase(finalDietType)) {
                                continue;
                            }
                            
                            FoodItem foodItem = new FoodItem(
                                foodJson.getString("name"),
                                foodJson.getString("region"),
                                foodJson.getString("description"),
                                foodJson.getString("diet_type"),
                                foodJson.getDouble("calories"),
                                foodJson.getDouble("protein"),
                                foodJson.getDouble("carbs"),
                                foodJson.getDouble("fat"),
                                foodJson.getString("meal_type")
                            );
                            
                            // Add additional nutritional information if available
                            if (foodJson.has("fiber")) foodItem.setFiber(foodJson.getDouble("fiber"));
                            if (foodJson.has("sugar")) foodItem.setSugar(foodJson.getDouble("sugar"));
                            if (foodJson.has("sodium")) foodItem.setSodium(foodJson.getDouble("sodium"));
                            if (foodJson.has("potassium")) foodItem.setPotassium(foodJson.getDouble("potassium"));
                            if (foodJson.has("vitamin_a")) foodItem.setVitaminA(foodJson.getDouble("vitamin_a"));
                            if (foodJson.has("vitamin_c")) foodItem.setVitaminC(foodJson.getDouble("vitamin_c"));
                            if (foodJson.has("calcium")) foodItem.setCalcium(foodJson.getDouble("calcium"));
                            if (foodJson.has("iron")) foodItem.setIron(foodJson.getDouble("iron"));
                            if (foodJson.has("cooking_time")) foodItem.setCookingTime(foodJson.getInt("cooking_time"));
                            if (foodJson.has("ingredients")) foodItem.setIngredients(foodJson.getString("ingredients"));
                            if (foodJson.has("preparation_steps")) foodItem.setPreparationSteps(foodJson.getString("preparation_steps"));
                            if (foodJson.has("image_url")) foodItem.setImageUrl(foodJson.getString("image_url"));
                            
                            // Sort by meal type
                            String mealType = foodJson.getString("meal_type").toLowerCase();
                            if (mealType.contains("breakfast")) {
                                breakfastFoods.add(foodItem);
                            } else if (mealType.contains("lunch")) {
                                lunchFoods.add(foodItem);
                            } else if (mealType.contains("snack")) {
                                snackFoods.add(foodItem);
                            } else if (mealType.contains("dinner")) {
                                dinnerFoods.add(foodItem);
                            }
                        }
                        
                        // Create a meal plan by picking foods from each category
                        // based on calorie targets
                        
                        // Distribute calories: 25% breakfast, 35% lunch, 10% snacks, 30% dinner
                        int breakfastCalories = (int)(targetCalories * 0.25);
                        int lunchCalories = (int)(targetCalories * 0.35);
                        int snackCalories = (int)(targetCalories * 0.10);
                        int dinnerCalories = (int)(targetCalories * 0.30);
                        
                        // Add foods to each category
                        addFoodsToPlan(breakfastFoods, "Breakfast", breakfastCalories);
                        addFoodsToPlan(lunchFoods, "Lunch", lunchCalories);
                        addFoodsToPlan(snackFoods, "Evening Snacks", snackCalories);
                        addFoodsToPlan(dinnerFoods, "Dinner", dinnerCalories);
                        
                        // Notify adapter of changes
                        adapter.notifyDataSetChanged();
                        
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error generating meal plan", e);
                        Toast.makeText(MainActivity.this, "Error generating meal plan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    
                    loadingIndicator.setVisibility(View.GONE);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingIndicator.setVisibility(View.GONE);
                    String errorMessage = "Network Error";
                    if (error.networkResponse != null) {
                        errorMessage += " - Status Code: " + error.networkResponse.statusCode;
                    }
                    Log.e("MainActivity", errorMessage, error);
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
    }
    
    private void addFoodsToPlan(List<FoodItem> foodPool, String categoryName, int targetCalories) {
        if (foodPool.isEmpty()) {
            return;
        }
        
        // Randomize the food pool
        java.util.Collections.shuffle(foodPool);
        
        // Find the category
        MealCategory category = null;
        for (MealCategory mc : mealCategories) {
            if (mc.getCategoryName().equalsIgnoreCase(categoryName)) {
                category = mc;
                break;
            }
        }
        
        if (category == null) {
            return;
        }
        
        // Add foods until we reach the target calories
        int currentCalories = 0;
        for (FoodItem food : foodPool) {
            if (currentCalories < targetCalories) {
                category.addFoodItem(food);
                currentCalories += food.getCalories();
            } else {
                break;
            }
        }
        
        // After adding all foods, notify the adapter of changes and animate the categories
        if (category != null && !category.getFoodItems().isEmpty()) {
            recyclerView.postDelayed(this::animatePlanItems, 200);
        }
    }

    private void loadFoods() {
        loadingIndicator.setVisibility(View.VISIBLE);
        
        Log.d("MainActivity", "Starting to load foods from server...");
        
        // Check server connection before making the request
        networkManager.checkServerConnection(new NetworkManager.ServerConnectionListener() {
            @Override
            public void onConnectionResult(boolean isConnected, String message) {
                if (isConnected) {
                    // Server is connected, proceed with loading foods
                    networkManager.getAllFoods(
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                loadingIndicator.setVisibility(View.GONE);
                                Log.d("MainActivity", "Received response: " + response.toString());
                                Toast.makeText(MainActivity.this, "Loaded " + response.length() + " food items from server", Toast.LENGTH_SHORT).show();
                                displayFoods(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loadingIndicator.setVisibility(View.GONE);
                                String errorMessage = "Network Error";
                                if (error.networkResponse != null) {
                                    errorMessage += " - Status Code: " + error.networkResponse.statusCode;
                                }
                                if (error.getMessage() != null) {
                                    errorMessage += "\nError: " + error.getMessage();
                                }
                                
                                // Add cause information
                                if (error.getCause() != null) {
                                    errorMessage += "\nCause: " + error.getCause().getMessage();
                                }
                                
                                Log.e("MainActivity", "Error loading foods: " + errorMessage, error);
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                
                                // Load some placeholder data if we can't connect to server
                                loadPlaceholderFoods();
                            }
                        });
                } else {
                    // Server is not connected
                    loadingIndicator.setVisibility(View.GONE);
                    Log.e("MainActivity", "Server connection error: " + message);
                    Toast.makeText(MainActivity.this, "Server connection error: " + message, Toast.LENGTH_LONG).show();
                    
                    // Load placeholder data since we can't connect to the server
                    loadPlaceholderFoods();
                }
            }
        });
    }
    
    /**
     * Loads placeholder food data when server connection fails
     */
    private void loadPlaceholderFoods() {
        Log.d("MainActivity", "Loading placeholder food data");
        
        // Create some placeholder food items
        for (MealCategory category : mealCategories) {
            // Clear existing items
            category.getFoodItems().clear();
            
            // Breakfast foods
            if (category.getCategoryName().equalsIgnoreCase("Breakfast")) {
                category.addFoodItem(new FoodItem("Idli Sambar", "South India", 
                    "Steamed rice cakes served with lentil soup", "Vegetarian", 
                    180, 5, 36, 0.5, "Breakfast"));
                    
                category.addFoodItem(new FoodItem("Poha", "Central India", 
                    "Flattened rice with spices and vegetables", "Vegetarian", 
                    270, 6, 45, 5, "Breakfast"));
            }
            // Lunch foods
            else if (category.getCategoryName().equalsIgnoreCase("Lunch")) {
                category.addFoodItem(new FoodItem("Rajma Chawal", "North India", 
                    "Kidney bean curry with rice", "Vegetarian", 
                    450, 15, 80, 5, "Lunch"));
                    
                category.addFoodItem(new FoodItem("Chole Bhature", "North India", 
                    "Chickpea curry with fried bread", "Vegetarian", 
                    650, 20, 90, 25, "Lunch"));
            }
            // Dinner foods
            else if (category.getCategoryName().equalsIgnoreCase("Dinner")) {
                category.addFoodItem(new FoodItem("Paneer Tikka", "North India", 
                    "Grilled cottage cheese with spices", "Vegetarian", 
                    350, 22, 12, 25, "Dinner"));
                    
                category.addFoodItem(new FoodItem("Dal Tadka", "North India", 
                    "Tempered lentil soup", "Vegetarian", 
                    250, 15, 30, 5, "Dinner"));
            }
            // Snack foods
            else if (category.getCategoryName().equalsIgnoreCase("Snacks")) {
                category.addFoodItem(new FoodItem("Samosa", "North India", 
                    "Fried pastry with spiced filling", "Vegetarian", 
                    250, 5, 30, 15, "Snack"));
                    
                category.addFoodItem(new FoodItem("Dhokla", "Gujarat", 
                    "Steamed chickpea flour cake", "Vegetarian", 
                    160, 8, 20, 5, "Snack"));
            }
        }
        
        // Update the adapter
        adapter.notifyDataSetChanged();
    }

    private void displayFoods(JSONArray foodsArray) {
        try {
            for (int i = 0; i < foodsArray.length(); i++) {
                JSONObject foodJson = foodsArray.getJSONObject(i);
                try {
                    FoodItem foodItem = new FoodItem(
                        foodJson.getString("name"),
                        foodJson.getString("region"),
                        foodJson.getString("description"),
                        foodJson.getString("diet_type"),
                        foodJson.getDouble("calories"),
                        foodJson.getDouble("protein"),
                        foodJson.getDouble("carbs"),
                        foodJson.getDouble("fat"),
                        foodJson.getString("meal_type")
                    );
                    
                    // Add additional nutritional information if available
                    if (foodJson.has("fiber")) foodItem.setFiber(foodJson.getDouble("fiber"));
                    if (foodJson.has("sugar")) foodItem.setSugar(foodJson.getDouble("sugar"));
                    if (foodJson.has("sodium")) foodItem.setSodium(foodJson.getDouble("sodium"));
                    if (foodJson.has("potassium")) foodItem.setPotassium(foodJson.getDouble("potassium"));
                    if (foodJson.has("vitamin_a")) foodItem.setVitaminA(foodJson.getDouble("vitamin_a"));
                    if (foodJson.has("vitamin_c")) foodItem.setVitaminC(foodJson.getDouble("vitamin_c"));
                    if (foodJson.has("calcium")) foodItem.setCalcium(foodJson.getDouble("calcium"));
                    if (foodJson.has("iron")) foodItem.setIron(foodJson.getDouble("iron"));
                    if (foodJson.has("cooking_time")) foodItem.setCookingTime(foodJson.getInt("cooking_time"));
                    if (foodJson.has("ingredients")) foodItem.setIngredients(foodJson.getString("ingredients"));
                    if (foodJson.has("preparation_steps")) foodItem.setPreparationSteps(foodJson.getString("preparation_steps"));
                    if (foodJson.has("image_url")) foodItem.setImageUrl(foodJson.getString("image_url"));
                    
                    // Add food item to appropriate category
                    String mealType = foodJson.getString("meal_type").toLowerCase();
                    for (MealCategory category : mealCategories) {
                        if (category.getCategoryName().toLowerCase().contains(mealType)) {
                            category.addFoodItem(foodItem);
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Log the problematic JSON object
                    Log.e("MainActivity", "Error parsing food item: " + foodJson.toString(), e);
                    continue; // Skip this item and continue with the next
                }
            }
            adapter.notifyDataSetChanged();
            
            // After a short delay to let the views render, animate them
            recyclerView.postDelayed(this::animatePlanItems, 200);
        } catch (Exception e) {
            Log.e("MainActivity", "Error parsing food array", e);
            Toast.makeText(this, "Error loading food data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void animatePlanItems() {
        // Apply a staggered animation to the meal categories
        for (int i = 0; i < mealCategories.size(); i++) {
            final View categoryView = recyclerView.getChildAt(i);
            if (categoryView != null) {
                categoryView.setTranslationX(-200);
                categoryView.setAlpha(0f);
                categoryView.animate()
                    .translationX(0)
                    .alpha(1f)
                    .setDuration(400)
                    .setStartDelay(i * 150) // Stagger the animations
                    .start();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            try {
                Intent intent = new Intent(this, ProfileActivity.class);
                // Add flags to create a new task
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                Log.e("MainActivity", "Error launching ProfileActivity", e);
                Toast.makeText(this, "Error opening profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}