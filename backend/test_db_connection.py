import os
import getpass
from flask import Flask
from dotenv import load_dotenv
from models.database import db, UserModel

load_dotenv()

def test_db_connection():
    password = getpass.getpass("Enter MySQL root password: ")
    
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = f'mysql+pymysql://root:{password}@localhost/nutritrack'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    
    db.init_app(app)
    
    with app.app_context():
        try:
            # Try to query the users table
            users = UserModel.query.all()
            print(f"Database connection successful! Found {len(users)} users.")
            
            # Print test user details if available
            test_user = UserModel.query.filter_by(username='testuser').first()
            if test_user:
                print(f"Test user found: ID={test_user.user_id}, Username={test_user.username}")
            else:
                print("No test user found.")
                
            # Print available tables
            from sqlalchemy import inspect
            inspector = inspect(db.engine)
            tables = inspector.get_table_names()
            print(f"Available tables: {', '.join(tables)}")
            
            return True
        except Exception as e:
            print(f"Database connection error: {str(e)}")
            return False

if __name__ == "__main__":
    test_db_connection()
