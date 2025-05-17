import pandas as pd
import os
import numpy as np
from datetime import datetime
from werkzeug.security import generate_password_hash
from .database import UserModel, db

class User:
    def __init__(self, db_connection):
        self.db = db_connection
    
    def _calculate_nutrition_goals(self, user_data):
        """Calculate BMR, TDEE, and macronutrient goals based on user data"""
        age = user_data['age']
        gender = user_data['gender']
        weight_kg = user_data['weight_kg']
        height_cm = user_data['height_cm']
        activity_level = user_data['activity_level']
        goal = user_data['goal']
        
        # Calculate BMR using Mifflin-St Jeor Equation
        if gender.lower() == 'male':
            bmr = 10 * weight_kg + 6.25 * height_cm - 5 * age + 5
        else:  # female
            bmr = 10 * weight_kg + 6.25 * height_cm - 5 * age - 161
        
        # Calculate TDEE based on activity level
        activity_multipliers = {
            'sedentary': 1.2,
            'lightly_active': 1.375,
            'moderately_active': 1.55,
            'very_active': 1.725,
            'extra_active': 1.9
        }
        
        multiplier = activity_multipliers.get(activity_level.lower(), 1.2)
        tdee = bmr * multiplier
        
        # Adjust based on goal
        goal_adjustments = {
            'lose_weight': -500,  # Calorie deficit
            'maintain_weight': 0,
            'gain_weight': 500,   # Calorie surplus
            'build_muscle': 300   # Moderate surplus for muscle gain
        }
        
        tdee_adjusted = tdee + goal_adjustments.get(goal.lower(), 0)
        
        # Calculate macronutrient goals based on common recommendations
        if goal.lower() == 'build_muscle':
            protein_pct = 0.3  # 30% of calories from protein
            fat_pct = 0.25     # 25% of calories from fat
            carbs_pct = 0.45   # 45% of calories from carbohydrates
        elif goal.lower() == 'lose_weight':
            protein_pct = 0.35  # Higher protein for satiety during weight loss
            fat_pct = 0.3
            carbs_pct = 0.35    # Lower carbs for weight loss
        else:
            protein_pct = 0.25  # 25% of calories from protein
            fat_pct = 0.3      # 30% of calories from fat
            carbs_pct = 0.45   # 45% of calories from carbohydrates
        
        protein_cals = tdee_adjusted * protein_pct
        fat_cals = tdee_adjusted * fat_pct
        carbs_cals = tdee_adjusted * carbs_pct
        
        # Convert calories to grams
        protein_g = protein_cals / 4  # 1g protein = 4 calories
        fat_g = fat_cals / 9         # 1g fat = 9 calories
        carbs_g = carbs_cals / 4     # 1g carbs = 4 calories
        
        return {
            'bmr': round(bmr),
            'tdee': round(tdee_adjusted),
            'protein_goal': round(protein_g),
            'carbs_goal': round(carbs_g),
            'fat_goal': round(fat_g)
        }

    def create_user(self, user_data):
        """Create a new user in the database"""
        try:
            # Calculate nutrition goals
            nutrition_goals = self._calculate_nutrition_goals(user_data)
            
            # Create new UserModel instance
            new_user = UserModel(
                username=user_data['username'],
                age=user_data['age'],
                gender=user_data['gender'],
                height_cm=user_data['height_cm'],
                weight_kg=user_data['weight_kg'],
                activity_level=user_data['activity_level'],
                goal=user_data['goal'],
                dietary_preference=user_data['dietary_preference'],
                allergies=user_data.get('allergies', ''),
                medical_conditions=user_data.get('medical_conditions', ''),
                bmr=nutrition_goals['bmr'],
                tdee=nutrition_goals['tdee'],
                protein_goal=nutrition_goals['protein_goal'],
                carbs_goal=nutrition_goals['carbs_goal'],
                fat_goal=nutrition_goals['fat_goal'],
                creation_date=datetime.now()
            )
            
            # Set password hash
            if 'password' in user_data:
                new_user.set_password(user_data['password'])
            
            # Add to database and commit
            self.db.session.add(new_user)
            self.db.session.commit()
            
            return new_user.user_id
        except Exception as e:
            print(f"Error creating user: {str(e)}")
            self.db.session.rollback()
            return None

    def get_user_by_id(self, user_id):
        """Get user by ID"""
        user = UserModel.query.get(user_id)
        if user:
            # Convert to dictionary and exclude password hash
            user_dict = user.to_dict()
            return user_dict
        return None

    def get_user_by_username(self, username):
        """Get user by username"""
        user = UserModel.query.filter_by(username=username).first()
        if user:
            # Convert to dictionary and exclude password hash
            user_dict = user.to_dict()
            return user_dict
        return None

    def update_user(self, user_id, user_data):
        """Update user information"""
        try:
            user = UserModel.query.get(user_id)
            if not user:
                return False
            
            # Update fields that are present in user_data
            if 'username' in user_data:
                user.username = user_data['username']
            if 'age' in user_data:
                user.age = user_data['age']
            if 'gender' in user_data:
                user.gender = user_data['gender']
            if 'height_cm' in user_data:
                user.height_cm = user_data['height_cm']
            if 'weight_kg' in user_data:
                user.weight_kg = user_data['weight_kg']
            if 'activity_level' in user_data:
                user.activity_level = user_data['activity_level']
            if 'goal' in user_data:
                user.goal = user_data['goal']
            if 'dietary_preference' in user_data:
                user.dietary_preference = user_data['dietary_preference']
            if 'allergies' in user_data:
                user.allergies = user_data['allergies']
            if 'medical_conditions' in user_data:
                user.medical_conditions = user_data['medical_conditions']
            if 'password' in user_data:
                user.set_password(user_data['password'])
            
            # Recalculate nutrition goals if relevant fields changed
            if any(key in user_data for key in ['age', 'gender', 'height_cm', 'weight_kg', 'activity_level', 'goal']):
                # Create a dictionary with current user data
                current_data = {
                    'age': user.age,
                    'gender': user.gender,
                    'height_cm': user.height_cm,
                    'weight_kg': user.weight_kg,
                    'activity_level': user.activity_level,
                    'goal': user.goal
                }
                
                nutrition_goals = self._calculate_nutrition_goals(current_data)
                user.bmr = nutrition_goals['bmr']
                user.tdee = nutrition_goals['tdee']
                user.protein_goal = nutrition_goals['protein_goal']
                user.carbs_goal = nutrition_goals['carbs_goal']
                user.fat_goal = nutrition_goals['fat_goal']
            
            self.db.session.commit()
            return True
        except Exception as e:
            print(f"Error updating user: {str(e)}")
            self.db.session.rollback()
            return False 