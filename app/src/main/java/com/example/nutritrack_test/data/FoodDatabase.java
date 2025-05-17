package com.example.nutritrack_test.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDatabase {
    private static FoodDatabase instance;
    private Map<String, List<FoodItem>> foodsByRegion;
    private Map<String, List<FoodItem>> foodsByType;
    private List<FoodItem> allFoods;
    
    private FoodDatabase() {
        foodsByRegion = new HashMap<>();
        foodsByType = new HashMap<>();
        allFoods = new ArrayList<>();
        
        initializeDatabase();
    }
    
    public static synchronized FoodDatabase getInstance() {
        if (instance == null) {
            instance = new FoodDatabase();
        }
        return instance;
    }
    
    public List<FoodItem> getAllFoods() {
        return allFoods;
    }
    
    public List<FoodItem> getFoodsByRegion(String region) {
        return foodsByRegion.getOrDefault(region, new ArrayList<>());
    }
    
    public List<FoodItem> getFoodsByType(String type) {
        return foodsByType.getOrDefault(type, new ArrayList<>());
    }
    
    public List<FoodItem> getFoodsByDietaryPreference(String preference) {
        List<FoodItem> filteredFoods = new ArrayList<>();
        
        for (FoodItem food : allFoods) {
            if (preference.equals("No Restrictions") || 
                (preference.equals("Vegetarian") && food.isVegetarian()) ||
                (preference.equals("Vegan") && food.isVegan()) ||
                (preference.equals("Jain") && food.isJainCompatible()) ||
                (food.getRegion().toLowerCase().contains(preference.toLowerCase()))) {
                filteredFoods.add(food);
            }
        }
        
        return filteredFoods;
    }
    
    private void addFood(FoodItem food) {
        allFoods.add(food);
        
        // Add to region map
        List<FoodItem> regionFoods = foodsByRegion.getOrDefault(food.getRegion(), new ArrayList<>());
        regionFoods.add(food);
        foodsByRegion.put(food.getRegion(), regionFoods);
        
        // Add to type map
        List<FoodItem> typeFoods = foodsByType.getOrDefault(food.getMealType(), new ArrayList<>());
        typeFoods.add(food);
        foodsByType.put(food.getMealType(), typeFoods);
    }
    
    private void initializeDatabase() {
        // Load food items from data loaders
        for (FoodItem food : NorthIndianFoods.getFoods()) {
            addFood(food);
        }
        
        for (FoodItem food : SouthIndianFoods.getFoods()) {
            addFood(food);
        }
        
        for (FoodItem food : WestIndianFoods.getFoods()) {
            addFood(food);
        }
        
        for (FoodItem food : EastIndianFoods.getFoods()) {
            addFood(food);
        }
        
        for (FoodItem food : JainFoods.getFoods()) {
            addFood(food);
        }
    }
} 