pipeline {
    agent {
        label 'maven'
    }
    environment {
        DOCKER_IMAGE_NAME = 'devsec_spring_maven'
    }
    stages {
        stage('Build Images') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                sh 'docker build -t ${DOCKER_IMAGE_NAME} .'
            }
        }
        stage('Test Maven') {
            steps {
                echo "Running tests..."
                sh 'mvn test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Use the configured SonarQube server name (replace 'sonarqube_server' with your SonarQube configuration name)
                    withSonarQubeEnv('sonarqube_server') {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=sqp_019f2885144ada3796a9931347d41bbe78036c02 \
                        -Dsonar.projectName="Spring API AutoScan Jenkins"
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
        stage('Deploy to dev env') {
            steps {
                // Uncomment the line below to run the Docker container if required
                // sh 'docker run -d -p 9999:8080 ${DOCKER_IMAGE_NAME}'
                echo "Skipping deploy stage for now."
            }
        }
    }
}
