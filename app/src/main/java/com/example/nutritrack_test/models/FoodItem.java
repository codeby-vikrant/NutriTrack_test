package com.example.nutritrack_test.models;

import com.google.gson.annotations.SerializedName;

public class FoodItem {
    @SerializedName("name")
    private String name;
    
    @SerializedName("region")
    private String region;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("diet_type")
    private String dietType;
    
    @SerializedName("calories")
    private double calories;
    
    @SerializedName("protein")
    private double protein;
    
    @SerializedName("carbs")
    private double carbs;
    
    @SerializedName("fat")
    private double fat;

    @SerializedName("meal_type")
    private String mealType;
    
    @SerializedName("is_completed")
    private boolean isCompleted;
    
    @SerializedName("fiber")
    private double fiber;
    
    @SerializedName("sugar")
    private double sugar;
    
    @SerializedName("sodium")
    private double sodium;
    
    @SerializedName("potassium")
    private double potassium;
    
    @SerializedName("vitamin_a")
    private double vitaminA;
    
    @SerializedName("vitamin_c")
    private double vitaminC;
    
    @SerializedName("calcium")
    private double calcium;
    
    @SerializedName("iron")
    private double iron;
    
    @SerializedName("cooking_time")
    private int cookingTime;
    
    @SerializedName("ingredients")
    private String ingredients;
    
    @SerializedName("preparation_steps")
    private String preparationSteps;
    
    @SerializedName("image_url")
    private String imageUrl;

    public FoodItem(String name, String region, String description, String dietType, 
                   double calories, double protein, double carbs, double fat, String mealType) {
        this.name = name;
        this.region = region;
        this.description = description;
        this.dietType = dietType;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.mealType = mealType;
        this.isCompleted = false;
        
        // Default values for new fields
        this.fiber = 0;
        this.sugar = 0;
        this.sodium = 0;
        this.potassium = 0;
        this.vitaminA = 0;
        this.vitaminC = 0;
        this.calcium = 0;
        this.iron = 0;
        this.cookingTime = 0;
        this.ingredients = "";
        this.preparationSteps = "";
        this.imageUrl = "";
    }

    // Constructor with all new fields
    public FoodItem(String name, String region, String description, String dietType, 
                   double calories, double protein, double carbs, double fat, String mealType,
                   double fiber, double sugar, double sodium, double potassium,
                   double vitaminA, double vitaminC, double calcium, double iron,
                   int cookingTime, String ingredients, String preparationSteps, String imageUrl) {
        this.name = name;
        this.region = region;
        this.description = description;
        this.dietType = dietType;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.mealType = mealType;
        this.isCompleted = false;
        this.fiber = fiber;
        this.sugar = sugar;
        this.sodium = sodium;
        this.potassium = potassium;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.calcium = calcium;
        this.iron = iron;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
        this.preparationSteps = preparationSteps;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getRegion() { return region; }
    public String getDescription() { return description; }
    public String getDietType() { return dietType; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
    public String getMealType() { return mealType; }
    public boolean isCompleted() { return isCompleted; }
    public double getFiber() { return fiber; }
    public double getSugar() { return sugar; }
    public double getSodium() { return sodium; }
    public double getPotassium() { return potassium; }
    public double getVitaminA() { return vitaminA; }
    public double getVitaminC() { return vitaminC; }
    public double getCalcium() { return calcium; }
    public double getIron() { return iron; }
    public int getCookingTime() { return cookingTime; }
    public String getIngredients() { return ingredients; }
    public String getPreparationSteps() { return preparationSteps; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setRegion(String region) { this.region = region; }
    public void setDescription(String description) { this.description = description; }
    public void setDietType(String dietType) { this.dietType = dietType; }
    public void setCalories(double calories) { this.calories = calories; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public void setFat(double fat) { this.fat = fat; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public void setCompleted(boolean completed) { this.isCompleted = completed; }
    public void setFiber(double fiber) { this.fiber = fiber; }
    public void setSugar(double sugar) { this.sugar = sugar; }
    public void setSodium(double sodium) { this.sodium = sodium; }
    public void setPotassium(double potassium) { this.potassium = potassium; }
    public void setVitaminA(double vitaminA) { this.vitaminA = vitaminA; }
    public void setVitaminC(double vitaminC) { this.vitaminC = vitaminC; }
    public void setCalcium(double calcium) { this.calcium = calcium; }
    public void setIron(double iron) { this.iron = iron; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
} 