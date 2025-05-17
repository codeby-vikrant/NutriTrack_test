# NutriTrack

NutriTrack is a nutrition tracking and meal planning application that helps users monitor their dietary intake and generate personalized meal plans.

## Features

- User profile management with health metrics calculation (BMR, TDEE)
- Food database with nutritional information
- Meal logging and tracking
- Personalized meal plan generation
- Progress tracking

## Project Structure

- `app/` - Android application
- `backend/` - Flask API server
- `data/` - CSV data files for the database

## Setup Instructions

### Backend Setup

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Create and activate a Python virtual environment:
   ```
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. Install the required packages:
   ```
   pip install -r requirements.txt
   ```

4. Set up the MySQL database:
   - Make sure MySQL is running
   - Create a database named 'nutritrack'
   - Update the `.env` file with your MySQL credentials:
     ```
     DATABASE_URL=mysql+pymysql://username:password@localhost/nutritrack
     DB_PASSWORD=your_mysql_password
     ```

5. Run the database setup script:
   ```
   python database_setup.py
   ```

6. Start the backend server:
   ```
   python app.py
   ```
   The server will run on http://localhost:8001

### Android App Setup

1. Open the project in Android Studio

2. Make sure you have set up an Android emulator or connected a physical device

3. Build and run the app:
   ```
   ./gradlew installDebug
   ```

## Using the App

1. First, complete your user profile with your personal details
2. The app will calculate your BMR (Basal Metabolic Rate) and TDEE (Total Daily Energy Expenditure)
3. You can view your personalized meal plan in the "Plan" tab
4. Track your food intake in the "Meals" tab
5. Monitor your progress in the "Profile" tab

## API Endpoints

- `/health` - Check if the API is running
- `/users` - Get user information
- `/foods` - Get food database entries
- `/meal_logs` - Get meal logs for a user
- `/meal_plans` - Get meal plans for a user
- `/login` - User authentication

## Technologies Used

- Android (Java)
- Flask (Python)
- MySQL
- Retrofit for API communication
- SQLAlchemy for ORM

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- Nutrition data sources
- Machine learning resources and libraries
- Material Design guidelines

## Database Setup

The application now uses MySQL for data storage instead of CSV files. Follow these steps to set up the database:

1. Install MySQL on your system if not already installed:
   - **Ubuntu/Debian**: `sudo apt install mysql-server`
   - **macOS**: `brew install mysql`
   - **Windows**: Download and install from [MySQL website](https://dev.mysql.com/downloads/installer/)

2. Start the MySQL service:
   - **Ubuntu/Debian**: `sudo systemctl start mysql`
   - **macOS**: `brew services start mysql`
   - **Windows**: MySQL service should start automatically or via Windows Services

3. Create a MySQL user and database:
   ```sql
   CREATE USER 'nutritrack'@'localhost' IDENTIFIED BY 'nutritrack_password';
   CREATE DATABASE nutritrack;
   GRANT ALL PRIVILEGES ON nutritrack.* TO 'nutritrack'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. Configure the backend:
   - Navigate to the `backend` directory
   - Copy the example environment file: `cp .env.example .env`
   - Edit the `.env` file to set your database connection details:
     ```
     DATABASE_URL=mysql+pymysql://nutritrack:nutritrack_password@localhost/nutritrack
     ```

5. Initialize the database:
   ```bash
   cd backend
   python init_db.py  # Creates the database if it doesn't exist
   flask init-db      # Creates the tables
   flask seed-db      # Seeds the database with initial data
   ```

## Running the Application

### Backend
```bash
cd backend
source venv/bin/activate  # On Windows: venv\Scripts\activate
flask run
```

### Android App
Open the project in Android Studio and run it on an emulator or physical device. 