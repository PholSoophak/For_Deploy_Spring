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
                    timeout(time: 10, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
        stage('Upload Scan to DefectDojo') {
            steps {
                defectDojoPublisher(
                    defectDojoUrl: 'http://35.187.239.28',
                    defectDojoTokenCredentialId: 'defectdojo_token', // Credentials ID configured in Jenkins for the DefectDojo API token
                    scanType: 'Dependency Check Scan',
                    artifact: 'target/dependency-check-report.xml',
                    autoCreateEngagement: true, // Automatically create engagement if not exists
                    autoCreateProduct: true, // Automatically create product if not exists
                    reimportScan: true, // Reupload scan if it already exists
                    engagementName: 'Automated Engagement - Spring API',
                    productName: 'Spring API Project',
                    environment: 'Development',
                    buildId: "${env.BUILD_ID}",
                    deduplicationOnEngagement: true,
                    closeOldFindings: true,
                    tags: 'v1,dependency-check'
                )
            }
        }
        stage('Deploy to dev env') {
            steps {
                echo "Skipping deploy stage for now."
            }
        }
    }
}
