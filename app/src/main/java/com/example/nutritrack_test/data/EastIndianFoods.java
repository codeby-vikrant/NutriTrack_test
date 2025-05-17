package com.example.nutritrack_test.data;

import java.util.ArrayList;
import java.util.List;

public class EastIndianFoods {
    
    public static List<FoodItem> getFoods() {
        List<FoodItem> foods = new ArrayList<>();
        
        // Bengali dishes
        foods.add(new FoodItem(
            "Machher Jhol", "Bengali", "Lunch",
            320, 25, 15, 18, 
            false, false, false,
            "Traditional Bengali fish curry with mustard"
        ));
        
        foods.add(new FoodItem(
            "Shorshe Ilish", "Bengali", "Lunch",
            380, 28, 8, 25, 
            false, false, false,
            "Hilsa fish in mustard sauce, a Bengali delicacy"
        ));
        
        foods.add(new FoodItem(
            "Chingri Malai Curry", "Bengali", "Lunch",
            420, 30, 10, 30, 
            false, false, false,
            "Prawns in coconut cream curry"
        ));
        
        foods.add(new FoodItem(
            "Aloo Posto", "Bengali", "Lunch",
            280, 6, 35, 15, 
            true, true, false,
            "Potatoes cooked with poppy seed paste"
        ));
        
        foods.add(new FoodItem(
            "Luchi", "Bengali", "Breakfast",
            220, 4, 32, 12, 
            true, false, false,
            "Deep-fried Bengali bread made from flour"
        ));
        
        foods.add(new FoodItem(
            "Kosha Mangsho", "Bengali", "Lunch",
            450, 32, 10, 35, 
            false, false, false,
            "Slow-cooked spicy mutton curry"
        ));
        
        foods.add(new FoodItem(
            "Mishti Doi", "Bengali", "Dessert",
            200, 5, 35, 5, 
            true, false, false,
            "Sweet yogurt dessert"
        ));
        
        foods.add(new FoodItem(
            "Rasgulla", "Bengali", "Dessert",
            180, 4, 38, 2, 
            true, false, false,
            "Soft, spongy cottage cheese balls in sugar syrup"
        ));
        
        foods.add(new FoodItem(
            "Sandesh", "Bengali", "Dessert",
            220, 6, 40, 8, 
            true, false, false,
            "Sweet made from cottage cheese and sugar"
        ));
        
        foods.add(new FoodItem(
            "Shukto", "Bengali", "Lunch",
            220, 5, 25, 12, 
            true, true, false,
            "Bengali mixed vegetable stew with a bitter taste"
        ));
        
        foods.add(new FoodItem(
            "Chholar Dal", "Bengali", "Lunch",
            280, 15, 40, 8, 
            true, true, false,
            "Bengal gram lentil preparation"
        ));
        
        foods.add(new FoodItem(
            "Potoler Dolma", "Bengali", "Lunch",
            250, 10, 30, 12, 
            false, false, false,
            "Stuffed pointed gourd with meat"
        ));
        
        foods.add(new FoodItem(
            "Bhapa Ilish", "Bengali", "Lunch",
            350, 25, 5, 25, 
            false, false, false,
            "Steamed Hilsa fish with mustard paste"
        ));
        
        // Odia dishes
        foods.add(new FoodItem(
            "Dalma", "Odia", "Lunch",
            280, 12, 45, 6, 
            true, true, false,
            "Lentils cooked with vegetables"
        ));
        
        foods.add(new FoodItem(
            "Pakhala Bhata", "Odia", "Lunch",
            250, 6, 50, 3, 
            true, true, false,
            "Fermented rice with water, often served with sides"
        ));
        
        foods.add(new FoodItem(
            "Chhena Poda", "Odia", "Dessert",
            350, 8, 50, 15, 
            true, false, false,
            "Baked sweet made from cottage cheese and sugar"
        ));
        
        foods.add(new FoodItem(
            "Machha Besara", "Odia", "Lunch",
            320, 25, 10, 20, 
            false, false, false,
            "Fish curry with mustard paste"
        ));
        
        foods.add(new FoodItem(
            "Santula", "Odia", "Lunch",
            220, 7, 30, 8, 
            true, true, false,
            "Mixed vegetable curry without onion and garlic"
        ));
        
        foods.add(new FoodItem(
            "Odia Aloodum", "Odia", "Lunch",
            280, 5, 40, 14, 
            true, true, false,
            "Spicy potato curry"
        ));
        
        foods.add(new FoodItem(
            "Chhena Jhili", "Odia", "Dessert",
            250, 5, 45, 8, 
            true, false, false,
            "Cottage cheese dumplings in sugar syrup"
        ));
        
        // Assamese dishes
        foods.add(new FoodItem(
            "Masor Tenga", "Assamese", "Lunch",
            280, 22, 15, 16, 
            false, false, false,
            "Sour fish curry, a traditional Assamese dish"
        ));
        
        foods.add(new FoodItem(
            "Aloo Pitika", "Assamese", "Snack",
            180, 4, 35, 5, 
            true, true, false,
            "Mashed potatoes with mustard oil, onions, and chili"
        ));
        
        foods.add(new FoodItem(
            "Khar", "Assamese", "Lunch",
            250, 6, 30, 12, 
            true, true, false,
            "Vegetable preparation with khar (alkali)"
        ));
        
        foods.add(new FoodItem(
            "Paro Manxho", "Assamese", "Lunch",
            380, 30, 5, 25, 
            false, false, false,
            "Pigeon meat curry"
        ));
        
        foods.add(new FoodItem(
            "Xaak Aru Bhaji", "Assamese", "Lunch",
            180, 5, 20, 8, 
            true, true, false,
            "Assamese stir-fried leafy greens"
        ));
        
        foods.add(new FoodItem(
            "Ou Tenga Masor Jool", "Assamese", "Lunch",
            300, 24, 12, 18, 
            false, false, false,
            "Fish curry with elephant apple"
        ));
        
        foods.add(new FoodItem(
            "Poita Bhaat", "Assamese", "Lunch",
            220, 5, 45, 3, 
            true, true, false,
            "Fermented rice, similar to Odia pakhala"
        ));
        
        foods.add(new FoodItem(
            "Luchi Mangsho", "Assamese", "Lunch",
            450, 25, 40, 25, 
            false, false, false,
            "Deep-fried bread with meat curry"
        ));
        
        // Bihari dishes
        foods.add(new FoodItem(
            "Litti Chokha", "Bihari", "Lunch",
            450, 12, 65, 15, 
            true, false, false,
            "Wheat balls filled with roasted gram flour, served with mashed vegetables"
        ));
        
        foods.add(new FoodItem(
            "Sattu Ka Paratha", "Bihari", "Breakfast",
            320, 10, 48, 10, 
            true, false, false,
            "Flatbread stuffed with roasted gram flour"
        ));
        
        foods.add(new FoodItem(
            "Kadhi Badi", "Bihari", "Lunch",
            280, 8, 30, 15, 
            true, false, false,
            "Fried lentil dumplings in yogurt gravy"
        ));
        
        foods.add(new FoodItem(
            "Thekua", "Bihari", "Snack",
            250, 5, 40, 10, 
            true, false, false,
            "Deep-fried sweet made with wheat flour and jaggery"
        ));
        
        foods.add(new FoodItem(
            "Bihari Mutton Curry", "Bihari", "Lunch",
            450, 32, 8, 35, 
            false, false, false,
            "Spicy mutton curry with ginger and garlic"
        ));
        
        foods.add(new FoodItem(
            "Dhuska", "Bihari", "Breakfast",
            280, 7, 45, 12, 
            true, false, false,
            "Deep-fried snack made with rice and lentils"
        ));
        
        // Jharkhand dishes
        foods.add(new FoodItem(
            "Rugra Sabzi", "Jharkhand", "Lunch",
            220, 8, 25, 10, 
            true, true, false,
            "Mushroom curry from Jharkhand"
        ));
        
        foods.add(new FoodItem(
            "Bamboo Shoot Curry", "Jharkhand", "Lunch",
            250, 6, 30, 12, 
            true, true, false,
            "Curry made from bamboo shoots"
        ));
        
        foods.add(new FoodItem(
            "Chilka Roti", "Jharkhand", "Lunch",
            220, 5, 45, 5, 
            true, true, false,
            "Rice flour flatbread"
        ));
        
        foods.add(new FoodItem(
            "Handia Mutton", "Jharkhand", "Lunch",
            420, 30, 15, 30, 
            false, false, false,
            "Mutton cooked with rice beer"
        ));
        
        // Tripura dishes
        foods.add(new FoodItem(
            "Mui Borok", "Tripura", "Lunch",
            320, 12, 40, 15, 
            true, true, false,
            "Bamboo shoot pickle or curry"
        ));
        
        foods.add(new FoodItem(
            "Berma Bwtwi", "Tripura", "Lunch",
            300, 25, 25, 15, 
            false, false, false,
            "Fermented fish with chili and vegetables"
        ));
        
        foods.add(new FoodItem(
            "Chakhwi", "Tripura", "Lunch",
            250, 8, 38, 10, 
            true, true, false,
            "Traditional vegetable stew"
        ));
        
        // Sikkim dishes
        foods.add(new FoodItem(
            "Momos", "Sikkim", "Snack",
            250, 12, 30, 12, 
            false, false, false,
            "Steamed dumplings filled with meat or vegetables"
        ));
        
        foods.add(new FoodItem(
            "Thukpa", "Sikkim", "Lunch",
            320, 15, 45, 8, 
            false, false, false,
            "Noodle soup with meat and vegetables"
        ));
        
        foods.add(new FoodItem(
            "Gundruk", "Sikkim", "Lunch",
            220, 7, 30, 6, 
            true, true, false,
            "Fermented leafy green vegetable dish"
        ));
        
        foods.add(new FoodItem(
            "Phagshapa", "Sikkim", "Lunch",
            380, 25, 10, 30, 
            false, false, false,
            "Pork curry with radishes"
        ));
        
        foods.add(new FoodItem(
            "Sel Roti", "Sikkim", "Breakfast",
            280, 5, 50, 10, 
            true, false, false,
            "Ring-shaped rice bread/doughnut"
        ));
        
        return foods;
    }
} 