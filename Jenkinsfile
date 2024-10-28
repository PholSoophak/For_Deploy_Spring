pipeline {
    agent {
        label 'maven'
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
                echo "Jes tae test leng tov :)"
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                // sh 'docker run -d -p 9999:8080 sophak12/spring-api'
                echo "Not yet run deploy"
            }
        }
    }
}
