import mysql.connector
print("Trying to connect to MySQL...")
try:
    conn = mysql.connector.connect(user="root", password="", host="localhost")
    print("Connected successfully!")
except mysql.connector.Error as err:
    print(f"Error: {err}")
