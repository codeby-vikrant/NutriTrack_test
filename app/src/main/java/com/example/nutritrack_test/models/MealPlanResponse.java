package com.example.nutritrack_test.models;

import java.util.List;

public class MealPlanResponse {
    private List<MealPlan.Meal> meals;

    public MealPlanResponse(List<MealPlan.Meal> meals) {
        this.meals = meals;
    }

    public List<MealPlan.Meal> getMeals() {
        return meals;
    }
} 