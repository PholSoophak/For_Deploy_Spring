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
                    defectDojoUrl: 'http://35.187.239.28:8080',
                    defectDojoCredentialsId: 'defectdojo_token', // Corrected credentials ID parameter
                    scanType: 'Dependency Check Scan',
                    artifact: 'target/dependency-check-report.xml',
                    autoCreateEngagements: true, // Corrected parameter for auto-creating engagements
                    autoCreateProducts: true, // Corrected parameter for auto-creating products
                    reimport: true, // Corrected parameter for re-uploading scans
                    engagementName: 'Automated Engagement - Spring API',
                    productName: 'Spring API Project',
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
