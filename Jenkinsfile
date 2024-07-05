pipeline {
    agent {
        node {
            label 'maven'
        }
    }
    tools {
        maven 'Maven 3.6.3' // Use the name given in Global Tool Configuration
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
