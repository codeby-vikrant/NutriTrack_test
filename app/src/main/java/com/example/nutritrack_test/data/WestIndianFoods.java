package com.example.nutritrack_test.data;

import java.util.ArrayList;
import java.util.List;

public class WestIndianFoods {
    
    public static List<FoodItem> getFoods() {
        List<FoodItem> foods = new ArrayList<>();
        
        // Maharashtrian dishes
        foods.add(new FoodItem(
            "Poha", "Maharashtrian", "Breakfast",
            250, 6, 45, 5, 
            true, true, false,
            "Flattened rice with peanuts, onions, and spices"
        ));
        
        foods.add(new FoodItem(
            "Misal Pav", "Maharashtrian", "Breakfast",
            350, 12, 50, 10, 
            true, true, false,
            "Spicy sprouted bean curry served with bread rolls"
        ));
        
        foods.add(new FoodItem(
            "Vada Pav", "Maharashtrian", "Snack",
            300, 8, 40, 12, 
            true, false, false,
            "Spicy potato fritter in a bread roll, Mumbai's street food"
        ));
        
        foods.add(new FoodItem(
            "Sabudana Khichdi", "Maharashtrian", "Breakfast",
            320, 5, 50, 12, 
            true, false, true,
            "Tapioca pearls with peanuts and potatoes, often eaten during fasting"
        ));
        
        foods.add(new FoodItem(
            "Pav Bhaji", "Maharashtrian", "Lunch",
            450, 10, 60, 18, 
            true, false, false,
            "Mashed vegetable curry served with buttered bread rolls"
        ));
        
        foods.add(new FoodItem(
            "Puran Poli", "Maharashtrian", "Dessert",
            350, 7, 65, 8, 
            true, false, false,
            "Sweet flatbread stuffed with jaggery and lentil mixture"
        ));
        
        foods.add(new FoodItem(
            "Amti", "Maharashtrian", "Lunch",
            220, 12, 30, 6, 
            true, true, false,
            "Spicy lentil curry with kokum"
        ));
        
        foods.add(new FoodItem(
            "Bharli Vangi", "Maharashtrian", "Lunch",
            280, 6, 25, 18, 
            true, true, false,
            "Stuffed eggplant curry"
        ));
        
        foods.add(new FoodItem(
            "Matki Usal", "Maharashtrian", "Lunch",
            250, 14, 35, 6, 
            true, true, false,
            "Sprouted moth beans curry"
        ));
        
        foods.add(new FoodItem(
            "Thalipeeth", "Maharashtrian", "Breakfast",
            280, 8, 45, 7, 
            true, true, false,
            "Multi-grain savory pancake"
        ));
        
        // Gujarati dishes
        foods.add(new FoodItem(
            "Dhokla", "Gujarati", "Breakfast",
            180, 6, 30, 4, 
            true, true, true,
            "Steamed fermented rice and chickpea flour cake"
        ));
        
        foods.add(new FoodItem(
            "Fafda", "Gujarati", "Snack",
            250, 5, 30, 15, 
            true, true, true,
            "Crispy, spicy chickpea flour snack"
        ));
        
        foods.add(new FoodItem(
            "Thepla", "Gujarati", "Breakfast",
            220, 6, 35, 8, 
            true, false, false,
            "Fenugreek-flavored flatbread"
        ));
        
        foods.add(new FoodItem(
            "Undhiyu", "Gujarati", "Lunch",
            350, 8, 40, 18, 
            true, true, false,
            "Mixed vegetable casserole"
        ));
        
        foods.add(new FoodItem(
            "Khandvi", "Gujarati", "Snack",
            150, 5, 20, 6, 
            true, false, false,
            "Savory rolled snack made from gram flour and yogurt"
        ));
        
        foods.add(new FoodItem(
            "Gujarati Kadhi", "Gujarati", "Lunch",
            180, 4, 15, 12, 
            true, false, true,
            "Sweet and spicy yogurt-based curry"
        ));
        
        foods.add(new FoodItem(
            "Daal Dhokli", "Gujarati", "Lunch",
            320, 12, 50, 8, 
            true, false, false,
            "Whole wheat flour dumplings in a lentil soup"
        ));
        
        foods.add(new FoodItem(
            "Handvo", "Gujarati", "Lunch",
            280, 8, 35, 12, 
            true, false, false,
            "Savory cake made with lentils, rice, and vegetables"
        ));
        
        foods.add(new FoodItem(
            "Muthiya", "Gujarati", "Snack",
            200, 5, 30, 8, 
            true, true, false,
            "Steamed or fried dumplings made with various flours and vegetables"
        ));
        
        foods.add(new FoodItem(
            "Khakhra", "Gujarati", "Snack",
            120, 3, 22, 3, 
            true, true, true,
            "Thin, crispy cracker made from mat bean, wheat flour and oil"
        ));
        
        // Goan dishes
        foods.add(new FoodItem(
            "Goan Fish Curry", "Goan", "Lunch",
            320, 25, 15, 18, 
            false, false, false,
            "Tangy and spicy fish curry with coconut"
        ));
        
        foods.add(new FoodItem(
            "Vindaloo", "Goan", "Lunch",
            450, 30, 10, 30, 
            false, false, false,
            "Spicy meat curry with vinegar and garlic"
        ));
        
        foods.add(new FoodItem(
            "Xacuti", "Goan", "Lunch",
            420, 28, 12, 28, 
            false, false, false,
            "Coconut-based curry with complex spices"
        ));
        
        foods.add(new FoodItem(
            "Sorpotel", "Goan", "Lunch",
            500, 35, 8, 38, 
            false, false, false,
            "Spicy pork dish with vinegar"
        ));
        
        foods.add(new FoodItem(
            "Bebinca", "Goan", "Dessert",
            350, 5, 45, 18, 
            true, false, false,
            "Traditional layered pudding"
        ));
        
        foods.add(new FoodItem(
            "Khatkhate", "Goan", "Lunch",
            280, 8, 40, 10, 
            true, false, false,
            "Mixed vegetable stew with coconut"
        ));
        
        foods.add(new FoodItem(
            "Goan Red Rice", "Goan", "Lunch",
            200, 4, 45, 1, 
            true, true, false,
            "Nutritious red rice, a staple in Goan cuisine"
        ));
        
        foods.add(new FoodItem(
            "Sanna", "Goan", "Breakfast",
            180, 3, 35, 5, 
            true, false, false,
            "Steamed rice cake similar to idli"
        ));
        
        foods.add(new FoodItem(
            "Caldo Verde", "Goan", "Lunch",
            220, 6, 25, 12, 
            false, false, false,
            "Portuguese-influenced soup with greens and sausage"
        ));
        
        foods.add(new FoodItem(
            "Goan Prawn Curry", "Goan", "Lunch",
            350, 28, 10, 20, 
            false, false, false,
            "Coconut-based curry with prawns"
        ));
        
        // Rajasthani dishes
        foods.add(new FoodItem(
            "Dal Baati Churma", "Rajasthani", "Lunch",
            650, 16, 80, 28, 
            true, false, false,
            "Lentils, baked wheat balls, and sweet crumbled bread"
        ));
        
        foods.add(new FoodItem(
            "Gatte Ki Sabzi", "Rajasthani", "Lunch",
            350, 14, 40, 15, 
            true, false, false,
            "Gram flour dumplings in a yogurt-based curry"
        ));
        
        foods.add(new FoodItem(
            "Ker Sangri", "Rajasthani", "Lunch",
            280, 7, 35, 16, 
            true, true, false,
            "Dried berries and beans preparation"
        ));
        
        foods.add(new FoodItem(
            "Laal Maas", "Rajasthani", "Lunch",
            480, 32, 8, 38, 
            false, false, false,
            "Spicy red meat curry"
        ));
        
        foods.add(new FoodItem(
            "Pyaaz Kachori", "Rajasthani", "Snack",
            280, 6, 35, 15, 
            true, false, false,
            "Deep-fried pastry filled with spicy onion mixture"
        ));
        
        foods.add(new FoodItem(
            "Bajra Roti", "Rajasthani", "Lunch",
            180, 5, 40, 3, 
            true, true, false,
            "Pearl millet flatbread"
        ));
        
        foods.add(new FoodItem(
            "Mohan Maas", "Rajasthani", "Lunch",
            450, 30, 5, 35, 
            false, false, false,
            "Rich meat curry with cream"
        ));
        
        foods.add(new FoodItem(
            "Rabodi", "Rajasthani", "Snack",
            220, 5, 30, 10, 
            true, false, false,
            "Sun-dried buttermilk and flour snack"
        ));
        
        foods.add(new FoodItem(
            "Ghevar", "Rajasthani", "Dessert",
            400, 5, 60, 20, 
            true, false, false,
            "Disc-shaped sweet cake made with flour and soaked in sugar syrup"
        ));
        
        foods.add(new FoodItem(
            "Mohanthal", "Rajasthani", "Dessert",
            350, 6, 55, 15, 
            true, false, false,
            "Sweet gram flour fudge"
        ));
        
        // More Maharashtrian dishes
        foods.add(new FoodItem(
            "Kothimbir Vadi", "Maharashtrian", "Snack",
            220, 6, 28, 12, 
            true, true, false,
            "Steamed and fried coriander fritters"
        ));
        
        foods.add(new FoodItem(
            "Zunka Bhakri", "Maharashtrian", "Lunch",
            350, 10, 52, 10, 
            true, true, false,
            "Spiced gram flour dish served with millet flatbread"
        ));
        
        foods.add(new FoodItem(
            "Bombil Fry", "Maharashtrian", "Lunch",
            280, 22, 12, 18, 
            false, false, false,
            "Crispy fried Bombay duck fish"
        ));
        
        foods.add(new FoodItem(
            "Tambda Rassa", "Maharashtrian", "Lunch",
            320, 18, 15, 22, 
            false, false, false,
            "Spicy red mutton curry from Kolhapur"
        ));
        
        foods.add(new FoodItem(
            "Shrikhand", "Maharashtrian", "Dessert",
            320, 7, 50, 10, 
            true, false, false,
            "Sweetened strained yogurt with saffron and cardamom"
        ));

        return foods;
    }
} 