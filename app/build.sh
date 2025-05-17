#!/bin/bash

# Build and run the Android app

# Make sure our backend server is running
cd "$(dirname "$0")/.." # Navigate to project root
pkill -f "python3 app.py" || true
cd backend
python3 app.py > server.log 2>&1 &
echo "Backend server started on port 8001"

# Go back to app directory
cd ../app

# Check for Android Studio and related tools
if command -v adb &> /dev/null; then
    # Check if an emulator is running, if not start one
    if ! adb devices | grep -q "emulator"; then
        echo "No emulator running. Please start an emulator from Android Studio."
        if command -v emulator &> /dev/null; then
            echo "Starting a new emulator..."
            emulator -list-avds
            # Use the first available AVD
            AVD=$(emulator -list-avds | head -n 1)
            if [ -n "$AVD" ]; then
                emulator -avd "$AVD" &
                adb wait-for-device
            else
                echo "No AVDs found. Please create one in Android Studio."
                exit 1
            fi
        else
            echo "Emulator command not found. Please use Android Studio to start an emulator."
            exit 1
        fi
    fi

    # Build and install the app
    if [ -f "./gradlew" ]; then
        ./gradlew installDebug
        
        # Run the app
        adb shell am start -n com.example.nutritrack_test/.MainActivity
    else
        echo "Gradle wrapper not found. Are you in the app directory?"
        echo "Please run this script from the project root directory."
        exit 1
    fi
else
    echo "Android tools (adb) not found in your PATH."
    echo "Please open the project in Android Studio and run it from there:"
    echo "1. Open Android Studio"
    echo "2. Open the NutriTrack_test project"
    echo "3. Click the 'Run' button (green triangle)"
    exit 1
fi 