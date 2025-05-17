package com.example.nutritrack_test.models;

import java.util.ArrayList;
import java.util.List;

public class MealCategory {
    private String categoryName;
    private List<FoodItem> foodItems;
    private boolean isExpanded;

    public MealCategory(String categoryName) {
        this.categoryName = categoryName;
        this.foodItems = new ArrayList<>();
        this.isExpanded = true;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void addFoodItem(FoodItem item) {
        foodItems.add(item);
    }
} 