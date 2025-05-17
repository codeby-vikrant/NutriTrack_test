from flask import Flask, jsonify, request, render_template, send_from_directory
import pandas as pd
from flask_cors import CORS
from datetime import datetime
import logging
import os
import json
from collections import deque
import hashlib
import uuid
from werkzeug.utils import secure_filename

app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "*"}})

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize in-memory storage
request_logs = deque(maxlen=1000)  # Store last 1000 requests
active_users = set()
api_request_count = 0

# Create uploads directory for profile images
UPLOAD_FOLDER = os.path.join(os.path.dirname(__file__), 'uploads')
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

# User database (in-memory for demo)
users = {}
user_profiles = {}

# Simple token storage (in production, use a real database)
active_tokens = {}

# Load food data from CSV
def load_food_data():
    try:
        csv_path = os.path.join(os.path.dirname(__file__), '..', 'app', 'src', 'main', 'assets', 'healthy_indian_foods.csv')
        logger.info(f"Loading food data from: {csv_path}")
        
        if not os.path.exists(csv_path):
            logger.error(f"CSV file not found at: {csv_path}")
            return []
            
        df = pd.read_csv(csv_path)
        logger.info(f"Successfully loaded {len(df)} food items")
        
        # Convert DataFrame to list of dictionaries with proper field names
        foods = []
        for _, row in df.iterrows():
            food = {
                "name": row["name"],
                "region": row["region"],
                "description": row["description"],
                "diet_type": row["diet_type"],
                "calories": float(row["calories"]),
                "protein": float(row["protein"]),
                "carbs": float(row["carbs"]),
                "fat": float(row["fats"]),  # Note: CSV column is "fats" but we return as "fat"
                "meal_type": row["meal_type"]
            }
            foods.append(food)
        return foods
    except Exception as e:
        logger.error(f"Error loading CSV: {e}")
        return []

foods = load_food_data()

def log_request(endpoint, status_code=200):
    global api_request_count
    api_request_count += 1
    timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    request_logs.append({
        'timestamp': timestamp,
        'endpoint': endpoint,
        'status_code': status_code,
        'ip': request.remote_addr
    })

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# Authentication and user profile endpoints
@app.route('/api/register', methods=['POST'])
def register():
    log_request('/api/register')
    data = request.json
    if not data or 'username' not in data or 'password' not in data:
        return jsonify({"error": "Username and password are required"}), 400
    
    username = data['username']
    if username in users:
        return jsonify({"error": "Username already exists"}), 409
    
    # Hash the password (in production, use a better salt mechanism)
    password_hash = hashlib.sha256(data['password'].encode()).hexdigest()
    user_id = str(uuid.uuid4())
    
    # Store user credentials
    users[username] = {
        'user_id': user_id,
        'password_hash': password_hash
    }
    
    # Initialize user profile
    user_profiles[user_id] = {
        'username': username,
        'user_id': user_id,
        'profile_image': None,
        'age': data.get('age'),
        'gender': data.get('gender'),
        'height': data.get('height'),
        'weight': data.get('weight'),
        'activity_level': data.get('activity_level'),
        'goal': data.get('goal'),
        'dietary_preference': data.get('dietary_preference'),
        'allergies': data.get('allergies', '').split(',') if data.get('allergies') else [],
        'target_calories': data.get('target_calories'),
        'target_protein': data.get('target_protein'),
        'created_at': datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    }
    
    # Generate auth token
    token = str(uuid.uuid4())
    active_tokens[token] = user_id
    
    logger.info(f"New user registered: {username}")
    return jsonify({
        "message": "Registration successful",
        "token": token,
        "user_id": user_id,
        "username": username
    }), 201

@app.route('/api/login', methods=['POST'])
def login():
    log_request('/api/login')
    data = request.json
    if not data or 'username' not in data or 'password' not in data:
        return jsonify({"error": "Username and password are required"}), 400
    
    username = data['username']
    if username not in users:
        return jsonify({"error": "Invalid username or password"}), 401
    
    # Check password
    password_hash = hashlib.sha256(data['password'].encode()).hexdigest()
    if password_hash != users[username]['password_hash']:
        return jsonify({"error": "Invalid username or password"}), 401
    
    # Generate and store token
    token = str(uuid.uuid4())
    user_id = users[username]['user_id']
    active_tokens[token] = user_id
    
    # Add to active users set
    active_users.add(username)
    
    logger.info(f"User logged in: {username}")
    return jsonify({
        "message": "Login successful",
        "token": token,
        "user_id": user_id,
        "username": username
    }), 200

