pipeline {
    agent {
        label 'maven'
    }
    environment {
        // Replace with your SonarQube instance and token
        SONARQUBE_SERVER = 'http://34.124.243.166:9001'
        SONARQUBE_TOKEN = 'squ_f532b03bc7446ec1f97b404ca40ae1fb04a8a9b3'
    }
    stages {
        stage('Build_Docker_Images') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                sh 'docker build -t sophak12/spring-api .'
            }
        }
        stage('Test') {
            steps {
                echo "Running tests..."
                sh 'mvn test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Use SonarQube environment and specify the login token
                    withSonarQubeEnv('SonarQube') {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=sqp_019f2885144ada3796a9931347d41bbe78036c02 \
                        -Dsonar.projectName="Spring API AutoScan Jenkins" \
                        -Dsonar.host.url=${SONARQUBE_SERVER} \
                        -Dsonar.login=${SONARQUBE_TOKEN}
                        """
                    }
                }
            }
        }
        stage('Quality Gate') {
            steps {
                script {
                    // Wait for SonarQube analysis to complete and check the quality gate status
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
        stage('Deploy to Dev ENV') {
            steps {
                // Uncomment the line below to run the Docker container if required
                // sh 'docker run -d -p 9999:8080 sophak12/spring-api'
                echo "Skipping deploy stage for now."
            }
        }
    }
}
