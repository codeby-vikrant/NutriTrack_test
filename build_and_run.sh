#!/bin/bash

# Ensure we're in the right directory
cd "$(dirname "$0")"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed or not in PATH"
    echo "Please install Java first:"
    echo "brew install openjdk@17"
    echo "echo 'export PATH=\"/opt/homebrew/opt/openjdk@17/bin:\$PATH\"' >> ~/.zshrc"
    echo "echo 'export JAVA_HOME=\"/opt/homebrew/opt/openjdk@17\"' >> ~/.zshrc"
    echo "source ~/.zshrc"
    exit 1
fi

# Check if the backend server is running
if ! curl -s http://localhost:8000/api/health &> /dev/null; then
    echo "Starting the backend server..."
    cd backend
    nohup python3 standalone_app.py < <(echo "admin123") > server.log 2>&1 &
    cd ..
    sleep 2
    echo "Backend server started!"
else
    echo "Backend server is already running"
fi

# Build and install the app
echo "Building and installing the app..."
./gradlew installDebug

# Check if build was successful
if [ $? -eq 0 ]; then
    # Launch the app
    echo "Launching the app..."
    ~/Library/Android/sdk/platform-tools/adb shell am start -n "com.example.nutritrack_test/com.example.nutritrack_test.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
else
    echo "Build failed. Please check the error messages above."
fi 