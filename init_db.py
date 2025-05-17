import os
import mysql.connector
from mysql.connector import errorcode
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Database configuration
DB_CONFIG = {
    'host': os.environ.get('DB_HOST', 'localhost'),
    'user': os.environ.get('DB_USER', 'root'),
    'password': os.environ.get('DB_PASSWORD', ''),  # Empty password for root user
}

DB_NAME = os.environ.get('DB_NAME', 'nutritrack')

def init_database():
    """Create the database if it doesn't exist"""
    cnx = None
    try:
        print(f"Connecting to MySQL server...")
        cnx = mysql.connector.connect(**DB_CONFIG)
        cursor = cnx.cursor()
        
        # Try to create database
        try:
            print(f"Creating database {DB_NAME}...")
            cursor.execute(f"CREATE DATABASE IF NOT EXISTS {DB_NAME} DEFAULT CHARACTER SET 'utf8'")
            print(f"Database {DB_NAME} created successfully!")
        except mysql.connector.Error as err:
            print(f"Failed creating database: {err}")
            return False
            
        # Switch to the database
        print(f"Switching to database {DB_NAME}...")
        cursor.execute(f"USE {DB_NAME}")
        
        print("Database setup complete! Use Flask commands to create tables:")
        print("  flask init-db      # Create tables")
        print("  flask seed-db      # Seed database with initial data")
        
        return True
        
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Error: Invalid database credentials")
        else:
            print(f"Error: {err}")
        return False
    finally:
        if cnx:
            cnx.close()

if __name__ == "__main__":
    init_database() 