package com.example.nutritrack_test.models;

public class MealLog {
    private int log_id;
    private int user_id;
    private String date;
    private String meal_type;
    private String food_name;
    private float quantity;
    private float calories;
    private float protein_g;
    private float carbs_g;
    private float fat_g;
    private String notes;

    public MealLog(int log_id, int user_id, String date, String meal_type, String food_name,
                  float quantity, float calories, float protein_g, float carbs_g, float fat_g,
                  String notes) {
        this.log_id = log_id;
        this.user_id = user_id;
        this.date = date;
        this.meal_type = meal_type;
        this.food_name = food_name;
        this.quantity = quantity;
        this.calories = calories;
        this.protein_g = protein_g;
        this.carbs_g = carbs_g;
        this.fat_g = fat_g;
        this.notes = notes;
    }

    // Getters and setters
    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getProtein_g() {
        return protein_g;
    }

    public void setProtein_g(float protein_g) {
        this.protein_g = protein_g;
    }

    public float getCarbs_g() {
        return carbs_g;
    }

    public void setCarbs_g(float carbs_g) {
        this.carbs_g = carbs_g;
    }

    public float getFat_g() {
        return fat_g;
    }

    public void setFat_g(float fat_g) {
        this.fat_g = fat_g;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
