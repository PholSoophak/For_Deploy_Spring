pipeline {
    agent {
        label 'maven'
    }
    environment {
        DATE_TIME = sh(script: 'TZ="Asia/Phnom_Penh" date +%d%m%Y%H%M', returnStdout: true).trim()
        DOCKER_IMAGE_NAME = "devsec_spring_maven:${DATE_TIME}"
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
        stage('Verify Scan File') {
            steps {
                sh 'ls -l target/dependency-check-report.xml'  // Confirm file presence
                sh 'cat target/dependency-check-report.xml'    // Optional: Display file content for verification
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
                echo "Skipping deploy stage for now."
            }
        }
    }
}
