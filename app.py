from flask import Flask, jsonify, request
from flask_cors import CORS
import os
from models.database import db, FoodModel, UserModel, MealLogModel, MealPlanModel, WeightLogModel

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Get database URI from environment variable or use default
app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get(
    'DATABASE_URL', 'mysql+pymysql://root:admin123@localhost/nutritrack')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Initialize the database with the app
db.init_app(app)

@app.route('/health', methods=['GET'])
def health_check():
    """Check if the API is running."""
    return jsonify({'status': 'healthy', 'message': 'NutriTrack API is operational'})

@app.route('/users', methods=['GET'])
def get_users():
    """Get all users or filtered by username."""
    username = request.args.get('username')
    
    if username:
        users = UserModel.query.filter_by(username=username).all()
    else:
        users = UserModel.query.all()
        
    return jsonify([user.to_dict() for user in users])

@app.route('/foods', methods=['GET'])
def get_foods():
    """Get foods with optional filtering by category or name."""
    category = request.args.get('category')
    name = request.args.get('name')
    
    query = FoodModel.query
    
    if category:
        query = query.filter_by(food_category=category)
    
    if name:
        query = query.filter(FoodModel.food_name.like(f'%{name}%'))
    
    foods = query.limit(50).all()  # Limit to 50 for performance
    
    return jsonify([food.to_dict() for food in foods])

@app.route('/meal_logs', methods=['GET'])
def get_meal_logs():
    """Get meal logs for a specific user."""
    user_id = request.args.get('user_id')
    
    if not user_id:
        return jsonify({'error': 'user_id parameter is required'}), 400
    
    meal_logs = MealLogModel.query.filter_by(user_id=user_id).all()
    
    return jsonify([log.to_dict() for log in meal_logs])

@app.route('/meal_plans', methods=['GET'])
def get_meal_plans():
    """Get meal plans for a specific user."""
    user_id = request.args.get('user_id')
    
    if not user_id:
        return jsonify({'error': 'user_id parameter is required'}), 400
    
    meal_plans = MealPlanModel.query.filter_by(user_id=user_id).all()
    
    return jsonify([plan.to_dict() for plan in meal_plans])

@app.route('/login', methods=['POST'])
def login():
    """Simple login endpoint."""
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    
    # This is just a placeholder for demo purposes
    # In a real app, you'd verify credentials against a database
    if username and password:
        return jsonify({'status': 'success', 'message': 'Login successful', 'user_id': 1})
    
    return jsonify({'status': 'error', 'message': 'Invalid credentials'}), 401

if __name__ == '__main__':
    # Create all tables
    with app.app_context():
        db.create_all()
    
    # Run the application
    port = int(os.environ.get('PORT', 8001))
    app.run(host='0.0.0.0', port=port, debug=True) 