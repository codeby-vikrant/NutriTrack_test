package com.example.nutritrack_test.data;

import java.util.ArrayList;
import java.util.List;

public class SouthIndianFoods {
    
    public static List<FoodItem> getFoods() {
        List<FoodItem> foods = new ArrayList<>();
        
        // Breakfast items
        foods.add(new FoodItem(
            "Idli", "South Indian", "Breakfast",
            120, 4, 25, 0.5f, 
            true, true, false,
            "Steamed rice and fermented black lentil cakes"
        ));
        
        foods.add(new FoodItem(
            "Dosa", "South Indian", "Breakfast",
            180, 5, 28, 5, 
            true, true, false,
            "Crispy fermented rice and black gram crepe"
        ));
        
        foods.add(new FoodItem(
            "Masala Dosa", "South Indian", "Breakfast",
            250, 6, 35, 8, 
            true, true, false,
            "Crispy crepe filled with spiced potato filling"
        ));
        
        foods.add(new FoodItem(
            "Uttapam", "South Indian", "Breakfast",
            220, 6, 32, 7, 
            true, true, false,
            "Thick pancake topped with vegetables"
        ));
        
        foods.add(new FoodItem(
            "Upma", "South Indian", "Breakfast",
            180, 5, 30, 4, 
            true, true, false,
            "Savory semolina porridge with vegetables"
        ));
        
        foods.add(new FoodItem(
            "Medu Vada", "South Indian", "Breakfast",
            150, 5, 18, 8, 
            true, true, false,
            "Fried doughnut-shaped lentil snack"
        ));
        
        foods.add(new FoodItem(
            "Appam", "South Indian", "Breakfast",
            180, 3, 32, 4, 
            true, true, false,
            "Fermented rice pancake with a soft center and crispy edges, from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Puttu", "South Indian", "Breakfast",
            200, 4, 40, 2, 
            true, true, false,
            "Steamed rice flour and coconut cylinders, popular in Kerala"
        ));
        
        foods.add(new FoodItem(
            "Pongal", "South Indian", "Breakfast",
            280, 8, 50, 5, 
            true, false, false,
            "Rice and lentil porridge, popular in Tamil Nadu"
        ));
        
        foods.add(new FoodItem(
            "Pesarattu", "South Indian", "Breakfast",
            160, 7, 25, 3, 
            true, true, false,
            "Green gram crepe, popular in Andhra Pradesh"
        ));
        
        // Lunch items
        foods.add(new FoodItem(
            "Sambar Rice", "South Indian", "Lunch",
            320, 10, 60, 5, 
            true, true, false,
            "Rice mixed with lentil and vegetable stew"
        ));
        
        foods.add(new FoodItem(
            "Rasam Rice", "South Indian", "Lunch",
            280, 6, 58, 3, 
            true, true, false,
            "Rice served with tangy tamarind soup"
        ));
        
        foods.add(new FoodItem(
            "Curd Rice", "South Indian", "Lunch",
            250, 8, 45, 6, 
            true, false, false,
            "Rice mixed with yogurt and tempering"
        ));
        
        foods.add(new FoodItem(
            "Bisi Bele Bath", "South Indian", "Lunch",
            380, 12, 65, 8, 
            true, false, false,
            "Spicy rice dish with lentils and vegetables, from Karnataka"
        ));
        
        foods.add(new FoodItem(
            "Hyderabadi Biryani", "South Indian", "Lunch",
            550, 28, 70, 20, 
            false, false, false,
            "Fragrant rice dish with meat, spices, and herbs, from Hyderabad"
        ));
        
        foods.add(new FoodItem(
            "Pulihora", "South Indian", "Lunch",
            320, 7, 60, 6, 
            true, true, false,
            "Tamarind rice, popular in Andhra Pradesh"
        ));
        
        foods.add(new FoodItem(
            "Coconut Rice", "South Indian", "Lunch",
            350, 6, 62, 10, 
            true, true, false,
            "Rice flavored with coconut and tempering"
        ));
        
        foods.add(new FoodItem(
            "Kerala Fish Curry", "South Indian", "Lunch",
            320, 25, 15, 18, 
            false, false, false,
            "Spicy fish curry with coconut milk, from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Avial", "South Indian", "Lunch",
            250, 6, 30, 15, 
            true, true, false,
            "Mixed vegetable curry with coconut and yogurt, from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Vatha Kuzhambu", "South Indian", "Lunch",
            220, 5, 25, 12, 
            true, true, false,
            "Tangy and spicy tamarind-based curry, from Tamil Nadu"
        ));
        
        // Dinner items
        foods.add(new FoodItem(
            "Parotta", "South Indian", "Dinner",
            320, 7, 45, 12, 
            true, false, false,
            "Layered flatbread, popular in Tamil Nadu and Kerala"
        ));
        
        foods.add(new FoodItem(
            "Kothu Parotta", "South Indian", "Dinner",
            450, 15, 50, 20, 
            false, false, false,
            "Shredded flatbread mixed with meat, vegetables, and spices"
        ));
        
        foods.add(new FoodItem(
            "Malabar Chicken Curry", "South Indian", "Dinner",
            380, 28, 10, 25, 
            false, false, false,
            "Spicy chicken curry from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Andhra Chicken Curry", "South Indian", "Dinner",
            400, 30, 8, 28, 
            false, false, false,
            "Fiery chicken curry from Andhra Pradesh"
        ));
        
        foods.add(new FoodItem(
            "Vegetable Stew", "South Indian", "Dinner",
            250, 6, 28, 14, 
            true, false, false,
            "Mild coconut milk-based vegetable stew, from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Tomato Pappu", "South Indian", "Dinner",
            280, 12, 35, 8, 
            true, true, false,
            "Tomato and lentil stew, from Andhra Pradesh"
        ));
        
        foods.add(new FoodItem(
            "Ulli Theeyal", "South Indian", "Dinner",
            260, 8, 30, 12, 
            true, true, false,
            "Spicy shallot curry with roasted coconut, from Kerala"
        ));
        
        // Snack items
        foods.add(new FoodItem(
            "Murukku", "South Indian", "Snack",
            180, 3, 25, 8, 
            true, true, false,
            "Crunchy twisted snack made from rice and urad dal flour"
        ));
        
        foods.add(new FoodItem(
            "Bonda", "South Indian", "Snack",
            150, 4, 20, 7, 
            true, true, false,
            "Fried potato dumplings"
        ));
        
        foods.add(new FoodItem(
            "Mysore Pak", "South Indian", "Snack",
            220, 3, 20, 15, 
            true, false, false,
            "Sweet made from ghee, sugar, and gram flour, from Karnataka"
        ));
        
        foods.add(new FoodItem(
            "Masala Vada", "South Indian", "Snack",
            120, 6, 15, 5, 
            true, true, false,
            "Spicy lentil fritters"
        ));
        
        foods.add(new FoodItem(
            "Paruppu Vada", "South Indian", "Snack",
            130, 7, 14, 6, 
            true, true, false,
            "Crispy lentil fritters, popular in Tamil Nadu"
        ));
        
        foods.add(new FoodItem(
            "Kuzhi Paniyaram", "South Indian", "Snack",
            160, 5, 22, 6, 
            true, true, false,
            "Savory steamed dumplings made from fermented rice and lentil batter"
        ));
        
        // Add more Tamil Nadu dishes
        foods.add(new FoodItem(
            "Kara Kuzhambu", "South Indian", "Lunch",
            250, 6, 30, 12, 
            true, true, false,
            "Spicy tamarind-based curry from Tamil Nadu"
        ));
        
        foods.add(new FoodItem(
            "Mor Kuzhambu", "South Indian", "Lunch",
            220, 5, 25, 10, 
            true, false, false,
            "Yogurt-based curry from Tamil Nadu"
        ));
        
        foods.add(new FoodItem(
            "Keerai Masiyal", "South Indian", "Lunch",
            180, 7, 22, 6, 
            true, true, false,
            "Mashed spinach dish from Tamil Nadu"
        ));
        
        // Add more Kerala dishes
        foods.add(new FoodItem(
            "Meen Pollichathu", "South Indian", "Lunch",
            320, 28, 8, 20, 
            false, false, false,
            "Fish wrapped in banana leaf and grilled, from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Kadala Curry", "South Indian", "Lunch",
            280, 12, 40, 8, 
            true, true, false,
            "Black chickpea curry from Kerala"
        ));
        
        foods.add(new FoodItem(
            "Erissery", "South Indian", "Lunch",
            250, 8, 35, 10, 
            true, true, false,
            "Pumpkin and lentil curry with coconut, from Kerala"
        ));
        
        // Add more Andhra Pradesh dishes
        foods.add(new FoodItem(
            "Gongura Pachadi", "South Indian", "Lunch",
            180, 4, 15, 12, 
            true, true, false,
            "Sorrel leaves chutney from Andhra Pradesh"
        ));
        
        foods.add(new FoodItem(
            "Pesarattu Upma", "South Indian", "Breakfast",
            250, 9, 40, 5, 
            true, true, false,
            "Green gram crepe served with semolina upma, from Andhra Pradesh"
        ));
        
        foods.add(new FoodItem(
            "Gutti Vankaya Kura", "South Indian", "Lunch",
            280, 6, 25, 18, 
            true, true, false,
            "Stuffed eggplant curry from Andhra Pradesh"
        ));
        
        // Add more Karnataka dishes
        foods.add(new FoodItem(
            "Neer Dosa", "South Indian", "Breakfast",
            150, 3, 30, 2, 
            true, true, false,
            "Thin rice crepe from Karnataka"
        ));
        
        foods.add(new FoodItem(
            "Akki Roti", "South Indian", "Breakfast",
            220, 5, 40, 4, 
            true, true, false,
            "Rice flour flatbread from Karnataka"
        ));
        
        foods.add(new FoodItem(
            "Ragi Mudde", "South Indian", "Lunch",
            250, 6, 50, 1, 
            true, true, false,
            "Finger millet balls, a staple in Karnataka"
        ));
        
        // Add more Telangana dishes
        foods.add(new FoodItem(
            "Sakinalu", "South Indian", "Snack",
            180, 3, 25, 8, 
            true, true, false,
            "Crispy rice-based snack from Telangana"
        ));
        
        foods.add(new FoodItem(
            "Sarva Pindi", "South Indian", "Breakfast",
            250, 6, 35, 10, 
            true, true, false,
            "Rice and lentil flatbread from Telangana"
        ));
        
        foods.add(new FoodItem(
            "Golichina Mamsam", "South Indian", "Lunch",
            380, 32, 5, 25, 
            false, false, false,
            "Spicy dry meat fry from Telangana"
        ));
        
        return foods;
    }
} 