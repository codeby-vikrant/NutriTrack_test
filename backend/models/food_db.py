import pandas as pd
import os
import numpy as np
from .database import FoodModel, db

class FoodDatabase:
    def __init__(self, db_connection):
        self.db = db_connection
    
    def get_categories(self):
        """Get list of all food categories"""
        categories = db.session.query(FoodModel.food_category).distinct().all()
        return [category[0] for category in categories if category[0]]
    
    def search_foods(self, query=None, category=None, limit=50):
        """Search foods by name or category"""
        food_query = FoodModel.query
        
        if query:
            food_query = food_query.filter(FoodModel.food_name.ilike(f'%{query}%'))
        
        if category:
            food_query = food_query.filter(FoodModel.food_category == category)
        
        foods = food_query.limit(limit).all()
        return [food.to_dict() for food in foods]
    
    def get_food_by_name(self, name):
        """Get food details by exact name"""
        food = FoodModel.query.filter(FoodModel.food_name == name).first()
        if food:
            return food.to_dict()
        return None
    
    def get_food_by_id(self, food_id):
        """Get food details by ID"""
        food = FoodModel.query.get(food_id)
        if food:
            return food.to_dict()
        return None
    
    def get_food_nutrition(self, food_name, quantity=1.0):
        """Get nutritional information for a specific food with specified quantity"""
        food = FoodModel.query.filter(FoodModel.food_name == food_name).first()
        if not food:
            return None
        
        # Convert to dictionary
        food_dict = food.to_dict()
        
        # Scale nutritional values by quantity
        nutritional_fields = [
            'calories', 'protein_g', 'carbs_g', 'fat_g', 
            'fiber_g', 'sugar_g', 'calcium_mg', 'iron_mg', 'sodium_mg'
        ]
        
        for field in nutritional_fields:
            if field in food_dict and food_dict[field] is not None:
                food_dict[field] = round(food_dict[field] * quantity, 2)
        
        return food_dict
    
    def suggest_foods(self, user_preferences, meal_type=None, limit=10):
        """Suggest foods based on user preferences and dietary restrictions"""
        food_query = FoodModel.query
        
        # Filter by user dietary preferences if available
        if 'dietary_preference' in user_preferences:
            preference = user_preferences['dietary_preference'].lower()
            
            if preference == 'vegetarian':
                # Filter out non-vegetarian categories
                food_query = food_query.filter(
                    ~FoodModel.food_category.in_(['Meat', 'Seafood', 'Poultry'])
                )
            elif preference == 'vegan':
                # Filter out animal products
                food_query = food_query.filter(
                    ~FoodModel.food_category.in_(['Meat', 'Seafood', 'Poultry', 'Dairy', 'Eggs'])
                )
            
        # Additional filtering based on meal type
        if meal_type:
            if meal_type.lower() == 'breakfast':
                # Breakfast foods might come from these categories
                food_query = food_query.filter(
                    FoodModel.food_category.in_(['Dairy', 'Fruits', 'Grains', 'Breakfast Cereals'])
                )
            elif meal_type.lower() == 'lunch' or meal_type.lower() == 'dinner':
                # These meals typically have proteins and vegetables
                food_query = food_query.filter(
                    FoodModel.food_category.in_(['Vegetables', 'Legumes', 'Meat', 'Poultry', 'Seafood', 'Grains'])
                )
            elif meal_type.lower() == 'snack':
                # Snacks are typically fruits, nuts, or small items
                food_query = food_query.filter(
                    FoodModel.food_category.in_(['Fruits', 'Nuts and Seeds', 'Snacks'])
                )
        
        # Get random foods matching the criteria
        foods = food_query.order_by(db.func.random()).limit(limit).all()
        return [food.to_dict() for food in foods]
    
    def get_similar_foods(self, food_name, limit=5):
        """Get similar foods based on nutritional profile"""
        target_food = self.get_food_by_name(food_name)
        if not target_food:
            return []
        
        # Simple similarity based on macronutrient ratios
        self.foods_df['similarity'] = self.foods_df.apply(
            lambda row: self._calculate_similarity(row, target_food),
            axis=1
        )
        
        # Sort by similarity (higher is more similar) and exclude the target food itself
        similar_foods = (
            self.foods_df[self.foods_df['food_name'] != food_name]
            .sort_values(by='similarity', ascending=False)
            .head(limit)
        )
        
        return similar_foods.to_dict(orient='records')
    
    def _calculate_similarity(self, food_row, target_food):
        """Calculate nutritional similarity between two foods"""
        total_target = target_food['protein_g'] + target_food['carbs_g'] + target_food['fat_g']
        total_food = food_row['protein_g'] + food_row['carbs_g'] + food_row['fat_g']
        
        if total_target == 0 or total_food == 0:
            return 0
        
        # Calculate ratios for protein, carbs, and fat
        target_ratios = {
            'protein': target_food['protein_g'] / total_target,
            'carbs': target_food['carbs_g'] / total_target,
            'fat': target_food['fat_g'] / total_target
        }
        
        food_ratios = {
            'protein': food_row['protein_g'] / total_food,
            'carbs': food_row['carbs_g'] / total_food,
            'fat': food_row['fat_g'] / total_food
        }
        
        # Calculate distance between ratios (lower is more similar)
        distance = (
            abs(target_ratios['protein'] - food_ratios['protein']) +
            abs(target_ratios['carbs'] - food_ratios['carbs']) +
            abs(target_ratios['fat'] - food_ratios['fat'])
        )
        
        # Convert distance to similarity (higher is more similar)
        similarity = 1 / (1 + distance)
        
        # Additional bonus for same food category
        if food_row['food_category'] == target_food['food_category']:
            similarity *= 1.5
        
        return similarity 