import mysql.connector
import getpass
password = getpass.getpass("Enter MySQL root password: ")
print("Trying to connect to MySQL...")
try:
    conn = mysql.connector.connect(user="root", password=password, host="localhost")
    print("Connected successfully!")
    cursor = conn.cursor()
    cursor.execute("CREATE DATABASE IF NOT EXISTS nutritrack")
    print("Database created successfully!")
    print("\nUpdate your .env file with these credentials:")
    print(f"DATABASE_URL=mysql+pymysql://root:{password}@localhost/nutritrack")
    print(f"DB_PASSWORD={password}")
except mysql.connector.Error as err:
    print(f"Error: {err}")
