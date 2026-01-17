pipeline {
    agent any

    stages {
        stage('Deploy using Docker Compose') {
            steps {
                bat 'docker-compose down'
                bat 'docker-compose up -d --build'
            }
        }
    }
}
