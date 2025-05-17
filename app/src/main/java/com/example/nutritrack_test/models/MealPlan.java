package com.example.nutritrack_test.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public class MealPlan {
    @SerializedName("user_id")
    private String userId;
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("wake_time")
    private String wakeTime;
    
    @SerializedName("sleep_time")
    private String sleepTime;
    
    @SerializedName("plan")
    private Plan plan;
    
    private List<Meal> meals;

    public MealPlan() {
        this.meals = new ArrayList<>();
    }

    public MealPlan(List<Meal> meals) {
        this.meals = meals != null ? meals : new ArrayList<>();
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public static class Plan {
        private Meal breakfast;
        @SerializedName("morning_snack")
        private Meal morningSnack;
        private Meal lunch;
        @SerializedName("afternoon_snack")
        private Meal afternoonSnack;
        private Meal dinner;
        
        public Meal getBreakfast() { return breakfast; }
        public Meal getMorningSnack() { return morningSnack; }
        public Meal getLunch() { return lunch; }
        public Meal getAfternoonSnack() { return afternoonSnack; }
        public Meal getDinner() { return dinner; }
        
        public float getTotalCalories() {
            float total = 0;
            if (breakfast != null) total += breakfast.getCalories();
            if (morningSnack != null) total += morningSnack.getCalories();
            if (lunch != null) total += lunch.getCalories();
            if (afternoonSnack != null) total += afternoonSnack.getCalories();
            if (dinner != null) total += dinner.getCalories();
            return total;
        }
        
        public float getTotalProtein() {
            float total = 0;
            if (breakfast != null) total += breakfast.getProtein();
            if (morningSnack != null) total += morningSnack.getProtein();
            if (lunch != null) total += lunch.getProtein();
            if (afternoonSnack != null) total += afternoonSnack.getProtein();
            if (dinner != null) total += dinner.getProtein();
            return total;
        }
        
        public float getTotalCarbs() {
            float total = 0;
            if (breakfast != null) total += breakfast.getCarbs();
            if (morningSnack != null) total += morningSnack.getCarbs();
            if (lunch != null) total += lunch.getCarbs();
            if (afternoonSnack != null) total += afternoonSnack.getCarbs();
            if (dinner != null) total += dinner.getCarbs();
            return total;
        }
        
        public float getTotalFat() {
            float total = 0;
            if (breakfast != null) total += breakfast.getFat();
            if (morningSnack != null) total += morningSnack.getFat();
            if (lunch != null) total += lunch.getFat();
            if (afternoonSnack != null) total += afternoonSnack.getFat();
            if (dinner != null) total += dinner.getFat();
            return total;
        }
    }
    
    public static class Meal {
        private String name;
        private String time;
        private float calories;
        private float protein;
        private float carbs;
        private float fat;

        public Meal() {
            // Default constructor required for API deserialization
        }

        public Meal(String name, String time, float calories, float protein, float carbs, float fat) {
            this.name = name;
            this.time = time;
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getCalories() {
            return calories;
        }

        public void setCalories(float calories) {
            this.calories = calories;
        }

        public float getProtein() {
            return protein;
        }

        public void setProtein(float protein) {
            this.protein = protein;
        }

        public float getCarbs() {
            return carbs;
        }

        public void setCarbs(float carbs) {
            this.carbs = carbs;
        }

        public float getFat() {
            return fat;
        }

        public void setFat(float fat) {
            this.fat = fat;
        }
    }
    
    public String getUserId() { return userId; }
    public String getDate() { return date; }
    public String getWakeTime() { return wakeTime; }
    public String getSleepTime() { return sleepTime; }
    public Plan getPlan() { return plan; }
    
    public float getTotalCalories() { return plan != null ? plan.getTotalCalories() : 0; }
    public float getTotalProtein() { return plan != null ? plan.getTotalProtein() : 0; }
    public float getTotalCarbs() { return plan != null ? plan.getTotalCarbs() : 0; }
    public float getTotalFat() { return plan != null ? plan.getTotalFat() : 0; }
    
    public Meal getBreakfast() { return plan != null ? plan.getBreakfast() : null; }
    public Meal getMorningSnack() { return plan != null ? plan.getMorningSnack() : null; }
    public Meal getLunch() { return plan != null ? plan.getLunch() : null; }
    public Meal getAfternoonSnack() { return plan != null ? plan.getAfternoonSnack() : null; }
    public Meal getDinner() { return plan != null ? plan.getDinner() : null; }
}
