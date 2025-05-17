import pandas as pd
import numpy as np
import random
from datetime import datetime, timedelta
import os
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import StandardScaler
from collections import Counter
from models.database import FoodModel, MealLogModel, UserModel, db

class RecommendationEngine:
    def __init__(self, food_db, db_connection):
        """Initialize the recommendation engine with food database"""
        self.food_db = food_db
        self.db = db_connection
        
        # Load user meal logs and ratings if available
        try:
            self.meal_logs_df = pd.read_csv(os.path.join(os.path.dirname(os.path.dirname(__file__)), 'data/meal_logs.csv'))
        except FileNotFoundError:
            self.meal_logs_df = pd.DataFrame()
    
    def get_recommendations(self, user, category=None, limit=10):
        """Get personalized food recommendations for a user"""
        # Get foods that match user's dietary preferences
        base_recommendations = self._get_preference_based_recommendations(user, category, limit=limit*2)
        
        # Get user's meal history
        meal_history = self._get_user_meal_history(user['user_id'])
        
        if not meal_history:
            # If no meal history, just return preference-based recommendations
            return base_recommendations[:limit]
        
        # Calculate frequency of foods in user's history
        food_counts = Counter()
        for meal in meal_history:
            food_counts[meal['food_name']] += 1
        
        # Get frequent foods
        frequent_foods = [food for food, count in food_counts.most_common(5)]
        
        # Get similar foods to frequent foods
        similar_foods = []
        for food_name in frequent_foods:
            similar = self._get_similar_foods(food_name, category, limit=3)
            similar_foods.extend(similar)
        
        # Combine recommendations
        all_recommendations = []
        
        # Add similar foods first (based on user history)
        for food in similar_foods:
            if food not in all_recommendations and food['food_name'] not in [f['food_name'] for f in all_recommendations]:
                all_recommendations.append(food)
        
        # Add preference-based recommendations
        for food in base_recommendations:
            if food not in all_recommendations and food['food_name'] not in [f['food_name'] for f in all_recommendations]:
                all_recommendations.append(food)
        
        # Return limited number of recommendations
        return all_recommendations[:limit]
    
    def _get_preference_based_recommendations(self, user, category=None, limit=10):
        """Get recommendations based on user preferences"""
        query = FoodModel.query
        
        # Filter by category if specified
        if category:
            query = query.filter(FoodModel.food_category == category)
        
        # Apply dietary preference filters
        if 'dietary_preference' in user:
            preference = user['dietary_preference'].lower()
            
            if preference == 'vegetarian':
                # Filter out non-vegetarian categories
                query = query.filter(
                    ~FoodModel.food_category.in_(['Meat', 'Seafood', 'Poultry'])
                )
            elif preference == 'vegan':
                # Filter out animal products
                query = query.filter(
                    ~FoodModel.food_category.in_(['Meat', 'Seafood', 'Poultry', 'Dairy', 'Eggs'])
                )
        
        # Apply nutrition filters based on user goals
        if 'goal' in user:
            goal = user['goal'].lower()
            
            if goal == 'lose_weight':
                # Prefer lower calorie foods
                query = query.order_by(FoodModel.calories)
            elif goal == 'build_muscle':
                # Prefer higher protein foods
                query = query.order_by(db.desc(FoodModel.protein_g))
        else:
            # Default sorting - random selection for variety
            query = query.order_by(db.func.random())
        
        # Get foods
        foods = query.limit(limit).all()
        return [food.to_dict() for food in foods]
    
    def _get_user_meal_history(self, user_id, days=30):
        """Get a user's meal history for the past X days"""
        # Get current date
        today = datetime.now().date()
        
        # Calculate date from 30 days ago
        start_date = today - pd.Timedelta(days=days)
        
        # Query meal logs for this user within date range
        meals = MealLogModel.query.filter(
            MealLogModel.user_id == user_id,
            MealLogModel.date >= start_date,
            MealLogModel.date <= today
        ).all()
        
        return [meal.to_dict() for meal in meals]
    
    def _get_similar_foods(self, food_name, category=None, limit=5):
        """Get foods similar to a specified food"""
        # Get the reference food
        ref_food = FoodModel.query.filter(FoodModel.food_name == food_name).first()
        if not ref_food:
            return []
        
        # Build query for potential similar foods
        query = FoodModel.query.filter(FoodModel.food_name != food_name)
        
        # Filter by category if specified, otherwise use the same category as reference food
        if category:
            query = query.filter(FoodModel.food_category == category)
        else:
            query = query.filter(FoodModel.food_category == ref_food.food_category)
        
        # Get all potential similar foods
        potential_similar = query.all()
        
        if not potential_similar:
            return []
        
        # Convert to dictionaries
        ref_food_dict = ref_food.to_dict()
        similar_foods_dict = [food.to_dict() for food in potential_similar]
        
        # Extract nutritional features for similarity calculation
        features = ['calories', 'protein_g', 'carbs_g', 'fat_g']
        
        ref_features = [ref_food_dict[f] for f in features]
        similar_features = [[food[f] for f in features] for food in similar_foods_dict]
        
        # Calculate similarity
        similarities = []
        for i, food in enumerate(similar_foods_dict):
            # Calculate Euclidean distance (lower is more similar)
            distance = np.sqrt(sum((ref_features[j] - similar_features[i][j])**2 for j in range(len(features))))
            similarities.append((food, distance))
        
        # Sort by similarity (smaller distance is more similar)
        similarities.sort(key=lambda x: x[1])
        
        # Return most similar foods
        return [item[0] for item in similarities[:limit]]
    
    def _collaborative_filtering_recommendations(self, user, category=None, limit=5):
        """Generate recommendations based on similar users' preferences"""
        user_id = user['user_id']
        
        # Get user's meal history
        user_meals = self.meal_logs_df[self.meal_logs_df['user_id'] == user_id]
        
        if user_meals.empty:
            return self._content_based_recommendations(user, category, limit)
        
        # Find favorite foods (foods eaten more than once)
        food_counts = user_meals['food_name'].value_counts()
        favorite_foods = list(food_counts[food_counts > 1].index)
        
        # Find users with similar preferences
        similar_users = set()
        for food in favorite_foods:
            # Find other users who ate this food
            other_users = set(self.meal_logs_df[
                (self.meal_logs_df['food_name'] == food) & 
                (self.meal_logs_df['user_id'] != user_id)
            ]['user_id'].unique())
            similar_users.update(other_users)
        
        # If no similar users found, fall back to content-based
        if not similar_users:
            return self._content_based_recommendations(user, category, limit)
        
        # Get foods eaten by similar users but not by this user
        eaten_foods = set(user_meals['food_name'].unique())
        potential_recommendations = []
        
        for similar_user in similar_users:
            similar_user_meals = self.meal_logs_df[self.meal_logs_df['user_id'] == similar_user]
            for _, meal in similar_user_meals.iterrows():
                if meal['food_name'] not in eaten_foods:
                    food_info = self.food_db.get_food_by_name(meal['food_name'])
                    if food_info:
                        if category and food_info['food_category'] != category:
                            continue
                        potential_recommendations.append(food_info)
        
        # Filter for dietary preferences and health conditions
        valid_recommendations = [
            food for food in potential_recommendations 
            if self._is_food_suitable(food, user)
        ]
        
        # Remove duplicates and get top recommendations
        unique_recommendations = []
        seen_foods = set()
        
        for food in valid_recommendations:
            if food['food_name'] not in seen_foods:
                seen_foods.add(food['food_name'])
                unique_recommendations.append(food)
                if len(unique_recommendations) >= limit:
                    break
        
        # If we don't have enough recommendations, add some content-based ones
        if len(unique_recommendations) < limit:
            content_based = self._content_based_recommendations(
                user, category, limit - len(unique_recommendations)
            )
            for food in content_based:
                if food['food_name'] not in seen_foods:
                    unique_recommendations.append(food)
        
        return unique_recommendations[:limit]
    
    def _content_based_recommendations(self, user, category=None, limit=5):
        """Generate recommendations based on user profile and dietary needs"""
        # Get all foods from database
        if category:
            all_foods = self.food_db.get_foods_by_category(category)
        else:
            all_foods = self.food_db.search_foods()
        
        # Filter for dietary preferences and health conditions
        valid_foods = [food for food in all_foods if self._is_food_suitable(food, user)]
        
        if not valid_foods:
            return []
        
        # Score foods based on nutritional needs
        scored_foods = []
        for food in valid_foods:
            score = self._calculate_food_score(food, user)
            scored_foods.append((food, score))
        
        # Sort by score (higher is better)
        scored_foods.sort(key=lambda x: x[1], reverse=True)
        
        # Return top recommendations
        recommendations = [food for food, _ in scored_foods[:limit]]
        return recommendations
    
    def _is_food_suitable(self, food, user):
        """Check if a food is suitable for a user's dietary restrictions and preferences"""
        dietary_preference = user.get('dietary_preference', 'Non-Vegetarian')
        
        # Check dietary preference
        if dietary_preference == 'Vegetarian' and food['food_category'] == 'Non-Vegetarian':
            return False
        elif dietary_preference == 'Vegan' and food['food_category'] in ['Non-Vegetarian', 'Dairy']:
            return False
        elif dietary_preference == 'Jain':
            if food['food_category'] == 'Non-Vegetarian':
                return False
            # Jain diet typically avoids root vegetables, but we may not have this info
        
        # Check allergies
        if 'allergies' in user and user['allergies'] != 'None':
            allergens = user['allergies'].split(',')
            for allergen in allergens:
                if allergen.strip().lower() in food['food_name'].lower():
                    return False
        
        # Check for medical conditions
        if 'medical_conditions' in user and user['medical_conditions'] != 'None':
            conditions = user['medical_conditions'].split(',')
            
            if 'Diabetes' in conditions and food['carbs_g'] > 20 and food['fiber_g'] < 3:
                # For diabetes: avoid high carb, low fiber foods
                # This is a simplified check and would need refinement
                return False
            
            if 'Hypertension' in conditions and 'sodium_mg' in food and food['sodium_mg'] > 400:
                # For hypertension: avoid high sodium foods
                return False
            
            if 'Cholesterol' in conditions and food['fat_g'] > 15:
                # For high cholesterol: avoid high fat foods
                return False
        
        return True
    
    def _calculate_food_score(self, food, user):
        """Calculate a score for a food based on how well it matches user's nutritional needs"""
        score = 0
        
        # Base score
        score += 50
        
        # Check protein needs
        daily_protein_goal = user.get('protein_goal', 0)
        if daily_protein_goal > 0:
            protein_contribution = food['protein_g'] / daily_protein_goal
            score += protein_contribution * 20  # More weight for protein
        
        # Check macronutrient balance
        total_macros = food['protein_g'] + food['carbs_g'] + food['fat_g']
        if total_macros > 0:
            protein_ratio = food['protein_g'] / total_macros
            carbs_ratio = food['carbs_g'] / total_macros
            fat_ratio = food['fat_g'] / total_macros
            
            # Adjust score based on user's goal
            if user.get('goal') == 'Weight Loss':
                # For weight loss: favor protein, moderate fat, lower carbs
                score += protein_ratio * 15
                score += (1 - carbs_ratio) * 10
            elif user.get('goal') == 'Muscle Gain':
                # For muscle gain: favor protein and adequate carbs
                score += protein_ratio * 20
                score += carbs_ratio * 10
            else:  # Maintain Weight
                # For maintenance: balanced diet
                balanced_score = (1 - abs(protein_ratio - 0.3)) + (1 - abs(carbs_ratio - 0.4)) + (1 - abs(fat_ratio - 0.3))
                score += balanced_score * 10
        
        # Adjust for medical conditions
        if 'medical_conditions' in user and user['medical_conditions'] != 'None':
            conditions = user['medical_conditions'].split(',')
            
            if 'Diabetes' in conditions:
                # Favor lower carb, higher fiber foods
                if 'fiber_g' in food and food['fiber_g'] > 0:
                    fiber_to_carb_ratio = min(food['fiber_g'] / max(food['carbs_g'], 1), 1)
                    score += fiber_to_carb_ratio * 15
                score -= (food['carbs_g'] / 50) * 10  # Penalize high carb foods
            
            if 'Cholesterol' in conditions:
                # Penalize high fat foods
                score -= (food['fat_g'] / 20) * 15
        
        # Add some randomness to provide variety
        score += random.uniform(0, 10)
        
        return score
    
    def get_meal_recommendations(self, user, meal_type, date=None):
        """Get meal-specific recommendations based on what's already eaten that day"""
        if date is None:
            date = datetime.now().strftime('%Y-%m-%d')
        
        # Get what the user has already eaten today
        today_meals = self.meal_logs_df[
            (self.meal_logs_df['user_id'] == user['user_id']) & 
            (self.meal_logs_df['date'] == date)
        ]
        
        # Calculate remaining nutritional needs
        daily_calories = user.get('tdee', 2000)
        daily_protein = user.get('protein_goal', 0)
        daily_carbs = user.get('carbs_goal', 0)
        daily_fat = user.get('fat_goal', 0)
        
        if user.get('goal') == 'Weight Loss':
            daily_calories *= 0.8  # 20% deficit
        elif user.get('goal') == 'Muscle Gain':
            daily_calories *= 1.1  # 10% surplus
        
        consumed_calories = today_meals['calories'].sum() if not today_meals.empty else 0
        consumed_protein = today_meals['protein_g'].sum() if not today_meals.empty else 0
        consumed_carbs = today_meals['carbs_g'].sum() if not today_meals.empty else 0
        consumed_fat = today_meals['fat_g'].sum() if not today_meals.empty else 0
        
        remaining_calories = max(daily_calories - consumed_calories, 0)
        remaining_protein = max(daily_protein - consumed_protein, 0)
        remaining_carbs = max(daily_carbs - consumed_carbs, 0)
        remaining_fat = max(daily_fat - consumed_fat, 0)
        
        # Define meal type calorie allocation
        meal_type_alloc = {
            'Breakfast': 0.25,
            'Lunch': 0.35,
            'Dinner': 0.3,
            'Snack': 0.1
        }
        
        target_calories = remaining_calories * meal_type_alloc.get(meal_type, 0.25)
        
        # Get common foods for this meal type
        meal_type_foods = self.meal_logs_df[self.meal_logs_df['meal_type'] == meal_type]
        common_categories = meal_type_foods['food_name'].value_counts().index[:10].tolist()
        
        # Get recommendations from these categories
        recommendations = []
        for food_name in common_categories:
            food = self.food_db.get_food_by_name(food_name)
            if food and self._is_food_suitable(food, user):
                # Calculate portion size to match target calories
                if food['calories'] > 0:
                    portion = min(target_calories / food['calories'], 2.0)
                    portion = max(0.5, portion)
                else:
                    portion = 1.0
                
                food_with_portion = food.copy()
                food_with_portion['recommended_portion'] = round(portion * 4) / 4  # Round to nearest 0.25
                food_with_portion['calories_per_portion'] = food['calories'] * food_with_portion['recommended_portion']
                
                recommendations.append(food_with_portion)
                if len(recommendations) >= 5:
                    break
        
        # If we don't have enough recommendations, add general ones
        if len(recommendations) < 5:
            general_recs = self.get_recommendations(user, None, 5 - len(recommendations))
            recommendations.extend(general_recs)
        
        return recommendations 