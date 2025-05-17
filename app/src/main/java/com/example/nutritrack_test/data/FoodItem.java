package com.example.nutritrack_test.data;

public class FoodItem {
    private String name;
    private String region;
    private String mealType; // breakfast, lunch, dinner, snack
    private float calories;
    private float protein;
    private float carbs;
    private float fat;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isJainCompatible;
    private String description;
    
    public FoodItem(String name, String region, String mealType, 
                   float calories, float protein, float carbs, float fat, 
                   boolean isVegetarian, boolean isVegan, boolean isJainCompatible, 
                   String description) {
        this.name = name;
        this.region = region;
        this.mealType = mealType;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.isVegetarian = isVegetarian;
        this.isVegan = isVegan;
        this.isJainCompatible = isJainCompatible;
        this.description = description;
    }
    
    // Getters
    public String getName() { return name; }
    public String getRegion() { return region; }
    public String getMealType() { return mealType; }
    public float getCalories() { return calories; }
    public float getProtein() { return protein; }
    public float getCarbs() { return carbs; }
    public float getFat() { return fat; }
    public boolean isVegetarian() { return isVegetarian; }
    public boolean isVegan() { return isVegan; }
    public boolean isJainCompatible() { return isJainCompatible; }
    public String getDescription() { return description; }
} 