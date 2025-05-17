import os
import getpass
from flask import Flask
from models.database import db
from dotenv import load_dotenv
import database_setup

load_dotenv()

def reset_database():
    password = getpass.getpass("Enter MySQL root password: ")
    
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = f'mysql+pymysql://root:{password}@localhost/nutritrack'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    
    db.init_app(app)
    
    with app.app_context():
        print("Dropping all tables...")
        db.drop_all()
        print("All tables dropped successfully.")
    
    # Now run the database setup to recreate tables and migrate data
    print("Recreating tables and migrating data...")
    app, _ = database_setup.create_app()
    database_setup.migrate_csv_to_db(app)
    
    print("Database reset and migration completed successfully.")

if __name__ == "__main__":
    reset_database() 