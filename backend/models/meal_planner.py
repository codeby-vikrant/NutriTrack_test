import pandas as pd
import os
import numpy as np
from datetime import datetime, timedelta
import random
from .database import MealPlanModel, db

class MealPlanner:
    def __init__(self, food_db, db_connection):
        """Initialize the meal planner with a food database"""
        self.food_db = food_db
        self.db = db_connection
        self.last_plan_id = 0  # This will be initialized correctly when needed
        
        # Define typical meal composition and calorie distribution
        self.meal_distribution = {
            'Breakfast': 0.25,  # 25% of daily calories
            'Lunch': 0.35,      # 35% of daily calories
            'Dinner': 0.3,      # 30% of daily calories
            'Snack': 0.1        # 10% of daily calories
        }
        
        # Define common food combinations
        self.meal_templates = {
            'Breakfast': [
                {'categories': ['Breads', 'Dairy'], 'weights': [1, 0.5]},
                {'categories': ['South Indian', 'Lentils'], 'weights': [1, 0.5]},
                {'categories': ['Grains', 'Dairy', 'Beverages'], 'weights': [0.5, 0.3, 1]}
            ],
            'Lunch': [
                {'categories': ['Breads', 'Vegetarian Curries', 'Sides'], 'weights': [2, 1, 0.5]},
                {'categories': ['Rice Dishes', 'Lentils', 'Sides'], 'weights': [1, 1, 0.5]},
                {'categories': ['Rice Dishes', 'Non-Vegetarian', 'Sides'], 'weights': [0.75, 1, 0.5]}
            ],
            'Dinner': [
                {'categories': ['Breads', 'Vegetarian Curries'], 'weights': [2, 1]},
                {'categories': ['Breads', 'Non-Vegetarian'], 'weights': [1, 1]},
                {'categories': ['Rice Dishes', 'Lentils', 'Vegetarian Curries'], 'weights': [0.75, 0.5, 0.5]}
            ],
            'Snack': [
                {'categories': ['Snacks'], 'weights': [1]},
                {'categories': ['Beverages', 'Breads'], 'weights': [1, 0.5]},
                {'categories': ['Dairy', 'Fruits'], 'weights': [1, 1]}  # Fruits category might not exist in our DB
            ]
        }
    
    def get_meal_plan(self, user_id, date):
        """Get the meal plan for a user on a specific date"""
        try:
            # Convert string date to datetime.date if needed
            if isinstance(date, str):
                date = datetime.strptime(date, '%Y-%m-%d').date()
            
            # Query meal plans for this user and date
            plans = MealPlanModel.query.filter_by(
                user_id=user_id,
                date=date
            ).all()
            
            # If no plans exist, return empty list
            if not plans:
                return []
            
            return [plan.to_dict() for plan in plans]
        except Exception as e:
            print(f"Error getting meal plan: {str(e)}")
            return []
    
    def generate_meal_plan(self, user, start_date, days=1):
        """Generate a meal plan for a user over a specified number of days"""
        try:
            # Get current last plan ID from database
            self.last_plan_id = self._get_last_plan_id()
            
            # Parse start date if it's a string
            if isinstance(start_date, str):
                start_date = datetime.strptime(start_date, '%Y-%m-%d').date()
            
            user_id = user['user_id']
            
            # Delete any existing meal plans for this date range
            self._delete_existing_plans(user_id, start_date, days)
            
            # Calculate daily targets based on user's TDEE and macronutrient goals
            daily_calories = user['tdee']
            protein_goal = user['protein_goal']
            carbs_goal = user['carbs_goal']
            fat_goal = user['fat_goal']
            
            # Define meal distribution
            meal_distribution = {
                'breakfast': 0.3,  # 30% of daily calories for breakfast
                'lunch': 0.35,     # 35% for lunch
                'dinner': 0.25,    # 25% for dinner
                'snack': 0.1       # 10% for snacks
            }
            
            # Generate meal plan for each day
            for day_offset in range(days):
                plan_date = start_date + timedelta(days=day_offset)
                
                for meal_type, calorie_percent in meal_distribution.items():
                    meal_calories = daily_calories * calorie_percent
                    
                    # Get suitable foods for this meal type
                    foods = self.food_db.suggest_foods(user, meal_type, limit=20)
                    
                    if not foods:
                        # If no specific foods for meal type, get general foods
                        foods = self.food_db.search_foods(limit=20)
                    
                    # Shuffle foods for variety
                    random.shuffle(foods)
                    
                    # Select foods to meet calorie and macro targets for this meal
                    selected_foods = self._select_foods_for_meal(
                        foods, 
                        target_calories=meal_calories,
                        target_protein=protein_goal * calorie_percent,
                        target_carbs=carbs_goal * calorie_percent,
                        target_fat=fat_goal * calorie_percent
                    )
                    
                    # Create meal plan entries for selected foods
                    for food in selected_foods:
                        meal_plan = MealPlanModel(
                            user_id=user_id,
                            date=plan_date,
                            meal_type=meal_type,
                            food_name=food['food_name'],
                            quantity=food['quantity'],
                            calories=food['calories'],
                            protein_g=food['protein_g'],
                            carbs_g=food['carbs_g'],
                            fat_g=food['fat_g'],
                            notes=f"Auto-generated for {meal_type}"
                        )
                        self.db.session.add(meal_plan)
            
            self.db.session.commit()
            return True
        except Exception as e:
            print(f"Error generating meal plan: {str(e)}")
            self.db.session.rollback()
            return False
    
    def _delete_existing_plans(self, user_id, start_date, days):
        """Delete existing meal plans for a date range"""
        end_date = start_date + timedelta(days=days-1)
        
        MealPlanModel.query.filter(
            MealPlanModel.user_id == user_id,
            MealPlanModel.date >= start_date,
            MealPlanModel.date <= end_date
        ).delete()
    
    def _select_foods_for_meal(self, foods, target_calories, target_protein, target_carbs, target_fat):
        """Select a combination of foods that meet nutritional targets for a meal"""
        selected_foods = []
        current_calories = 0
        current_protein = 0
        current_carbs = 0
        current_fat = 0
        
        # Try to get a balanced set of foods
        for food in foods:
            # Skip if this food would exceed targets by too much
            if current_calories + food['calories'] > target_calories * 1.1:
                continue
                
            # Determine appropriate quantity (default to 1)
            quantity = max(0.5, min(1.0, (target_calories - current_calories) / food['calories']))
            quantity = round(quantity, 1)  # Round to nearest 0.1
            
            if quantity < 0.3:  # Skip if quantity would be too small
                continue
                
            # Calculate nutrition with this quantity
            calories = food['calories'] * quantity
            protein = food['protein_g'] * quantity
            carbs = food['carbs_g'] * quantity
            fat = food['fat_g'] * quantity
            
            # Add food to meal plan
            selected_food = food.copy()
            selected_food['quantity'] = quantity
            selected_food['calories'] = round(calories, 2)
            selected_food['protein_g'] = round(protein, 2)
            selected_food['carbs_g'] = round(carbs, 2)
            selected_food['fat_g'] = round(fat, 2)
            
            selected_foods.append(selected_food)
            
            # Update current totals
            current_calories += calories
            current_protein += protein
            current_carbs += carbs
            current_fat += fat
            
            # Break if we've reached targets
            if (current_calories >= target_calories * 0.9 and 
                current_protein >= target_protein * 0.9):
                break
        
        # If we couldn't find enough foods, just add the first one with adjusted quantity
        if not selected_foods and foods:
            food = foods[0]
            quantity = target_calories / food['calories']
            quantity = max(0.5, min(2.0, quantity))  # Limit quantity to reasonable range
            quantity = round(quantity, 1)  # Round to nearest 0.1
            
            calories = food['calories'] * quantity
            protein = food['protein_g'] * quantity
            carbs = food['carbs_g'] * quantity
            fat = food['fat_g'] * quantity
            
            selected_food = food.copy()
            selected_food['quantity'] = quantity
            selected_food['calories'] = round(calories, 2)
            selected_food['protein_g'] = round(protein, 2)
            selected_food['carbs_g'] = round(carbs, 2)
            selected_food['fat_g'] = round(fat, 2)
            
            selected_foods.append(selected_food)
        
        return selected_foods
    
    def _generate_meal(self, meal_type, target_calories, dietary_preference, excluded_categories, user):
        """Generate a meal based on meal type, target calories, and preferences"""
        # Select a random template for this meal type
        template = random.choice(self.meal_templates[meal_type])
        
        meal_items = []
        total_calories = 0
        remaining_calories = target_calories
        
        # Select foods for each category in the template
        for i, category in enumerate(template['categories']):
            if category in excluded_categories:
                continue
            
            # Skip if category doesn't exist in food database
            available_foods = self.food_db.get_foods_by_category(category)
            if not available_foods:
                continue
            
            # Calculate target calories for this category
            category_calories = remaining_calories * (template['weights'][i] / sum(template['weights'][i:]))
            
            # Select a random food from this category
            valid_foods = [f for f in available_foods if self._is_food_valid(f, dietary_preference, user)]
            if not valid_foods:
                continue
            
            selected_food = random.choice(valid_foods)
            
            # Calculate quantity to match target calories
            if selected_food['calories'] > 0:
                quantity = min(category_calories / selected_food['calories'], 3.0)  # Cap at 3x portion
                quantity = max(0.25, quantity)  # Minimum 0.25 portion
                quantity = round(quantity * 4) / 4  # Round to nearest 0.25
            else:
                quantity = 1.0
            
            # Calculate nutrition for the selected food and quantity
            calories = selected_food['calories'] * quantity
            protein = selected_food['protein_g'] * quantity
            carbs = selected_food['carbs_g'] * quantity
            fat = selected_food['fat_g'] * quantity
            
            # Add to meal items
            meal_items.append({
                'food_name': selected_food['food_name'],
                'quantity': quantity,
                'calories': calories,
                'protein_g': protein,
                'carbs_g': carbs,
                'fat_g': fat
            })
            
            total_calories += calories
            remaining_calories -= calories
            
            # Stop if we've exceeded target calories
            if total_calories >= target_calories * 0.9:
                break
        
        # Add special notes based on user preferences or health conditions
        for item in meal_items:
            if 'allergies' in user and user['allergies'] != 'None':
                allergens = user['allergies'].split(',')
                for allergen in allergens:
                    if allergen.strip().lower() in item['food_name'].lower():
                        item['notes'] = f"Replace with non-{allergen.strip()} alternative"
            
            if 'medical_conditions' in user and user['medical_conditions'] != 'None':
                conditions = user['medical_conditions'].split(',')
                if 'Diabetes' in conditions and item['carbs_g'] > 30:
                    item['notes'] = "Reduce portion size for diabetes management"
                elif 'Hypertension' in conditions:
                    item['notes'] = "Low sodium version"
        
        return meal_items
    
    def _is_food_valid(self, food, dietary_preference, user):
        """Check if a food meets the user's dietary preferences and restrictions"""
        # Check dietary preference
        if dietary_preference == 'Vegetarian' and food['food_category'] == 'Non-Vegetarian':
            return False
        elif dietary_preference == 'Vegan' and food['food_category'] in ['Non-Vegetarian', 'Dairy']:
            return False
        
        # Check allergies
        if 'allergies' in user and user['allergies'] != 'None':
            allergens = user['allergies'].split(',')
            for allergen in allergens:
                if allergen.strip().lower() in food['food_name'].lower():
                    return False
        
        return True
    
    def adjust_plan_for_health_condition(self, meal_plan, health_condition):
        """Adjust a meal plan based on health conditions"""
        adjusted_plan = []
        
        for meal in meal_plan:
            adjusted_meal = meal.copy()
            
            if health_condition == 'Diabetes':
                # Adjust for diabetes (lower carbs, more fiber)
                if meal['carbs_g'] > 30:
                    adjusted_meal['quantity'] = meal['quantity'] * 0.75
                    adjusted_meal['calories'] = meal['calories'] * 0.75
                    adjusted_meal['carbs_g'] = meal['carbs_g'] * 0.75
                    adjusted_meal['protein_g'] = meal['protein_g'] * 0.75
                    adjusted_meal['fat_g'] = meal['fat_g'] * 0.75
                    adjusted_meal['notes'] = "Reduced portion for diabetes management"
            
            elif health_condition == 'Hypertension':
                # Lower sodium options
                adjusted_meal['notes'] = "Choose low-sodium preparation"
            
            elif health_condition == 'Cholesterol':
                # Lower fat options
                if meal['fat_g'] > 10:
                    adjusted_meal['notes'] = "Choose low-fat preparation method"
            
            adjusted_plan.append(adjusted_meal)
        
        return adjusted_plan

    def _get_last_plan_id(self):
        """Get the last plan ID from the database"""
        try:
            return db.session.query(db.func.max(MealPlanModel.plan_id)).scalar() or 0
        except Exception as e:
            print(f"Error getting last plan ID: {str(e)}")
            return 0 