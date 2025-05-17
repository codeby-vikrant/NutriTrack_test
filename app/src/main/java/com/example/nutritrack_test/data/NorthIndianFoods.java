package com.example.nutritrack_test.data;

import java.util.ArrayList;
import java.util.List;

public class NorthIndianFoods {
    
    public static List<FoodItem> getFoods() {
        List<FoodItem> foods = new ArrayList<>();
        
        // Breakfast items
        foods.add(new FoodItem(
            "Aloo Paratha", "North Indian", "Breakfast",
            350, 9, 45, 15, 
            true, false, false,
            "Whole wheat flatbread stuffed with spiced potatoes, served with yogurt"
        ));
        
        foods.add(new FoodItem(
            "Chole Bhature", "North Indian", "Breakfast",
            650, 18, 85, 25, 
            true, false, false,
            "Fried bread served with spicy chickpea curry"
        ));
        
        foods.add(new FoodItem(
            "Bedmi Poori with Aloo Sabzi", "North Indian", "Breakfast",
            450, 10, 65, 20, 
            true, false, false,
            "Fried bread made with whole wheat flour and spices, served with potato curry"
        ));
        
        foods.add(new FoodItem(
            "Paneer Paratha", "North Indian", "Breakfast",
            380, 14, 40, 18, 
            true, false, false,
            "Whole wheat flatbread stuffed with spiced cottage cheese"
        ));
        
        foods.add(new FoodItem(
            "Gobi Paratha", "North Indian", "Breakfast",
            320, 8, 45, 14, 
            true, false, false,
            "Whole wheat flatbread stuffed with spiced cauliflower"
        ));
        
        foods.add(new FoodItem(
            "Methi Paratha", "North Indian", "Breakfast",
            280, 7, 42, 12, 
            true, true, false,
            "Whole wheat flatbread with fenugreek leaves"
        ));

        // Lunch items
        foods.add(new FoodItem(
            "Dal Makhani", "North Indian", "Lunch",
            350, 17, 45, 15, 
            true, false, false,
            "Creamy black lentils cooked with butter and cream"
        ));
        
        foods.add(new FoodItem(
            "Butter Chicken", "North Indian", "Lunch",
            450, 30, 12, 35, 
            false, false, false,
            "Chicken in a rich, creamy tomato-based sauce"
        ));
        
        foods.add(new FoodItem(
            "Rajma Chawal", "North Indian", "Lunch",
            380, 15, 60, 8, 
            true, true, false,
            "Red kidney bean curry served with rice"
        ));
        
        foods.add(new FoodItem(
            "Kadai Paneer", "North Indian", "Lunch",
            420, 20, 18, 32, 
            true, false, false,
            "Cottage cheese cooked with bell peppers in a spicy tomato-based sauce"
        ));
        
        foods.add(new FoodItem(
            "Sarson ka Saag with Makki Roti", "North Indian", "Lunch",
            450, 12, 50, 22, 
            true, false, false,
            "Mustard greens curry served with cornbread"
        ));
        
        foods.add(new FoodItem(
            "Chicken Biryani", "North Indian", "Lunch",
            550, 28, 65, 20, 
            false, false, false,
            "Fragrant rice dish with chicken, spices, and herbs"
        ));
        
        foods.add(new FoodItem(
            "Paneer Tikka Masala", "North Indian", "Lunch",
            480, 22, 20, 35, 
            true, false, false,
            "Grilled cottage cheese in a spicy tomato-based sauce"
        ));
        
        foods.add(new FoodItem(
            "Chana Masala", "North Indian", "Lunch",
            350, 15, 50, 10, 
            true, true, false,
            "Spicy chickpea curry"
        ));
        
        foods.add(new FoodItem(
            "Rogan Josh", "North Indian", "Lunch",
            500, 35, 10, 35, 
            false, false, false,
            "Aromatic lamb curry"
        ));
        
        foods.add(new FoodItem(
            "Palak Paneer", "North Indian", "Lunch",
            380, 18, 15, 25, 
            true, false, false,
            "Cottage cheese in a spinach-based sauce"
        ));

        // Dinner items
        foods.add(new FoodItem(
            "Vegetable Korma", "North Indian", "Dinner",
            380, 12, 35, 20, 
            true, false, false,
            "Mixed vegetables in a creamy, mildly spiced sauce"
        ));
        
        foods.add(new FoodItem(
            "Tandoori Chicken", "North Indian", "Dinner",
            350, 40, 5, 15, 
            false, false, false,
            "Chicken marinated in yogurt and spices, cooked in a tandoor"
        ));
        
        foods.add(new FoodItem(
            "Punjabi Kadhi Pakora", "North Indian", "Dinner",
            320, 10, 38, 15, 
            true, false, false,
            "Yogurt-based curry with gram flour fritters"
        ));
        
        foods.add(new FoodItem(
            "Shahi Paneer", "North Indian", "Dinner",
            450, 20, 16, 35, 
            true, false, false,
            "Cottage cheese in a rich, creamy sauce with nuts"
        ));
        
        foods.add(new FoodItem(
            "Malai Kofta", "North Indian", "Dinner",
            500, 15, 25, 40, 
            true, false, false,
            "Fried vegetable and cottage cheese dumplings in a creamy sauce"
        ));
        
        foods.add(new FoodItem(
            "Amritsari Fish", "North Indian", "Dinner",
            380, 28, 20, 18, 
            false, false, false,
            "Seasoned and fried fish, a specialty from Amritsar"
        ));
        
        foods.add(new FoodItem(
            "Dal Tadka", "North Indian", "Dinner",
            280, 15, 40, 8, 
            true, true, false,
            "Yellow lentil curry with a tempering of spices"
        ));

        // Snack items
        foods.add(new FoodItem(
            "Samosa", "North Indian", "Snack",
            220, 5, 25, 12, 
            true, false, false,
            "Fried pastry with a spiced potato and pea filling"
        ));
        
        foods.add(new FoodItem(
            "Aloo Tikki", "North Indian", "Snack",
            180, 4, 30, 7, 
            true, true, false,
            "Spiced potato patties, often served with chutneys"
        ));
        
        foods.add(new FoodItem(
            "Papdi Chaat", "North Indian", "Snack",
            250, 6, 35, 10, 
            true, false, false,
            "Crispy fried dough wafers with toppings of yogurt, chutneys, and spices"
        ));
        
        foods.add(new FoodItem(
            "Panipuri", "North Indian", "Snack",
            150, 3, 30, 4, 
            true, true, false,
            "Hollow crispy balls filled with flavored water, tamarind chutney, and spiced mashed potatoes"
        ));
        
        foods.add(new FoodItem(
            "Dahi Bhalla", "North Indian", "Snack",
            220, 8, 32, 8, 
            true, false, false,
            "Fried lentil dumplings soaked in yogurt, topped with chutneys and spices"
        ));
        
        foods.add(new FoodItem(
            "Pakora", "North Indian", "Snack",
            200, 6, 20, 11, 
            true, false, false,
            "Vegetable fritters made with gram flour batter"
        ));
        
        // Add more Punjabi dishes
        foods.add(new FoodItem(
            "Punjabi Chole", "North Indian", "Lunch",
            350, 16, 52, 9, 
            true, true, false,
            "Spicy chickpea curry, a specialty from Punjab"
        ));
        
        foods.add(new FoodItem(
            "Amritsari Kulcha", "North Indian", "Breakfast",
            320, 8, 48, 12, 
            true, false, false,
            "Leavened bread stuffed with potatoes or paneer, from Amritsar"
        ));
        
        foods.add(new FoodItem(
            "Pindi Chana", "North Indian", "Lunch",
            330, 15, 50, 10, 
            true, true, false,
            "Spicy chickpea curry from Rawalpindi"
        ));
        
        foods.add(new FoodItem(
            "Lassi", "North Indian", "Beverage",
            180, 7, 25, 5, 
            true, false, false,
            "Yogurt-based drink, can be sweet or salty"
        ));
        
        // Add Awadhi cuisine
        foods.add(new FoodItem(
            "Lucknowi Biryani", "North Indian", "Lunch",
            550, 25, 70, 20, 
            false, false, false,
            "Aromatic rice dish with meat and spices, from Lucknow"
        ));
        
        foods.add(new FoodItem(
            "Galouti Kebab", "North Indian", "Snack",
            320, 20, 5, 25, 
            false, false, false,
            "Tender minced meat kebabs, a specialty from Lucknow"
        ));
        
        foods.add(new FoodItem(
            "Sheermal", "North Indian", "Breakfast",
            280, 8, 40, 12, 
            true, false, false,
            "Saffron-flavored flatbread from Lucknow"
        ));
        
        // Add more Haryanvi dishes
        foods.add(new FoodItem(
            "Kachri Ki Sabzi", "North Indian", "Lunch",
            280, 8, 38, 14, 
            true, true, false,
            "Curry made with wild cucumbers, from Haryana"
        ));
        
        foods.add(new FoodItem(
            "Hara Dhania Cholia", "North Indian", "Lunch",
            310, 12, 42, 15, 
            true, true, false,
            "Green chickpeas cooked with coriander, a Haryanvi specialty"
        ));
        
        // Add more items from Himachal Pradesh
        foods.add(new FoodItem(
            "Dham", "North Indian", "Lunch",
            450, 15, 60, 16, 
            true, false, false,
            "Traditional festive meal from Himachal Pradesh with rajma, rice, and yogurt"
        ));
        
        foods.add(new FoodItem(
            "Babru", "North Indian", "Snack",
            320, 10, 38, 18, 
            true, false, false,
            "Black gram stuffed fried bread from Himachal Pradesh"
        ));
        
        // Add more items from Uttarakhand
        foods.add(new FoodItem(
            "Kafuli", "North Indian", "Lunch",
            280, 12, 35, 10, 
            true, true, false,
            "Spinach and fenugreek curry from Uttarakhand"
        ));
        
        foods.add(new FoodItem(
            "Aloo Ke Gutke", "North Indian", "Lunch",
            310, 6, 45, 14, 
            true, true, false,
            "Spicy potato preparation from Uttarakhand"
        ));
        
        // Add more Rajasthani dishes
        foods.add(new FoodItem(
            "Dal Baati Churma", "North Indian", "Lunch",
            580, 18, 70, 22, 
            true, false, false,
            "Traditional Rajasthani meal with lentils, baked wheat balls, and sweet crumbled bread"
        ));
        
        foods.add(new FoodItem(
            "Gatte Ki Sabzi", "North Indian", "Lunch",
            350, 14, 40, 15, 
            true, false, false,
            "Gram flour dumplings in a yogurt-based curry from Rajasthan"
        ));
        
        foods.add(new FoodItem(
            "Ker Sangri", "North Indian", "Lunch",
            280, 10, 35, 12, 
            true, true, false,
            "Dried berries and beans preparation from Rajasthan"
        ));
        
        foods.add(new FoodItem(
            "Laal Maas", "North Indian", "Lunch",
            480, 32, 8, 38, 
            false, false, false,
            "Spicy red meat curry from Rajasthan"
        ));
        
        return foods;
    }
} 