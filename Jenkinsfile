pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-8' // Use a Maven Docker image
            label 'maven'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                sh 'docker build -t sophak12/spring-api .'
            }
        }
        stage('Test') {
            steps {
                echo "Jes tae tes leng tov :)"
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker run -d -p 9999:8080 sophak12/spring-api'
            }
        }
    }
}
