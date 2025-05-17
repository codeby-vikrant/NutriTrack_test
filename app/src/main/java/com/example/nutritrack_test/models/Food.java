package com.example.nutritrack_test.models;

import com.google.gson.annotations.SerializedName;

public class Food {
    @SerializedName("food_id")
    private int foodId;
    
    @SerializedName("food_name")
    private String foodName;
    
    @SerializedName("calories")
    private float calories;
    
    @SerializedName("protein_g")
    private float proteinG;
    
    @SerializedName("carbs_g")
    private float carbsG;
    
    @SerializedName("fat_g")
    private float fatG;
    
    @SerializedName("fiber_g")
    private float fiberG;
    
    @SerializedName("sugar_g")
    private float sugarG;
    
    @SerializedName("calcium_mg")
    private float calciumMg;
    
    @SerializedName("iron_mg")
    private float ironMg;
    
    @SerializedName("sodium_mg")
    private float sodiumMg;
    
    @SerializedName("portion_size")
    private String portionSize;
    
    @SerializedName("food_category")
    private String foodCategory;
    
    public Food() {
    }
    
    public Food(int foodId, String foodName, float calories, float proteinG, float carbsG, float fatG,
               float fiberG, float sugarG, float calciumMg, float ironMg, float sodiumMg,
               String portionSize, String foodCategory) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.calories = calories;
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
        this.fiberG = fiberG;
        this.sugarG = sugarG;
        this.calciumMg = calciumMg;
        this.ironMg = ironMg;
        this.sodiumMg = sodiumMg;
        this.portionSize = portionSize;
        this.foodCategory = foodCategory;
    }
    
    public int getFoodId() {
        return foodId;
    }
    
    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
    
    public String getFood_name() {
        return foodName;
    }
    
    public void setFood_name(String foodName) {
        this.foodName = foodName;
    }
    
    public float getCalories() {
        return calories;
    }
    
    public void setCalories(float calories) {
        this.calories = calories;
    }
    
    public float getProtein_g() {
        return proteinG;
    }
    
    public void setProtein_g(float proteinG) {
        this.proteinG = proteinG;
    }
    
    public float getCarbs_g() {
        return carbsG;
    }
    
    public void setCarbs_g(float carbsG) {
        this.carbsG = carbsG;
    }
    
    public float getFat_g() {
        return fatG;
    }
    
    public void setFat_g(float fatG) {
        this.fatG = fatG;
    }
    
    public float getFiber_g() {
        return fiberG;
    }
    
    public void setFiber_g(float fiberG) {
        this.fiberG = fiberG;
    }
    
    public float getSugar_g() {
        return sugarG;
    }
    
    public void setSugar_g(float sugarG) {
        this.sugarG = sugarG;
    }
    
    public float getCalcium_mg() {
        return calciumMg;
    }
    
    public void setCalcium_mg(float calciumMg) {
        this.calciumMg = calciumMg;
    }
    
    public float getIron_mg() {
        return ironMg;
    }
    
    public void setIron_mg(float ironMg) {
        this.ironMg = ironMg;
    }
    
    public float getSodium_mg() {
        return sodiumMg;
    }
    
    public void setSodium_mg(float sodiumMg) {
        this.sodiumMg = sodiumMg;
    }
    
    public String getPortion_size() {
        return portionSize;
    }
    
    public void setPortion_size(String portionSize) {
        this.portionSize = portionSize;
    }
    
    public String getFood_category() {
        return foodCategory;
    }
    
    public void setFood_category(String foodCategory) {
        this.foodCategory = foodCategory;
    }
}
