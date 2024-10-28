pipeline {
    agent {
        label 'maven'
    }
    environment {
        GIT_TAG = sh(script: 'git describe --tags --abbrev=0 || echo "no-tag"', returnStdout: true).trim()
        COMMIT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        DOCKER_IMAGE_NAME = "devsec_spring_maven:${GIT_TAG == 'no-tag' ? COMMIT_HASH : GIT_TAG}-${COMMIT_HASH}"
    }
    stages {
        stage('Build Images') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                sh 'docker build -t ${DOCKER_IMAGE_NAME} .'
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
                        -Dsonar.projectKey=sqp_019f2885144ada3796a9931347d41bbe78036c02 \
                        -Dsonar.projectName="Spring API Automate Scan Jenkins Pipeline"
                        """
                    }
                }
            }
        }
        // Wait for the SonarQube Quality Gate result
        stage('Quality Gate') {
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
        }
        stage('Upload Scan to DefectDojo') {
            steps {
                script {
                    echo "Uploading scan to DefectDojo..."
                    def result = defectDojoPublisher(
                        defectDojoUrl: 'http://35.187.239.28:8080',
                        defectDojoCredentialsId: 'defectdojo_token',
                        scanType: 'Dependency Check Scan',
                        artifact: 'target/dependency-check-report.xml',
                        autoCreateEngagements: true,
                        autoCreateProducts: true,
                        engagementName: 'Automated Engagement - Spring API',
                        productName: 'Spring API Automate Scan From Jenkins'
                    )
                    echo "DefectDojo upload result: ${result}"
                }
            }
        }

        stage('Deploy to dev env') {
            steps {
                echo "ot torn deploy teee"
                echo "Skipping deploy stage for now."
            }
        }
    }
}
