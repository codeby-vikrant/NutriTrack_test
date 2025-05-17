#!/usr/bin/env python3
"""
Start script for the NutriTrack API server.
"""
import os
import sys
from flask import Flask, request, jsonify
from flask_cors import CORS
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from datetime import datetime, timedelta
from dotenv import load_dotenv
from models.database import db, UserModel, FoodModel, MealLogModel, MealPlanModel, WeightLogModel
from models.meal_planner import MealPlanner
from models.food_db import FoodDatabase
from models.meal_logger import MealLogger
from models.user import User
from utils.recommendation_engine import RecommendationEngine

load_dotenv()

# Get DB password from .env
db_password = os.environ.get('DB_PASSWORD', 'admin123')

app = Flask(__name__)
CORS(app)

# Database Configuration
app.config['SQLALCHEMY_DATABASE_URI'] = f'mysql+pymysql://root:{db_password}@localhost/nutritrack'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db.init_app(app)

# JWT Configuration
app.config['JWT_SECRET_KEY'] = os.environ.get('JWT_SECRET_KEY', 'nutritrack-secret-key')
app.config['JWT_ACCESS_TOKEN_EXPIRES'] = timedelta(days=1)
jwt = JWTManager(app)

# Routes
@app.route('/api/health', methods=['GET'])
def health_check():
    print("Health check endpoint called")
    return jsonify({"status": "healthy", "timestamp": datetime.now().isoformat()})

# User Authentication and Management
@app.route('/api/auth/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    
    user_model = UserModel.query.filter_by(username=username).first()
    if not user_model or not user_model.check_password(password):
        return jsonify({"error": "Invalid credentials"}), 401
    
    access_token = create_access_token(identity=user_model.user_id)
    return jsonify(access_token=access_token, user_id=user_model.user_id)

# Food Database
@app.route('/api/foods', methods=['GET'])
def get_foods():
    category = request.args.get('category')
    query = request.args.get('query')
    
    foods = FoodModel.query.all()
    results = [food.to_dict() for food in foods]
    
    if category:
        results = [food for food in results if food['food_category'] == category]
    if query:
        query = query.lower()
        results = [food for food in results if query in food['food_name'].lower()]
    
    return jsonify(results)

# A simple user endpoint to list all users
@app.route('/api/users', methods=['GET'])
def get_users():
    users = UserModel.query.all()
    return jsonify([user.to_dict() for user in users])

if __name__ == '__main__':
    # Create all tables
    with app.app_context():
        db.create_all()
    
    # Get port from environment variable or use default
    port = int(os.environ.get('PORT', 8001))
    
    # Run the application
    print(f"Starting NutriTrack API server on port {port}...")
    app.run(host='0.0.0.0', port=port, debug=True) 