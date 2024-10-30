pipeline {
    agent {
        label 'maven'
    }
    environment {
        TIMESTAMP = sh(script: 'TZ="Asia/Phnom_Penh" date +%d%m%Y%H%M', returnStdout: true).trim()
        DOCKER_IMAGE_NAME = "devsec_spring_maven:${TIMESTAMP}"
    }
    stages {
        stage('Build Images') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                // Run OWASP Dependency Check to generate the vulnerability report
                echo 'Check scan dependencies'
                sh 'mvn org.owasp:dependency-check-maven:check'
                // Build the Docker image with dynamic tagging
                sh "docker build -t ${DOCKER_IMAGE_NAME} ."
                echo "Docker image built: ${DOCKER_IMAGE_NAME}"  // Echo the new image name and tag
                sh 'docker images | grep devsec_spring_maven'    // Show the newly built image specifically
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
                    withSonarQubeEnv('sonarqube_server') {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=sqp_979f7ec25470bc72daec5132f153921f3abb4627 \
                        -Dsonar.projectName=" New Spring API Automate Scan Jenkins Pipeline"
                        """
                    }
                }
            }
        }
        
        /* stage('Quality Gate') {
            steps {
                script {
                    def qg = waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                    if (qg.status != 'OK') {
                        echo "Quality gate failed with status: ${qg.status}"
                        // Optionally set a warning or handle the failure in a custom way
                    } else {
                        echo "Quality gate passed successfully."
                    }
                }
            }
        } */

        stage('Upload Scan to DefectDojo') {
            steps {
                script {
                    echo "Uploading scan to DefectDojo..."
                    def result = defectDojoPublisher(
                        defectDojoUrl: 'http://35.187.239.28:8080',
                        defectDojoCredentialsId: 'Token 612f6ebd1ad696fb4bc685a806e84d8c716d1b94',
                        scanType: 'Dependency Check Scan',
                        artifact: 'owasp-dependency-check-logs/dependency-check-report.json',  // Updated path
                        autoCreateEngagements: true,
                        autoCreateProducts: true,
                        engagementName: 'New Automated Engagement - Spring API',
                        productName: 'Spring API Automate Scan From Jenkins'
                    )
                    echo "DefectDojo upload result: ${result}"
                }
            }
        }


        stage('Deploy to dev env') {
            steps {
                echo "Not yet deploying to dev environment."
                echo "Skipping deploy stage for now."
            }
        }
    }
}
