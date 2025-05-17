import os
import pandas as pd
import getpass
from datetime import datetime
from models.database import db, UserModel, FoodModel, MealLogModel, MealPlanModel, WeightLogModel
from flask import Flask
from dotenv import load_dotenv
from werkzeug.security import generate_password_hash

load_dotenv()

def create_app():
    password = getpass.getpass("Enter MySQL root password: ")
    
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = f'mysql+pymysql://root:{password}@localhost/nutritrack'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    
    db.init_app(app)
    
    return app, password

def migrate_csv_to_db(app):
    with app.app_context():
        # Create tables
        db.create_all()
        
        # Check if data exists already
        if FoodModel.query.count() > 0:
            print("Database already has data. Skipping migration.")
            return
        
        print("Migrating CSV data to MySQL database...")
        
        # Migrate food data
        try:
            foods_df = pd.read_csv('data/indian_foods.csv')
            for _, row in foods_df.iterrows():
                food = FoodModel(
                    food_name=row['food_name'],
                    calories=row['calories'],
                    protein_g=row['protein_g'],
                    carbs_g=row['carbs_g'],
                    fat_g=row['fat_g'],
                    fiber_g=row['fiber_g'],
                    sugar_g=row['sugar_g'],
                    calcium_mg=row['calcium_mg'],
                    iron_mg=row['iron_mg'],
                    sodium_mg=row['sodium_mg'],
                    portion_size=row['portion_size'],
                    food_category=row['food_category']
                )
                db.session.add(food)
            db.session.commit()
            print(f"Migrated {len(foods_df)} food items")
        except Exception as e:
            print(f"Error migrating food data: {e}")
            db.session.rollback()
        
        # Migrate user data
        try:
            users_df = pd.read_csv('data/user_database.csv')
            for _, row in users_df.iterrows():
                default_password = f"default_{row['username']}"
                password_hash = generate_password_hash(default_password, method='pbkdf2:sha256')
                
                user = UserModel(
                    user_id=row['user_id'],
                    username=row['username'],
                    password_hash=password_hash,  # Set the hash directly
                    age=row['age'],
                    gender=row['gender'],
                    height_cm=row['height_cm'],
                    weight_kg=row['weight_kg'],
                    activity_level=row['activity_level'],
                    goal=row['goal'],
                    dietary_preference=row['dietary_preference'],
                    allergies=row['allergies'] if row['allergies'] != 'None' else None,
                    medical_conditions=row['medical_conditions'] if row['medical_conditions'] != 'None' else None,
                    bmr=row['bmr'],
                    tdee=row['tdee'],
                    protein_goal=row['protein_goal'],
                    carbs_goal=row['carbs_goal'],
                    fat_goal=row['fat_goal'],
                    creation_date=datetime.strptime(row['creation_date'], '%Y-%m-%d').date()
                )
                db.session.add(user)
            db.session.commit()
            print(f"Migrated {len(users_df)} users")
        except Exception as e:
            print(f"Error migrating user data: {e}")
            db.session.rollback()
        
        # Migrate meal logs
        try:
            meal_logs_df = pd.read_csv('data/meal_logs.csv')
            for _, row in meal_logs_df.iterrows():
                meal_log = MealLogModel(
                    log_id=row['log_id'],
                    user_id=row['user_id'],
                    date=datetime.strptime(row['date'], '%Y-%m-%d').date(),
                    meal_type=row['meal_type'],
                    food_name=row['food_name'],
                    quantity=row['quantity'],
                    calories=row['calories'],
                    protein_g=row['protein_g'],
                    carbs_g=row['carbs_g'],
                    fat_g=row['fat_g'],
                    notes=row['notes'] if pd.notna(row['notes']) else None
                )
                db.session.add(meal_log)
            db.session.commit()
            print(f"Migrated {len(meal_logs_df)} meal logs")
        except Exception as e:
            print(f"Error migrating meal log data: {e}")
            db.session.rollback()
        
        # Migrate meal plans
        try:
            meal_plans_df = pd.read_csv('data/meal_plans.csv')
            for _, row in meal_plans_df.iterrows():
                meal_plan = MealPlanModel(
                    plan_id=row['plan_id'],
                    user_id=row['user_id'],
                    date=datetime.strptime(row['date'], '%Y-%m-%d').date(),
                    meal_type=row['meal_type'],
                    food_name=row['food_name'],
                    quantity=row['quantity'],
                    calories=row['calories'],
                    protein_g=row['protein_g'],
                    carbs_g=row['carbs_g'],
                    fat_g=row['fat_g'],
                    notes=row['notes'] if pd.notna(row['notes']) else None
                )
                db.session.add(meal_plan)
            db.session.commit()
            print(f"Migrated {len(meal_plans_df)} meal plans")
        except Exception as e:
            print(f"Error migrating meal plan data: {e}")
            db.session.rollback()
        
        print("Migration completed successfully!")

if __name__ == "__main__":
    app, password = create_app()
    migrate_csv_to_db(app) 