@app.route('/api/logout', methods=['POST'])
def logout():
    token = request.headers.get('Authorization')
    if token and token in active_tokens:
        user_id = active_tokens[token]
        # Find username from user_id
        username = next((u for u, data in users.items() if data['user_id'] == user_id), None)
        
        # Remove token
        del active_tokens[token]
        
        # Remove from active users if found
        if username and username in active_users:
            active_users.remove(username)
        
        return jsonify({"message": "Logout successful"}), 200
    
    return jsonify({"message": "Already logged out"}), 200

# Helper middleware for authentication
def authenticate_request():
    token = request.headers.get('Authorization')
    if not token or token not in active_tokens:
        return None
    
    return active_tokens[token]  # Returns user_id

@app.route('/api/profile/<user_id>', methods=['GET'])
def get_profile(user_id):
    log_request(f'/api/profile/{user_id}')
    requester_id = authenticate_request()
    
    if not requester_id:
        return jsonify({"error": "Authentication required"}), 401
    
    if user_id not in user_profiles:
        return jsonify({"error": "User not found"}), 404
    
    profile = user_profiles[user_id].copy()
    return jsonify(profile), 200

@app.route('/api/profile/<user_id>', methods=['PUT'])
def update_profile(user_id):
    log_request(f'/api/profile/{user_id}')
    requester_id = authenticate_request()
    
    if not requester_id:
        return jsonify({"error": "Authentication required"}), 401
    
    if requester_id != user_id:
        return jsonify({"error": "Not authorized to update this profile"}), 403
    
    if user_id not in user_profiles:
        return jsonify({"error": "User not found"}), 404
    
    data = request.json
    profile = user_profiles[user_id]
    
    # Update profile fields
    if 'age' in data: profile['age'] = data['age']
    if 'gender' in data: profile['gender'] = data['gender']
    if 'height' in data: profile['height'] = data['height']
    if 'weight' in data: profile['weight'] = data['weight']
    if 'activity_level' in data: profile['activity_level'] = data['activity_level']
    if 'goal' in data: profile['goal'] = data['goal']
    if 'dietary_preference' in data: profile['dietary_preference'] = data['dietary_preference']
    if 'allergies' in data: profile['allergies'] = data['allergies'].split(',') if isinstance(data['allergies'], str) else data['allergies']
    if 'target_calories' in data: profile['target_calories'] = data['target_calories']
    if 'target_protein' in data: profile['target_protein'] = data['target_protein']
    
    return jsonify({
        "message": "Profile updated successfully",
        "profile": profile
    }), 200

@app.route('/api/profile/<user_id>/upload', methods=['POST'])
def upload_profile_image(user_id):
    log_request(f'/api/profile/{user_id}/upload')
    requester_id = authenticate_request()
    
    if not requester_id:
        return jsonify({"error": "Authentication required"}), 401
    
    if requester_id != user_id:
        return jsonify({"error": "Not authorized to update this profile"}), 403
    
    if 'file' not in request.files:
        return jsonify({"error": "No file part"}), 400
    
    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400
    
    if file and allowed_file(file.filename):
        filename = secure_filename(f"{user_id}_{file.filename}")
        filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(filepath)
        
        # Update profile with image path
        if user_id in user_profiles:
            user_profiles[user_id]['profile_image'] = f"/uploads/{filename}"
        
        return jsonify({
            "message": "File uploaded successfully",
            "file_path": f"/uploads/{filename}"
        }), 200
    
    return jsonify({"error": "File type not allowed"}), 400

@app.route('/uploads/<filename>')
def serve_upload(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)

@app.route('/')
def dashboard():
    stats = {
        'total_foods': len(foods),
        'active_users': len(active_users),
        'api_requests': api_request_count
    }
    
    recent_logs = list(request_logs)[-50:]  # Get last 50 logs
    return render_template('dashboard.html', stats=stats, foods=foods, logs=recent_logs)

@app.route('/api/health')
def health_check():
    log_request('/api/health')
    return jsonify({"status": "healthy"})

@app.route('/api/foods')
def get_all_foods():
    log_request('/api/foods')
    logger.info(f"Returning {len(foods)} food items")
    return jsonify(foods)

