import os
import getpass
from flask import Flask
from dotenv import load_dotenv
from models.database import db
import app

load_dotenv()

def run_app():
    password = getpass.getpass("Enter MySQL root password: ")
    
    # Configure the app with the provided password
    app.app.config['SQLALCHEMY_DATABASE_URI'] = f'mysql+pymysql://root:{password}@localhost/nutritrack'
    
    # Create tables within the app context
    with app.app.app_context():
        db.create_all()
        print('Database tables ready.')
    
    # Run the app
    app.app.run(debug=True, host='0.0.0.0', port=5000)

if __name__ == "__main__":
    run_app() 