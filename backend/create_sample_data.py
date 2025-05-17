import os
import random
from datetime import datetime, timedelta
from flask import Flask
from models.database import db, UserModel, FoodModel, MealLogModel, MealPlanModel
from dotenv import load_dotenv

load_dotenv()

def create_sample_data():
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///nutritrack.db'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    
    db.init_app(app)
    
    with app.app_context():
        # Create tables
        db.create_all()
        
        # Create test user if not exists
        user = UserModel.query.filter_by(username='testuser').first()
        if not user:
            user = UserModel(
                username='testuser',
                age=30,
                gender='Male',
                height_cm=175.0,
                weight_kg=70.0,
                activity_level='Moderate',
                goal='Maintain',
                dietary_preference='any',
                bmr=1700,
                tdee=2200,
                protein_goal=150,
                carbs_goal=200,
                fat_goal=70
            )
            user.set_password('password123')
            db.session.add(user)
            db.session.commit()
            print("Test user created.")
        
        # Create sample foods if not exists
        if FoodModel.query.count() == 0:
            foods_data = [
                {'name': 'Chicken Breast', 'calories': 165, 'protein_g': 31, 'carbs_g': 0, 'fat_g': 3.6, 'category': 'Meat'},
                {'name': 'Brown Rice', 'calories': 216, 'protein_g': 5, 'carbs_g': 45, 'fat_g': 1.8, 'category': 'Grains'},
                {'name': 'Broccoli', 'calories': 55, 'protein_g': 3.7, 'carbs_g': 11.2, 'fat_g': 0.6, 'category': 'Vegetables'},
                {'name': 'Salmon', 'calories': 206, 'protein_g': 22, 'carbs_g': 0, 'fat_g': 13, 'category': 'Fish'},
                {'name': 'Eggs', 'calories': 155, 'protein_g': 13, 'carbs_g': 1.1, 'fat_g': 11, 'category': 'Dairy'},
                {'name': 'Apple', 'calories': 95, 'protein_g': 0.5, 'carbs_g': 25, 'fat_g': 0.3, 'category': 'Fruits'},
                {'name': 'Greek Yogurt', 'calories': 100, 'protein_g': 17, 'carbs_g': 6, 'fat_g': 0.4, 'category': 'Dairy'},
                {'name': 'Spinach', 'calories': 23, 'protein_g': 2.9, 'carbs_g': 3.6, 'fat_g': 0.4, 'category': 'Vegetables'},
                {'name': 'Oatmeal', 'calories': 158, 'protein_g': 6, 'carbs_g': 27, 'fat_g': 3, 'category': 'Grains'},
                {'name': 'Sweet Potato', 'calories': 115, 'protein_g': 2.1, 'carbs_g': 27, 'fat_g': 0.1, 'category': 'Vegetables'}
            ]
            
            for food_data in foods_data:
                food = FoodModel(
                    food_name=food_data['name'],
                    calories=food_data['calories'],
                    protein_g=food_data['protein_g'],
                    carbs_g=food_data['carbs_g'],
                    fat_g=food_data['fat_g'],
                    food_category=food_data['category'],
                    fiber_g=random.uniform(0.5, 5.0),
                    sugar_g=random.uniform(0.5, 10.0),
                    calcium_mg=random.uniform(10, 300),
                    iron_mg=random.uniform(0.5, 4.0),
                    sodium_mg=random.uniform(10, 500),
                    portion_size="100g"
                )
                db.session.add(food)
            
            db.session.commit()
            print("Sample foods created.")
        
        foods = FoodModel.query.all()
        today = datetime.now().date()
        meal_types = ['Breakfast', 'Lunch', 'Dinner', 'Snack']
        
        # Create meal logs for today
        existing_logs = MealLogModel.query.filter_by(user_id=user.user_id, date=today).first()
        if existing_logs:
            print("Meal logs already exist for today.")
        else:
            # Add meal logs for today
            for i, meal_type in enumerate(meal_types):
                if i >= len(foods):
                    break
                    
                food = foods[i]
                meal_log = MealLogModel(
                    user_id=user.user_id,
                    date=today,
                    meal_type=meal_type,
                    food_name=food.food_name,
                    quantity=1.0,
                    calories=food.calories,
                    protein_g=food.protein_g,
                    carbs_g=food.carbs_g,
                    fat_g=food.fat_g,
                    notes="Sample meal log"
                )
                db.session.add(meal_log)
            
            db.session.commit()
            print("Sample meal logs created for today.")
        
        # Create meal plans for tomorrow
        tomorrow = today + timedelta(days=1)
        
        # Check if meal plans already exist
        existing_plans = MealPlanModel.query.filter_by(user_id=user.user_id, date=tomorrow).first()
        if existing_plans:
            print("Meal plans already exist for tomorrow.")
        else:
            # Add meal plans for tomorrow
            for i, meal_type in enumerate(meal_types):
                if i >= len(foods):
                    break
                    
                food = foods[i]
                meal_plan = MealPlanModel(
                    user_id=user.user_id,
                    date=tomorrow,
                    meal_type=meal_type,
                    food_name=food.food_name,
                    quantity=1.0,
                    calories=food.calories,
                    protein_g=food.protein_g,
                    carbs_g=food.carbs_g,
                    fat_g=food.fat_g,
                    notes="Sample meal plan"
                )
                db.session.add(meal_plan)
            
            db.session.commit()
            print("Sample meal plans created for tomorrow.")
        
        print("Sample data creation completed.")

if __name__ == "__main__":
    create_sample_data() 