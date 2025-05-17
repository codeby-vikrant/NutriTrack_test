import pandas as pd
import os
from datetime import datetime
from .database import MealLogModel, db

class MealLogger:
    def __init__(self, db_connection):
        self.db = db_connection
    
    def add_meal(self, user_id, meal_data):
        """Add a new meal log for a user"""
        try:
            # Create new meal log
            new_meal = MealLogModel(
                user_id=user_id,
                date=datetime.strptime(meal_data['date'], '%Y-%m-%d').date() if isinstance(meal_data['date'], str) else meal_data['date'],
                meal_type=meal_data['meal_type'],
                food_name=meal_data['food_name'],
                quantity=meal_data['quantity'],
                calories=meal_data.get('calories', 0),
                protein_g=meal_data.get('protein_g', 0),
                carbs_g=meal_data.get('carbs_g', 0),
                fat_g=meal_data.get('fat_g', 0),
                notes=meal_data.get('notes', '')
            )
            
            # Add to database and commit
            self.db.session.add(new_meal)
            self.db.session.commit()
            
            return new_meal.log_id
        except Exception as e:
            print(f"Error adding meal: {str(e)}")
            self.db.session.rollback()
            return None
    
    def get_meals_by_date(self, user_id, date):
        """Get all meals for a user on a specific date"""
        try:
            # Convert string date to datetime.date if needed
            if isinstance(date, str):
                date = datetime.strptime(date, '%Y-%m-%d').date()
            
            # Query meals for this user and date
            meals = MealLogModel.query.filter_by(
                user_id=user_id,
                date=date
            ).all()
            
            return [meal.to_dict() for meal in meals]
        except Exception as e:
            print(f"Error getting meals: {str(e)}")
            return []
    
    def get_meals_by_date_range(self, user_id, start_date, end_date):
        """Get all meals for a user within a date range"""
        try:
            # Convert string dates to datetime.date if needed
            if isinstance(start_date, str):
                start_date = datetime.strptime(start_date, '%Y-%m-%d').date()
            if isinstance(end_date, str):
                end_date = datetime.strptime(end_date, '%Y-%m-%d').date()
            
            # Query meals for this user within date range
            meals = MealLogModel.query.filter(
                MealLogModel.user_id == user_id,
                MealLogModel.date >= start_date,
                MealLogModel.date <= end_date
            ).order_by(MealLogModel.date).all()
            
            return [meal.to_dict() for meal in meals]
        except Exception as e:
            print(f"Error getting meals: {str(e)}")
            return []
    
    def delete_meal(self, user_id, log_id):
        """Delete a meal log entry"""
        try:
            # Find the meal log
            meal = MealLogModel.query.filter_by(
                user_id=user_id,
                log_id=log_id
            ).first()
            
            if not meal:
                return False
            
            # Delete the meal log
            self.db.session.delete(meal)
            self.db.session.commit()
            
            return True
        except Exception as e:
            print(f"Error deleting meal: {str(e)}")
            self.db.session.rollback()
            return False
    
    def get_meal_summary(self, user_id, date):
        """Get a summary of nutritional intake for a day"""
        meals = self.get_meals_by_date(user_id, date)
        
        if not meals:
            return {
                'total_calories': 0,
                'total_protein': 0,
                'total_carbs': 0,
                'total_fat': 0,
                'meal_count': 0
            }
        
        summary = {
            'total_calories': sum(meal['calories'] for meal in meals),
            'total_protein': sum(meal['protein_g'] for meal in meals),
            'total_carbs': sum(meal['carbs_g'] for meal in meals),
            'total_fat': sum(meal['fat_g'] for meal in meals),
            'meal_count': len(meals),
            'meals_by_type': {}
        }
        
        # Group meals by type
        for meal in meals:
            meal_type = meal['meal_type']
            if meal_type not in summary['meals_by_type']:
                summary['meals_by_type'][meal_type] = []
            summary['meals_by_type'][meal_type].append(meal)
        
        return summary

    def get_daily_nutrition(self, user_id, date):
        """Calculate total nutritional values for a specific day"""
        day_meals = self.logs_df[(self.logs_df['user_id'] == user_id) & (self.logs_df['date'] == date)]
        
        if day_meals.empty:
            return {
                'date': date,
                'calories': 0,
                'protein_g': 0,
                'carbs_g': 0,
                'fat_g': 0
            }
        
        # Sum up nutritional values
        total_nutrition = {
            'date': date,
            'calories': day_meals['calories'].sum(),
            'protein_g': day_meals['protein_g'].sum(),
            'carbs_g': day_meals['carbs_g'].sum(),
            'fat_g': day_meals['fat_g'].sum()
        }
        
        return total_nutrition
    
    def get_meal_distribution(self, user_id, date):
        """Get distribution of calories across meal types for a day"""
        day_meals = self.logs_df[(self.logs_df['user_id'] == user_id) & (self.logs_df['date'] == date)]
        
        if day_meals.empty:
            return []
        
        # Group by meal type and sum calories
        meal_distribution = day_meals.groupby('meal_type')['calories'].sum().reset_index()
        
        return meal_distribution.to_dict(orient='records') 