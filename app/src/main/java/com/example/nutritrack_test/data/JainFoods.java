package com.example.nutritrack_test.data;

import java.util.ArrayList;
import java.util.List;

public class JainFoods {
    
    public static List<FoodItem> getFoods() {
        List<FoodItem> foods = new ArrayList<>();
        
        // Breakfast items
        foods.add(new FoodItem(
            "Jain Poha", "Jain", "Breakfast",
            220, 5, 40, 5, 
            true, true, true,
            "Flattened rice prepared without onions, garlic, and root vegetables"
        ));
        
        foods.add(new FoodItem(
            "Moong Dal Cheela", "Jain", "Breakfast",
            250, 10, 35, 8, 
            true, true, true,
            "Savory pancakes made from split green gram batter"
        ));
        
        foods.add(new FoodItem(
            "Sabudana Khichdi", "Jain", "Breakfast",
            320, 5, 55, 10, 
            true, true, true,
            "Tapioca pearls cooked with peanuts and spices"
        ));
        
        foods.add(new FoodItem(
            "Farali Dosa", "Jain", "Breakfast",
            280, 6, 45, 8, 
            true, true, true,
            "Dosa made with water chestnut flour and mashed potatoes"
        ));
        
        foods.add(new FoodItem(
            "Jain Upma", "Jain", "Breakfast",
            230, 6, 38, 6, 
            true, true, true,
            "Semolina preparation without onions, garlic, and root vegetables"
        ));
        
        foods.add(new FoodItem(
            "Sabudana Vada", "Jain", "Breakfast",
            180, 4, 30, 8, 
            true, true, true,
            "Deep-fried patties made with tapioca pearls and peanuts"
        ));
        
        // Lunch items
        foods.add(new FoodItem(
            "Jain Dal", "Jain", "Lunch",
            250, 12, 40, 6, 
            true, true, true,
            "Lentil soup prepared without onions, garlic, and root vegetables"
        ));
        
        foods.add(new FoodItem(
            "Jain Pulao", "Jain", "Lunch",
            320, 7, 60, 8, 
            true, true, true,
            "Fragrant rice with vegetables excluding onions, garlic, and root vegetables"
        ));
        
        foods.add(new FoodItem(
            "Lauki Sabzi", "Jain", "Lunch",
            180, 4, 20, 10, 
            true, true, true,
            "Bottle gourd curry without onions and garlic"
        ));
        
        foods.add(new FoodItem(
            "Turai Ki Sabzi", "Jain", "Lunch",
            160, 3, 18, 8, 
            true, true, true,
            "Ridge gourd curry prepared in Jain style"
        ));
        
        foods.add(new FoodItem(
            "Jain Kadhi", "Jain", "Lunch",
            220, 5, 25, 12, 
            true, true, true,
            "Yogurt-based curry without onions and garlic"
        ));
        
        foods.add(new FoodItem(
            "Jain Khichdi", "Jain", "Lunch",
            280, 10, 50, 6, 
            true, true, true,
            "Rice and lentil porridge without onions, garlic, and root vegetables"
        ));
        
        foods.add(new FoodItem(
            "Bhindi Masala", "Jain", "Lunch",
            200, 5, 18, 14, 
            true, true, true,
            "Okra curry prepared without onions and garlic"
        ));
        
        foods.add(new FoodItem(
            "Jain Pav Bhaji", "Jain", "Lunch",
            380, 8, 55, 15, 
            true, true, true,
            "Mashed vegetable curry without potatoes, onions, and garlic, served with bread rolls"
        ));
        
        // Dinner items
        foods.add(new FoodItem(
            "Jain Rotla", "Jain", "Dinner",
            220, 5, 45, 3, 
            true, true, true,
            "Thick millet flatbread"
        ));
        
        foods.add(new FoodItem(
            "Pumpkin Sabzi", "Jain", "Dinner",
            180, 3, 25, 8, 
            true, true, true,
            "Pumpkin curry prepared without onions and garlic"
        ));
        
        foods.add(new FoodItem(
            "Capsicum Sabzi", "Jain", "Dinner",
            160, 3, 15, 10, 
            true, true, true,
            "Bell pepper curry prepared in Jain style"
        ));
        
        foods.add(new FoodItem(
            "Jain Chapati", "Jain", "Dinner",
            120, 4, 25, 1, 
            true, true, true,
            "Whole wheat flatbread"
        ));
        
        foods.add(new FoodItem(
            "Jain Rice", "Jain", "Dinner",
            200, 4, 45, 1, 
            true, true, true,
            "Steamed rice prepared in Jain style"
        ));
        
        foods.add(new FoodItem(
            "Sev Tamatar", "Jain", "Dinner",
            250, 6, 30, 12, 
            true, true, true,
            "Tomato curry with crispy gram flour noodles, without onions and garlic"
        ));
        
        // Snack items
        foods.add(new FoodItem(
            "Jain Dhokla", "Jain", "Snack",
            160, 5, 28, 3, 
            true, true, true,
            "Steamed gram flour cake without onions and garlic"
        ));
        
        foods.add(new FoodItem(
            "Jain Khandvi", "Jain", "Snack",
            140, 4, 20, 5, 
            true, true, true,
            "Gram flour rolls without mustard seeds"
        ));
        
        foods.add(new FoodItem(
            "Jain Samosa", "Jain", "Snack",
            180, 3, 22, 10, 
            true, true, true,
            "Pastry filled with spiced peas and non-root vegetables"
        ));
        
        foods.add(new FoodItem(
            "Farsaan", "Jain", "Snack",
            200, 4, 25, 12, 
            true, true, true,
            "Assortment of savory snacks prepared without onions and garlic"
        ));
        
        foods.add(new FoodItem(
            "Jain Khakhra", "Jain", "Snack",
            120, 3, 22, 3, 
            true, true, true,
            "Thin, crispy cracker made from mat bean and wheat flour"
        ));
        
        // Dessert items
        foods.add(new FoodItem(
            "Jain Shrikhand", "Jain", "Dessert",
            280, 6, 45, 8, 
            true, true, true,
            "Sweetened strained yogurt with saffron and cardamom"
        ));
        
        foods.add(new FoodItem(
            "Coconut Ladoo", "Jain", "Dessert",
            200, 3, 22, 12, 
            true, true, true,
            "Sweet balls made with coconut and jaggery"
        ));
        
        foods.add(new FoodItem(
            "Jain Basundi", "Jain", "Dessert",
            320, 8, 50, 10, 
            true, true, true,
            "Thickened, sweetened milk dessert"
        ));
        
        foods.add(new FoodItem(
            "Jain Mohanthal", "Jain", "Dessert",
            300, 5, 48, 12, 
            true, true, true,
            "Sweet gram flour fudge"
        ));
        
        foods.add(new FoodItem(
            "Jain Sukhdi", "Jain", "Dessert",
            250, 4, 35, 14, 
            true, true, true,
            "Sweet made with wheat flour, jaggery, and ghee"
        ));
        
        // Drinks
        foods.add(new FoodItem(
            "Jain Buttermilk", "Jain", "Beverage",
            80, 3, 6, 5, 
            true, true, true,
            "Spiced yogurt drink without asafoetida"
        ));
        
        foods.add(new FoodItem(
            "Fresh Fruit Juice", "Jain", "Beverage",
            120, 1, 30, 0, 
            true, true, true,
            "Juice from fresh fruits"
        ));
        
        foods.add(new FoodItem(
            "Dry Fruits Milkshake", "Jain", "Beverage",
            250, 10, 35, 12, 
            true, true, true,
            "Milk blended with assorted dry fruits"
        ));
        
        return foods;
    }
} 