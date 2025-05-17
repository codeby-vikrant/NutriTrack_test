import mysql.connector

# Try different credentials
credentials_options = [
    # Try with empty password
    {'user': 'root', 'password': '', 'host': 'localhost'},
    # Try with no password specified
    {'user': 'root', 'host': 'localhost'},
    # Try with default socket
    {'user': 'root', 'unix_socket': '/tmp/mysql.sock'},
    # Try with standard socket path
    {'user': 'root', 'unix_socket': '/var/run/mysqld/mysqld.sock'},
]

success = False

for credentials in credentials_options:
    try:
        print(f"Trying to connect with: {credentials}")
        connection = mysql.connector.connect(**credentials)
        
        # If we get here, connection was successful
        cursor = connection.cursor()
        
        # Create the database if it doesn't exist
        print("Connection successful! Creating database...")
        cursor.execute("CREATE DATABASE IF NOT EXISTS nutritrack DEFAULT CHARACTER SET 'utf8'")
        print("Database 'nutritrack' created successfully!")
        
        # Close connection
        cursor.close()
        connection.close()
        
        print(f"✅ Successfully connected with: {credentials}")
        print("You can use these credentials in your .env file")
        success = True
        break
        
    except mysql.connector.Error as err:
        print(f"Failed: {err}")
        continue

if not success:
    print("\n❌ Could not connect to MySQL with any of the tried credentials.")
    print("You may need to:")
    print("1. Check if MySQL is running")
    print("2. Create a user with proper privileges")
    print("3. Try with the correct password for your MySQL installation") 