@app.route('/api/foods/filter')
def get_filtered_foods():
    log_request('/api/foods/filter')
    region = request.args.get('region')
    diet = request.args.get('diet')
    meal_type = request.args.get('mealType')
    
    filtered_foods = foods.copy()
    
    if region:
        filtered_foods = [f for f in filtered_foods if f['region'].lower() == region.lower()]
    
    if diet:
        filtered_foods = [f for f in filtered_foods if f.get('diet_type', '').lower() == diet.lower()]
    
    if meal_type:
        filtered_foods = [f for f in filtered_foods if f['meal_type'].lower() == meal_type.lower()]
    
    return jsonify(filtered_foods)

@app.route('/api/foods/search')
def search_foods():
    log_request('/api/foods/search')
    query = request.args.get('query', '').lower()
    if not query:
        return jsonify(foods)
    
    searched_foods = [
        food for food in foods
        if query in food['name'].lower() or
           query in food['region'].lower() or
           query in food['meal_type'].lower()
    ]
    return jsonify(searched_foods)

@app.route('/api/generate_meal_plan', methods=['POST'])
def generate_meal_plan():
    log_request('/api/generate_meal_plan')
    data = request.json
    diet_type = data.get('diet_type', '').lower()
    target_calories = int(data.get('target_calories', 2000))
    
    # Filter foods by diet type
    diet_foods = [f for f in foods if f.get('diet_type', '').lower() == diet_type]
    
    # If no foods match the specific diet type, use all foods
    if not diet_foods:
        diet_foods = foods

    # For keto diet, filter foods with low carbs (less than 10g per serving)
    if diet_type == 'keto':
        diet_foods = [f for f in diet_foods if f.get('carbs', 0) < 10]
    
    # Group foods by meal type
    breakfast_foods = [f for f in diet_foods if f['meal_type'].lower() == 'breakfast']
    lunch_foods = [f for f in diet_foods if f['meal_type'].lower() == 'lunch']
    dinner_foods = [f for f in diet_foods if f['meal_type'].lower() == 'dinner']
    snack_foods = [f for f in diet_foods if f['meal_type'].lower() == 'snack']
    
    # Calculate target calories per meal (simple distribution)
    breakfast_target = int(target_calories * 0.3)  # 30% for breakfast
    lunch_target = int(target_calories * 0.35)     # 35% for lunch
    dinner_target = int(target_calories * 0.25)    # 25% for dinner
    snack_target = int(target_calories * 0.1)      # 10% for snacks
    
    # Simple meal plan generator (in real world, use a better algorithm)
    import random
    meal_plan = {
        "breakfast": select_foods_for_meal(breakfast_foods, breakfast_target),
        "lunch": select_foods_for_meal(lunch_foods, lunch_target),
        "dinner": select_foods_for_meal(dinner_foods, dinner_target),
        "snacks": select_foods_for_meal(snack_foods, snack_target),
        "target_calories": target_calories,
        "actual_calories": 0,  # Will be calculated
        "diet_type": diet_type
    }
    
    # Calculate total calories
    total_calories = sum(food.get('calories', 0) for meal_type, foods_list in meal_plan.items() 
                        if isinstance(foods_list, list) for food in foods_list)
    meal_plan["actual_calories"] = total_calories
    
    return jsonify(meal_plan)

def select_foods_for_meal(available_foods, target_calories):
    """Simple function to select foods for a meal based on target calories"""
    if not available_foods:
        return []
        
    import random
    selected_foods = []
    current_calories = 0
    
    # Make a copy and shuffle
    foods_copy = available_foods.copy()
    random.shuffle(foods_copy)
    
    # Add foods until we reach target calories
    for food in foods_copy:
        if current_calories >= target_calories:
            break
            
        selected_foods.append(food)
        current_calories += food.get('calories', 0)
    
    return selected_foods

@app.route('/api/logs')
def get_logs():
    return jsonify({
        'logs': list(request_logs)
    })

@app.errorhandler(Exception)
def handle_error(error):
    logger.error(f"Error: {str(error)}")
    return jsonify({
        'error': str(error),
        'status': 'error'
    }), 500

if __name__ == '__main__':
    # Make sure we have needed imports
    from flask import send_from_directory
    
    # Create templates directory if it doesn't exist
    os.makedirs('templates', exist_ok=True)
    
    # Reload food data
    if not foods:
        foods.extend(load_food_data())
    
    logger.info(f"Server starting with {len(foods)} food items loaded")
    app.run(host='0.0.0.0', port=8090, debug=True) 