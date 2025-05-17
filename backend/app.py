from flask import Flask, jsonify, request
from flask_cors import CORS
import os
import random
from datetime import datetime, timedelta
from models.database import db, FoodModel, UserModel, MealLogModel, MealPlanModel, WeightLogModel
from sqlalchemy import text
from prometheus_flask_exporter import PrometheusMetrics

app = Flask(__name__)
metrics = PrometheusMetrics(app)


CORS(app)  # Enable CORS for all routes

# Get database URI from environment variable or use default
app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get(
    'DATABASE_URL', 'sqlite:///nutritrack.db')
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

@app.route('/generate_meal_plan', methods=['POST'])
def generate_meal_plan():
    """Generate a meal plan based on user preferences."""
    data = request.get_json()
    
    # Get user parameters
    user_id = data.get('user_id', 1)  # Default to user 1 for demo
    calories_goal = data.get('calories_goal', 2000)
    protein_goal = data.get('protein_goal', 150)
    carbs_goal = data.get('carbs_goal', 200)
    fat_goal = data.get('fat_goal', 70)
    dietary_preference = data.get('dietary_preference', 'any')  # 'vegetarian', 'non-vegetarian', 'any'
    
    # Query foods based on dietary preference
    foods_query = FoodModel.query
    
    if dietary_preference == 'vegetarian':
        # Filter out non-vegetarian foods (assuming they're in categories like 'Meat', 'Poultry', etc.)
        excluded_categories = ['Meat', 'Poultry', 'Fish', 'Seafood']
        foods_query = foods_query.filter(~FoodModel.food_category.in_(excluded_categories))
    elif dietary_preference == 'non-vegetarian':
        # Include meat categories
        included_categories = ['Meat', 'Poultry', 'Fish', 'Seafood']
        foods_query = foods_query.filter(FoodModel.food_category.in_(included_categories))
    
    # Get all available foods
    available_foods = foods_query.all()
    
    if not available_foods:
        return jsonify({'error': 'No foods available for the given dietary preference'}), 404
    
    # Generate meal plan for 7 days
    meal_plan = []
    meal_types = ['Breakfast', 'Lunch', 'Dinner', 'Snack']
    today = datetime.now().date()
    
    for day in range(7):
        current_date = today + timedelta(days=day)
        
        for meal_type in meal_types:
            # Select random foods for this meal
            num_foods = random.randint(1, 3)  # 1-3 foods per meal
            
            for _ in range(num_foods):
                food = random.choice(available_foods)
                
                # Calculate appropriate quantity to meet goals (simplified)
                quantity = 1.0
                
                meal_entry = {
                    'user_id': user_id,
                    'date': current_date.strftime('%Y-%m-%d'),
                    'meal_type': meal_type,
                    'food_name': food.food_name,
                    'quantity': quantity,
                    'calories': food.calories * quantity,
                    'protein_g': food.protein_g * quantity,
                    'carbs_g': food.carbs_g * quantity,
                    'fat_g': food.fat_g * quantity,
                    'food_category': food.food_category
                }
                
                meal_plan.append(meal_entry)
    
    return jsonify({
        'meal_plan': meal_plan,
        'total_days': 7,
        'dietary_preference': dietary_preference
    })

@app.route('/api/users', methods=['POST'])
def create_user():
    data = request.get_json()
    required_fields = ['username', 'age', 'gender', 'height_cm', 'weight_kg', 
                      'activity_level', 'goal', 'dietary_preference', 'allergies',
                      'wake_time', 'sleep_time']
    
    if not all(field in data for field in required_fields):
        return jsonify({'error': 'Missing required fields'}), 400

    # Calculate BMR using the Mifflin-St Jeor Equation
    if data['gender'].lower() == 'male':
        bmr = (10 * data['weight_kg']) + (6.25 * data['height_cm']) - (5 * data['age']) + 5
    else:
        bmr = (10 * data['weight_kg']) + (6.25 * data['height_cm']) - (5 * data['age']) - 161

    # Calculate TDEE based on activity level
    activity_multipliers = {
        'sedentary': 1.2,
        'lightly_active': 1.375,
        'moderately_active': 1.55,
        'very_active': 1.725,
        'extra_active': 1.9
    }
    
    tdee = bmr * activity_multipliers.get(data['activity_level'].lower(), 1.2)

    # Calculate macronutrient goals
    if data['goal'].lower() == 'weight_loss':
        calorie_adjustment = -500
    elif data['goal'].lower() == 'weight_gain':
        calorie_adjustment = 500
    else:
        calorie_adjustment = 0

    adjusted_calories = tdee + calorie_adjustment
    protein_goal = (adjusted_calories * 0.3) / 4  # 30% of calories from protein
    carbs_goal = (adjusted_calories * 0.45) / 4   # 45% of calories from carbs
    fat_goal = (adjusted_calories * 0.25) / 9     # 25% of calories from fat

    user = {
        'username': data['username'],
        'age': data['age'],
        'gender': data['gender'],
        'height_cm': data['height_cm'],
        'weight_kg': data['weight_kg'],
        'activity_level': data['activity_level'],
        'goal': data['goal'],
        'dietary_preference': data['dietary_preference'],
        'allergies': data['allergies'],
        'wake_time': data['wake_time'],
        'sleep_time': data['sleep_time'],
        'bmr': bmr,
        'tdee': tdee,
        'protein_goal': protein_goal,
        'carbs_goal': carbs_goal,
        'fat_goal': fat_goal
    }

    users_collection.insert_one(user)
    user['_id'] = str(user['_id'])
    return jsonify(user), 201

