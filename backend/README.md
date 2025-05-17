# NutriTrack Backend

This is the backend for the NutriTrack application.

## Features

- User management
- Food database with nutritional information
- Meal logging and planning
- Personalized recommendations using ML
- Analytics

## Setup

### Prerequisites

- Python 3.8+
- MySQL database
- Virtual environment (recommended)

### Installation

1. Clone the repository and navigate to the backend directory:

```bash
cd backend
```

2. Create and activate a virtual environment:

```bash
python -m venv venv
source venv/bin/activate  # On Windows, use: venv\Scripts\activate
```

3. Install dependencies:

```bash
pip install -r requirements.txt
```

4. Configure environment variables:

```bash
cp .env.example .env
```

Edit the `.env` file with your database credentials:

```
DATABASE_URL=mysql+pymysql://root:your_password@localhost/nutritrack
JWT_SECRET_KEY=your_secret_key
```

5. Initialize the MySQL database:

```bash
python init_db.py
```

6. Set up and seed the database tables:

```bash
flask init-db
flask seed-db
```

7. Run the server:

```bash
flask run
```

The API will be available at http://localhost:5000

## API Endpoints

### User Endpoints

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token
- `GET /api/user/<user_id>` - Get user profile
- `PUT /api/user/<user_id>` - Update user profile

### Food Database Endpoints

- `GET /api/foods` - Get foods (with optional query and category filters)
- `GET /api/foods/categories` - Get all food categories

### Meal Logging Endpoints

- `GET /api/meals/<user_id>` - Get user's meals for a specific date
- `POST /api/meals/<user_id>` - Add a new meal
- `DELETE /api/meals/<user_id>/<log_id>` - Delete a meal

### Meal Planning Endpoints

- `GET /api/meal-plan/<user_id>` - Get meal plan for a specific date
- `POST /api/meal-plan/<user_id>/generate` - Generate a meal plan

### Recommendation Endpoints

- `GET /api/recommendations/<user_id>` - Get personalized food recommendations

### Analytics Endpoints

- `GET /api/analytics/<user_id>/nutrition` - Get nutritional intake analytics
- `GET /api/analytics/<user_id>/progress` - Get weight progress analytics
- `POST /api/weight/<user_id>` - Log weight data

## Project Structure

- `app.py` - Main Flask application and API routes
- `models/` - Data models and database interaction
  - `database.py` - SQLAlchemy models
  - `user.py` - User management
  - `food_db.py` - Food database
  - `meal_logger.py` - Meal logging
  - `meal_planner.py` - Meal planning
- `utils/` - Utility functions
  - `recommendation_engine.py` - ML-based recommendation system
- `data/` - CSV files for initial data seeding
- `database_setup.py` - Database migration script

## License

[MIT License](LICENSE)