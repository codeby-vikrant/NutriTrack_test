#!/usr/bin/env python3
"""
Simple server script to run the Flask app
"""
from app import app

if __name__ == '__main__':
    # Create all tables
    with app.app_context():
        from models.database import db
        db.create_all()
        print("Database tables ready.")
    
    # Run the application
    print("Starting server on port 8002")
    app.run(host='0.0.0.0', port=8002, debug=True) 