@app.route('/api/meal-plans', methods=['POST'])
def create_meal_plan():
    data = request.get_json()
    
    # Check if this is a MealPlanRequest from Android
    if 'dietaryPreference' in data and 'allergies' in data and 'wakeTime' in data and 'sleepTime' in data and 'tdee' in data:
        # This is an Android request
        user_id = data.get('user_id', 1)  # Default to user 1 if not provided
        dietary_preference = data['dietaryPreference']
        allergies = data['allergies']
        wake_time = data['wakeTime']
        sleep_time = data['sleepTime']
        tdee = data['tdee']
    else:
        # This is a web request
        required_fields = ['user_id', 'wake_time', 'sleep_time']
        if not all(field in data for field in required_fields):
            return jsonify({'error': 'Missing required fields'}), 400
        
        user_id = data['user_id']
        wake_time = data['wake_time']
        sleep_time = data['sleep_time']
        
        # Get user to calculate TDEE
        user = UserModel.query.get(user_id)
        if not user:
            return jsonify({'error': 'User not found'}), 404
        
        tdee = user.tdee
        dietary_preference = user.dietary_preference
        allergies = user.allergies

    # Parse wake and sleep times
    wake_time_obj = datetime.strptime(wake_time, '%H:%M').time()
    sleep_time_obj = datetime.strptime(sleep_time, '%H:%M').time()
    
    # Calculate meal times based on wake and sleep schedule
    wake_dt = datetime.combine(datetime.today(), wake_time_obj)
    sleep_dt = datetime.combine(datetime.today(), sleep_time_obj)
    day_length = (sleep_dt - wake_dt).seconds / 3600  # Length of day in hours
    
    # Calculate optimal meal times
    breakfast_time = wake_dt + timedelta(minutes=30)
    lunch_time = wake_dt + timedelta(hours=day_length * 0.4)
    dinner_time = wake_dt + timedelta(hours=day_length * 0.7)
    snack_times = [
        wake_dt + timedelta(hours=day_length * 0.25),
        wake_dt + timedelta(hours=day_length * 0.55)
    ]

    # Calculate macronutrient distribution
    protein_goal = (tdee * 0.3) / 4  # 30% of calories from protein
    carbs_goal = (tdee * 0.45) / 4   # 45% of calories from carbs
    fat_goal = (tdee * 0.25) / 9     # 25% of calories from fat

    # Create meal plan
    meal_plan = {
        'breakfast': {
            'time': breakfast_time.strftime('%H:%M'),
            'calories': tdee * 0.3,
            'protein': protein_goal * 0.3,
            'carbs': carbs_goal * 0.3,
            'fat': fat_goal * 0.3
        },
        'lunch': {
            'time': lunch_time.strftime('%H:%M'),
            'calories': tdee * 0.35,
            'protein': protein_goal * 0.35,
            'carbs': carbs_goal * 0.35,
            'fat': fat_goal * 0.35
        },
        'dinner': {
            'time': dinner_time.strftime('%H:%M'),
            'calories': tdee * 0.25,
            'protein': protein_goal * 0.25,
            'carbs': carbs_goal * 0.25,
            'fat': fat_goal * 0.25
        },
        'snacks': [{
            'time': snack_time.strftime('%H:%M'),
            'calories': tdee * 0.05,
            'protein': protein_goal * 0.05,
            'carbs': carbs_goal * 0.05,
            'fat': fat_goal * 0.05
        } for snack_time in snack_times]
    }

    # Create and save meal plan
    meal_plan_model = MealPlanModel(
        user_id=user_id,
        date=datetime.now(),
        wake_time=wake_time,
        sleep_time=sleep_time,
        plan=meal_plan
    )
    
    db.session.add(meal_plan_model)
    db.session.commit()
    
    # Return response in Android-compatible format with 200 status code
    return jsonify({
        'meals': meal_plan,
        'wake_time': wake_time,
        'sleep_time': sleep_time,
        'tdee': tdee,
        'dietary_preference': dietary_preference,
        'allergies': allergies
    }), 200

@app.route('/send_query', methods=['POST'])
def send_query():
    """Execute a SQL query and return the results."""
    try:
        data = request.get_json()
        if not data or 'query' not in data:
            return jsonify({'error': 'No query provided'}), 400

        query = data['query']
        
        # Basic security check - prevent harmful operations but allow DROP TABLE
        restricted_keywords = ['DELETE', 'UPDATE', 'ALTER', 'TRUNCATE', 'DROP DATABASE', 'DROP SCHEMA']
        if any(keyword in query.upper() for keyword in restricted_keywords):
            return jsonify({'error': 'Query contains restricted keywords'}), 403

        # Execute the query using text() and commit the transaction
        result = db.session.execute(text(query))
        db.session.commit()
        
        # If the query returns results, fetch them
        if result.returns_rows:
            columns = result.keys()
            rows = [dict(zip(columns, row)) for row in result]
            return jsonify({'results': rows})
        else:
            return jsonify({'message': 'Query executed successfully'})

    except Exception as e:
        db.session.rollback()
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    # Create all tables
    with app.app_context():
        db.create_all()
    
    # Run the application
    port = int(os.environ.get('PORT', 5001))
    app.run(host='0.0.0.0', port=port, debug=True) 