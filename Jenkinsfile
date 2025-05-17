pipeline {
    agent any

    environment {
        JAVA_HOME = '/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home'
        ANDROID_HOME = '/Users/vikrantvani/Library/Android/sdk'
        PATH = "${env.PATH}:${JAVA_HOME}/bin:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools"
    }

    stages {
        stage('Start Python Backend') {
            steps {
                dir('/Users/vikrantvani/JenkinsProjects/NutriTrack/backend') {
                    sh '''
                        python3 -m venv venv
                        source venv/bin/activate
                        pip install -r requirements.txt
                        nohup python3 app.py > backend.log 2>&1 &
                    '''
                }
            }
        }

        stage('Build Android App') {
            steps {
                dir('/Users/vikrantvani/Downloads/NutriTrack_test') {
                    sh './gradlew clean build'
                }
            }
        }

        stage('Install and Launch App') {
            steps {
                dir('/Users/vikrantvani/Downloads/NutriTrack_test') {
                    sh '''
                        # Find the debug APK
                        APK_PATH=$(find . -name "*debug*.apk" | head -n 1)

                        # If APK is found, install and launch
                        if [ -n "$APK_PATH" ]; then
                            echo "Installing APK: $APK_PATH"
                            adb install -r "$APK_PATH"

                            # Launch the app (make sure this matches your actual app's package and activity)
                            adb shell am start -n com.example.nutritrack_test/.MainActivity
                        else
                            echo "No debug APK found!"
                            exit 1
                        fi
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Stopping backend...'
            sh 'pkill -f app.py || true'
        }
    }
}