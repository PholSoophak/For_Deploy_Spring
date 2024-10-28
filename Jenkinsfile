pipeline {
    agent {
        label 'maven'
    }
    environment {
        TIMESTAMP = sh(script: 'TZ="Asia/Phnom_Penh" date +%Y%m%d%H%M%S', returnStdout: true).trim()
        DOCKER_IMAGE_NAME = "devsec_spring_maven:${TIMESTAMP}"
    }
    stages {
        stage('Build Images') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                sh 'docker build -t ${DOCKER_IMAGE_NAME} .'
                sh 'docker images'
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
        // Wait for the SonarQube Quality Gate result
        // stage('Quality Gate') {
        //     steps {
        //         script {
        //             def qg = waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
        //             if (qg.status != 'OK') {
        //                 echo "Quality gate failed with status: ${qg.status}"
        //                 // Optionally set a warning or handle the failure in a custom way
        //             } else {
        //                 echo "Quality gate passed successfully."
        //             }
        //         }
        //     }
        // }
        stage('Upload Scan to DefectDojo') {
            steps {
                defectDojoPublisher(
                    defectDojoUrl: 'http://35.187.239.2:8080',
                    defectDojoCredentialsId: 'defectdojo_token', // Jenkins credentials ID for DefectDojo token
                    scanType: 'Dependency Check Scan',
                    artifact: 'target/dependency-check-report.xml',
                    autoCreateEngagements: true, // Automatically creates engagements if they don’t exist
                    autoCreateProducts: true, // Automatically creates products if they don’t exist
                    engagementName: 'Automated Engagement - Spring API',
                    productName: 'Spring API Automate Scan From Jenkins'
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
