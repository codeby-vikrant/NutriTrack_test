package com.example.nutritrack_test.models;

public class MealPlanRequest {
    private String dietaryPreference;
    private String allergies;
    private String wakeTime;
    private String sleepTime;
    private double tdee;

    public MealPlanRequest(String dietaryPreference, String allergies, String wakeTime, String sleepTime, double tdee) {
        this.dietaryPreference = dietaryPreference;
        this.allergies = allergies;
        this.wakeTime = wakeTime;
        this.sleepTime = sleepTime;
        this.tdee = tdee;
    }

    // Getters
    public String getDietaryPreference() { return dietaryPreference; }
    public String getAllergies() { return allergies; }
    public String getWakeTime() { return wakeTime; }
    public String getSleepTime() { return sleepTime; }
    public double getTdee() { return tdee; }
} 