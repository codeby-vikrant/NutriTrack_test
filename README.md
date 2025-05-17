# NutriTrack

A Flask-based nutrition tracking application.

## Setup Instructions

### Prerequisites

- Python 3.8 or higher
- MySQL Server
- pip (Python package manager)

### Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/NutriTrack.git
cd NutriTrack
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

4. Set up environment variables:

   - Copy `.env.example` to `.env`:
     ```bash
     cp .env.example .env
     ```
   - Edit `.env` with your configuration:

     ```
     # Flask Configuration
     FLASK_APP=app.py
     FLASK_ENV=development
     SECRET_KEY=your_secret_key_here

     # MySQL Configuration
     MYSQL_HOST=localhost
     MYSQL_USER=your_mysql_username
     MYSQL_PASSWORD=your_mysql_password
     MYSQL_DB=your_database_name

     # Application Settings
     DEBUG=True
     ```

5. Set up MySQL:

   - Create a new MySQL database
   - Update the `.env` file with your MySQL credentials
   - Make sure MySQL server is running

6. Initialize the database:

```bash
flask db upgrade
```

### Running the Application

1. Start the Flask development server:

```bash
flask run
```

2. Access the application at `http://localhost:5000`

## Security Notes

- Never commit the `.env` file to version control
- Keep your database credentials secure
- Use strong, unique passwords
- Set `DEBUG=False` in production
- Generate a strong `SECRET_KEY` for production

## Development

- The application uses Flask-SQLAlchemy for database operations
- Flask-Migrate for database migrations
- MySQL as the database backend

## Contributing

1. Fork the repository
2. Create a new branch for your feature
3. Make your changes
4. Submit a pull request

## License

[Your chosen license]
