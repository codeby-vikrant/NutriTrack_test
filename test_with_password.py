import mysql.connector
import getpass

# Ask for password
password = getpass.getpass("Enter MySQL root password: ")

try:
    # Try connecting with the provided password
    print("Connecting to MySQL...")
    conn = mysql.connector.connect(
        user='root',
        password=password,
        host='localhost'
    )
    
    print("Connected successfully!")
    
    # Create the database
    cursor = conn.cursor()
    cursor.execute("CREATE DATABASE IF NOT EXISTS nutritrack DEFAULT CHARACTER SET 'utf8'")
    print("Database 'nutritrack' created successfully!")
    
    # Close connection
    cursor.close()
    conn.close()
    
    # Update the .env file with the correct credentials
    print("\nYou should update your .env file with these credentials:")
    print(f"DATABASE_URL=mysql+pymysql://root:{password}@localhost/nutritrack")
    print(f"DB_PASSWORD={password}")
    
except mysql.connector.Error as err:
    print(f"Error: {err}") 