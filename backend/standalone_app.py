import os
import getpass
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

def create_app(db_password):
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

    # Initialize components
    food_db = FoodDatabase(db)
    user_manager = User(db)
    meal_planner = MealPlanner(food_db, db)
    meal_logger = MealLogger(db)
    recommendation_engine = RecommendationEngine(food_db, db)

    # Routes
    @app.route('/api/health', methods=['GET'])
    def health_check():
        return jsonify({"status": "healthy", "timestamp": datetime.now().isoformat()})

    # User Authentication and Management
    @app.route('/api/auth/login', methods=['POST'])
    def login():
        data = request.get_json()
        username = data.get('username')
        password = data.get('password')
        
        user = user_manager.get_user_by_username(username)
        if not user:
            return jsonify({"error": "Invalid credentials"}), 401
        
        user_model = UserModel.query.filter_by(username=username).first()
        if not user_model or not user_model.check_password(password):
            return jsonify({"error": "Invalid credentials"}), 401
        
        access_token = create_access_token(identity=user['user_id'])
        return jsonify(access_token=access_token, user_id=user['user_id'])

    # Food Database
    @app.route('/api/foods', methods=['GET'])
    def get_foods():
        category = request.args.get('category')
        query = request.args.get('query')
        
        foods = food_db.search_foods(query, category)
        return jsonify(foods)

    # A simple user endpoint to list all users
    @app.route('/api/users', methods=['GET'])
    def get_users():
        users = UserModel.query.all()
        return jsonify([user.to_dict() for user in users])

    return app

def run_app():
    password = getpass.getpass("Enter MySQL root password: ")
    app = create_app(password)
    
    with app.app_context():
        db.create_all()
        print('Database tables ready.')
    
    app.run(debug=True, host='0.0.0.0', port=8000)

if __name__ == "__main__":
    run_app() 