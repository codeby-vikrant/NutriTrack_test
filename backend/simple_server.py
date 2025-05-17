#!/usr/bin/env python3
"""
Simple Flask server for NutriTrack API
"""
from flask import Flask, jsonify, request
from flask_cors import CORS
import os
import json

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Dummy data for users
USERS = [
    {
        "user_id": 1,
        "username": "testuser",
        "age": 30,
        "gender": "Male",
        "height_cm": 175.0,
        "weight_kg": 70.0,
        "activity_level": "Moderate",
        "goal": "Maintain",
        "dietary_preference": "any",
        "bmr": 1700,
        "tdee": 2200,
        "protein_goal": 150,
        "carbs_goal": 200,
        "fat_goal": 70
    }
]

# Dummy data for foods
FOODS = [
    {"food_id": 1, "food_name": "Chicken Breast", "calories": 165, "protein_g": 31, "carbs_g": 0, "fat_g": 3.6, "food_category": "Meat"},
    {"food_id": 2, "food_name": "Brown Rice", "calories": 216, "protein_g": 5, "carbs_g": 45, "fat_g": 1.8, "food_category": "Grains"},
    {"food_id": 3, "food_name": "Broccoli", "calories": 55, "protein_g": 3.7, "carbs_g": 11.2, "fat_g": 0.6, "food_category": "Vegetables"},
    {"food_id": 4, "food_name": "Salmon", "calories": 206, "protein_g": 22, "carbs_g": 0, "fat_g": 13, "food_category": "Fish"},
    {"food_id": 5, "food_name": "Eggs", "calories": 155, "protein_g": 13, "carbs_g": 1.1, "fat_g": 11, "food_category": "Dairy"}
]

# Dummy data for meal logs
MEAL_LOGS = [
    {"log_id": 1, "user_id": 1, "date": "2023-04-18", "meal_type": "Breakfast", "food_name": "Eggs", "quantity": 2, "calories": 310, "protein_g": 26, "carbs_g": 2.2, "fat_g": 22},
    {"log_id": 2, "user_id": 1, "date": "2023-04-18", "meal_type": "Lunch", "food_name": "Chicken Breast", "quantity": 1, "calories": 165, "protein_g": 31, "carbs_g": 0, "fat_g": 3.6}
]

# Dummy data for meal plans
MEAL_PLANS = [
    {"plan_id": 1, "user_id": 1, "date": "2023-04-19", "meal_type": "Breakfast", "food_name": "Oatmeal", "quantity": 1, "calories": 158, "protein_g": 6, "carbs_g": 27, "fat_g": 3},
    {"plan_id": 2, "user_id": 1, "date": "2023-04-19", "meal_type": "Lunch", "food_name": "Salmon", "quantity": 1, "calories": 206, "protein_g": 22, "carbs_g": 0, "fat_g": 13}
]

@app.route('/health', methods=['GET'])
def health_check():
    """Check if the API is running."""
    return jsonify({'status': 'healthy', 'message': 'NutriTrack API is operational'})

@app.route('/users', methods=['GET'])
def get_users():
    """Get all users or filtered by username."""
    username = request.args.get('username')
    
    if username:
        filtered_users = [user for user in USERS if user['username'] == username]
        return jsonify(filtered_users)
    
    return jsonify(USERS)

@app.route('/foods', methods=['GET'])
def get_foods():
    """Get foods with optional filtering by category or name."""
    category = request.args.get('category')
    name = request.args.get('name')
    
    filtered_foods = FOODS
    
    if category:
        filtered_foods = [food for food in filtered_foods if food['food_category'] == category]
    
    if name:
        filtered_foods = [food for food in filtered_foods if name.lower() in food['food_name'].lower()]
    
    return jsonify(filtered_foods)

@app.route('/meal_logs', methods=['GET'])
def get_meal_logs():
    """Get meal logs for a specific user."""
    user_id = request.args.get('user_id')
    
    if not user_id:
        return jsonify({'error': 'user_id parameter is required'}), 400
    
    user_id = int(user_id)
    filtered_logs = [log for log in MEAL_LOGS if log['user_id'] == user_id]
    
    return jsonify(filtered_logs)

@app.route('/meal_plans', methods=['GET'])
def get_meal_plans():
    """Get meal plans for a specific user."""
    user_id = request.args.get('user_id')
    
    if not user_id:
        return jsonify({'error': 'user_id parameter is required'}), 400
    
    user_id = int(user_id)
    filtered_plans = [plan for plan in MEAL_PLANS if plan['user_id'] == user_id]
    
    return jsonify(filtered_plans)

@app.route('/login', methods=['POST'])
def login():
    """Simple login endpoint."""
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    
    # This is just a placeholder for demo purposes
    if username and password:
        return jsonify({'status': 'success', 'message': 'Login successful', 'user_id': 1})
    
    return jsonify({'status': 'error', 'message': 'Invalid credentials'}), 401

@app.route('/generate_meal_plan', methods=['POST'])
def generate_meal_plan():
    """Generate a meal plan based on user preferences."""
    data = request.get_json()
    
    # Get user parameters
    user_id = data.get('user_id', 1)  # Default to user 1 for demo
    dietary_preference = data.get('dietary_preference', 'any')
    
    # Filter foods based on dietary preference
    available_foods = FOODS
    if dietary_preference == 'vegetarian':
        available_foods = [food for food in FOODS if food['food_category'] not in ['Meat', 'Fish']]
    
    # Return some sample meal plans
    meal_plan = [
        {"user_id": user_id, "date": "2023-04-19", "meal_type": "Breakfast", "food_name": "Oatmeal", "quantity": 1, "calories": 158, "protein_g": 6, "carbs_g": 27, "fat_g": 3, "food_category": "Grains"},
        {"user_id": user_id, "date": "2023-04-19", "meal_type": "Lunch", "food_name": "Brown Rice", "quantity": 1, "calories": 216, "protein_g": 5, "carbs_g": 45, "fat_g": 1.8, "food_category": "Grains"},
        {"user_id": user_id, "date": "2023-04-19", "meal_type": "Lunch", "food_name": "Chicken Breast", "quantity": 1, "calories": 165, "protein_g": 31, "carbs_g": 0, "fat_g": 3.6, "food_category": "Meat"},
        {"user_id": user_id, "date": "2023-04-19", "meal_type": "Dinner", "food_name": "Salmon", "quantity": 1, "calories": 206, "protein_g": 22, "carbs_g": 0, "fat_g": 13, "food_category": "Fish"},
        {"user_id": user_id, "date": "2023-04-19", "meal_type": "Dinner", "food_name": "Broccoli", "quantity": 1, "calories": 55, "protein_g": 3.7, "carbs_g": 11.2, "fat_g": 0.6, "food_category": "Vegetables"}
    ]
    
    return jsonify({
        'meal_plan': meal_plan,
        'total_days': 1,
        'dietary_preference': dietary_preference
    })

if __name__ == '__main__':
    # Run the application
    port = int(os.environ.get('PORT', 8001))
    print(f"\033[92mNutriTrack API Server is running on http://localhost:{port}\033[0m")
    app.run(host='0.0.0.0', port=port, debug=True) 