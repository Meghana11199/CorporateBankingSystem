pipeline {
    agent any

    stages {
        stage('Docker Cleanup') {
            steps {
                bat 'docker-compose down || exit 0'
            }
        }

        stage('Build & Deploy') {
            steps {
                bat 'docker-compose up -d --build'
            }
        }
    }
}
