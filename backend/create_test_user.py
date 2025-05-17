import os
import getpass
from flask import Flask
from models.database import db, UserModel
from werkzeug.security import generate_password_hash
from dotenv import load_dotenv

load_dotenv()

def create_test_user():
    password = getpass.getpass("Enter MySQL root password: ")
    
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = f'mysql+pymysql://root:{password}@localhost/nutritrack'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    
    db.init_app(app)
    
    with app.app_context():
        # Check if the test user already exists
        existing_user = UserModel.query.filter_by(username='testuser').first()
        if existing_user:
            print("Test user already exists.")
            return
        
        # Create test user
        test_user = UserModel(
            username='testuser',
            password_hash=generate_password_hash('password123', method='pbkdf2:sha256'),
            age=30,
            gender='Male',
            height_cm=175.0,
            weight_kg=70.0,
            activity_level='Moderate',
            goal='Maintain',
            dietary_preference='No Restrictions',
            bmr=1700.0,
            tdee=2200.0,
            protein_goal=150.0,
            carbs_goal=250.0,
            fat_goal=70.0
        )
        
        db.session.add(test_user)
        db.session.commit()
        
        print(f"Test user created successfully with ID: {test_user.user_id}")
        print("Username: testuser")
        print("Password: password123")

if __name__ == "__main__":
    create_test_user() 