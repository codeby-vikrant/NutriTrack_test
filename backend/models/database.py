from flask_sqlalchemy import SQLAlchemy
from datetime import datetime
import hashlib

db = SQLAlchemy()

class UserModel(db.Model):
    __tablename__ = 'users'
    
    user_id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(50), unique=True, nullable=False)
    password_hash = db.Column(db.String(128), nullable=False)
    age = db.Column(db.Integer, nullable=False)
    gender = db.Column(db.String(10), nullable=False)
    height_cm = db.Column(db.Float, nullable=False)
    weight_kg = db.Column(db.Float, nullable=False)
    activity_level = db.Column(db.String(20), nullable=False)
    goal = db.Column(db.String(20), nullable=False)
    dietary_preference = db.Column(db.String(20), nullable=False)
    allergies = db.Column(db.String(100), nullable=True)
    medical_conditions = db.Column(db.String(100), nullable=True)
    bmr = db.Column(db.Float, nullable=True)
    tdee = db.Column(db.Float, nullable=True)
    protein_goal = db.Column(db.Float, nullable=True)
    carbs_goal = db.Column(db.Float, nullable=True)
    fat_goal = db.Column(db.Float, nullable=True)
    creation_date = db.Column(db.Date, default=datetime.utcnow)

    # Relationships
    meal_logs = db.relationship('MealLogModel', backref='user', lazy=True, cascade="all, delete-orphan")
    meal_plans = db.relationship('MealPlanModel', backref='user', lazy=True, cascade="all, delete-orphan")
    
    def set_password(self, password):
        self.password_hash = hashlib.md5(password.encode()).hexdigest()
        
    def check_password(self, password):
        return self.password_hash == hashlib.md5(password.encode()).hexdigest()
    
    def to_dict(self):
        return {
            'user_id': self.user_id,
            'username': self.username,
            'age': self.age,
            'gender': self.gender,
            'height_cm': self.height_cm,
            'weight_kg': self.weight_kg,
            'activity_level': self.activity_level,
            'goal': self.goal,
            'dietary_preference': self.dietary_preference,
            'allergies': self.allergies,
            'medical_conditions': self.medical_conditions,
            'bmr': self.bmr,
            'tdee': self.tdee,
            'protein_goal': self.protein_goal,
            'carbs_goal': self.carbs_goal,
            'fat_goal': self.fat_goal,
            'creation_date': self.creation_date.strftime('%Y-%m-%d') if self.creation_date else None
        }


class FoodModel(db.Model):
    __tablename__ = 'foods'
    
    food_id = db.Column(db.Integer, primary_key=True)
    food_name = db.Column(db.String(100), nullable=False)
    calories = db.Column(db.Float, nullable=False)
    protein_g = db.Column(db.Float, nullable=False)
    carbs_g = db.Column(db.Float, nullable=False)
    fat_g = db.Column(db.Float, nullable=False)
    fiber_g = db.Column(db.Float, nullable=True)
    sugar_g = db.Column(db.Float, nullable=True)
    calcium_mg = db.Column(db.Float, nullable=True)
    iron_mg = db.Column(db.Float, nullable=True)
    sodium_mg = db.Column(db.Float, nullable=True)
    portion_size = db.Column(db.String(50), nullable=True)
    food_category = db.Column(db.String(50), nullable=False)
    
    def to_dict(self):
        return {
            'food_id': self.food_id,
            'food_name': self.food_name,
            'calories': self.calories,
            'protein_g': self.protein_g,
            'carbs_g': self.carbs_g,
            'fat_g': self.fat_g,
            'fiber_g': self.fiber_g,
            'sugar_g': self.sugar_g,
            'calcium_mg': self.calcium_mg,
            'iron_mg': self.iron_mg,
            'sodium_mg': self.sodium_mg,
            'portion_size': self.portion_size,
            'food_category': self.food_category
        }


class MealLogModel(db.Model):
    __tablename__ = 'meal_logs'
    
    log_id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.user_id'), nullable=False)
    date = db.Column(db.Date, nullable=False)
    meal_type = db.Column(db.String(20), nullable=False)
    food_name = db.Column(db.String(100), nullable=False)
    quantity = db.Column(db.Float, nullable=False, default=1.0)
    calories = db.Column(db.Float, nullable=False)
    protein_g = db.Column(db.Float, nullable=False)
    carbs_g = db.Column(db.Float, nullable=False)
    fat_g = db.Column(db.Float, nullable=False)
    notes = db.Column(db.String(200), nullable=True)
    
    def to_dict(self):
        return {
            'log_id': self.log_id,
            'user_id': self.user_id,
            'date': self.date.strftime('%Y-%m-%d') if self.date else None,
            'meal_type': self.meal_type,
            'food_name': self.food_name,
            'quantity': self.quantity,
            'calories': self.calories,
            'protein_g': self.protein_g,
            'carbs_g': self.carbs_g,
            'fat_g': self.fat_g,
            'notes': self.notes
        }


class MealPlanModel(db.Model):
    __tablename__ = 'meal_plans'
    
    plan_id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.user_id'), nullable=False)
    date = db.Column(db.Date, nullable=False)
    wake_time = db.Column(db.String(5), nullable=False)  # Format: HH:MM
    sleep_time = db.Column(db.String(5), nullable=False)  # Format: HH:MM
    plan = db.Column(db.JSON, nullable=False)  # Store the meal plan as JSON
    
    def to_dict(self):
        return {
            'plan_id': self.plan_id,
            'user_id': self.user_id,
            'date': self.date.strftime('%Y-%m-%d') if self.date else None,
            'wake_time': self.wake_time,
            'sleep_time': self.sleep_time,
            'plan': self.plan
        }


class WeightLogModel(db.Model):
    __tablename__ = 'weight_logs'
    
    weight_log_id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.user_id'), nullable=False)
    date = db.Column(db.Date, nullable=False)
    weight_kg = db.Column(db.Float, nullable=False)
    notes = db.Column(db.String(200), nullable=True)
    
    def to_dict(self):
        return {
            'weight_log_id': self.weight_log_id,
            'user_id': self.user_id,
            'date': self.date.strftime('%Y-%m-%d') if self.date else None,
            'weight_kg': self.weight_kg,
            'notes': self.notes
        } 