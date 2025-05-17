package com.example.nutritrack_test.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int userId;
    
    @SerializedName("username")
    private String username;
    
    @SerializedName("age")
    private int age;
    
    @SerializedName("gender")
    private String gender;
    
    @SerializedName("height_cm")
    private float height;
    
    @SerializedName("weight_kg")
    private float weight;
    
    @SerializedName("activity_level")
    private String activityLevel;
    
    @SerializedName("goal")
    private String goal;
    
    @SerializedName("dietary_preference")
    private String dietaryPreference;
    
    @SerializedName("allergies")
    private String allergies;
    
    @SerializedName("wake_time")
    private String wakeTime;
    
    @SerializedName("sleep_time")
    private String sleepTime;
    
    @SerializedName("bmr")
    private float bmr;
    
    @SerializedName("tdee")
    private float tdee;
    
    @SerializedName("protein_goal")
    private float proteinGoal;
    
    @SerializedName("carbs_goal")
    private float carbsGoal;
    
    @SerializedName("fat_goal")
    private float fatGoal;
    
    private MealPlan mealPlan;

    public User(String username, int age, String gender, float height, float weight,
                String activityLevel, String goal, String dietaryPreference, String allergies,
                String wakeTime, String sleepTime) {
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.dietaryPreference = dietaryPreference;
        this.allergies = allergies;
        this.wakeTime = wakeTime;
        this.sleepTime = sleepTime;
        calculateNutritionalGoals();
    }

    private void calculateNutritionalGoals() {
        // Calculate BMR (Mifflin-St Jeor Equation)
        bmr = (float) ((10 * weight) + (6.25 * height) - (5 * age));
        if (gender.equals("Male")) {
            bmr += 5;
        } else {
            bmr -= 161;
        }

        // Calculate TDEE based on activity level
        float activityMultiplier;
        switch (activityLevel) {
            case "Sedentary":
                activityMultiplier = 1.2f;
                break;
            case "Lightly Active":
                activityMultiplier = 1.375f;
                break;
            case "Moderately Active":
                activityMultiplier = 1.55f;
                break;
            case "Very Active":
                activityMultiplier = 1.725f;
                break;
            case "Extra Active":
                activityMultiplier = 1.9f;
                break;
            default:
                activityMultiplier = 1.2f;
        }
        tdee = bmr * activityMultiplier;

        // Adjust TDEE based on goal
        switch (goal) {
            case "Weight Loss":
                tdee -= 500; // 500 calorie deficit
                break;
            case "Weight Gain":
                tdee += 500; // 500 calorie surplus
                break;
            case "Muscle Gain":
                tdee += 300; // 300 calorie surplus
                break;
            default:
                // Weight Maintenance - no adjustment
                break;
        }

        // Calculate macro goals
        proteinGoal = weight * 1.8f; // 1.8g protein per kg of body weight
        float proteinCalories = proteinGoal * 4;
        float fatCalories = tdee * 0.25f; // 25% of calories from fat
        fatGoal = fatCalories / 9;
        float carbCalories = tdee - proteinCalories - fatCalories;
        carbsGoal = carbCalories / 4;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
    
    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
    
    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
    
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    
    public String getDietaryPreference() { return dietaryPreference; }
    public void setDietaryPreference(String dietaryPreference) { this.dietaryPreference = dietaryPreference; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getWakeTime() { return wakeTime; }
    public void setWakeTime(String wakeTime) { this.wakeTime = wakeTime; }
    
    public String getSleepTime() { return sleepTime; }
    public void setSleepTime(String sleepTime) { this.sleepTime = sleepTime; }
    
    public float getBmr() { return bmr; }
    public void setBmr(float bmr) { this.bmr = bmr; }
    
    public float getTdee() { return tdee; }
    public void setTdee(float tdee) { this.tdee = tdee; }
    
    public float getProteinGoal() { return proteinGoal; }
    public void setProteinGoal(float proteinGoal) { this.proteinGoal = proteinGoal; }
    
    public float getCarbsGoal() { return carbsGoal; }
    public void setCarbsGoal(float carbsGoal) { this.carbsGoal = carbsGoal; }
    
    public float getFatGoal() { return fatGoal; }
    public void setFatGoal(float fatGoal) { this.fatGoal = fatGoal; }
    
    public MealPlan getMealPlan() { return mealPlan; }
    public void setMealPlan(MealPlan mealPlan) { this.mealPlan = mealPlan; }
}
