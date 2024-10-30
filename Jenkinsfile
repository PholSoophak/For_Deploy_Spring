pipeline {
    agent {
        label 'maven'
    }
    environment {
        TIMESTAMP = sh(script: 'TZ="Asia/Phnom_Penh" date +%d%m%Y%H%M', returnStdout: true).trim()
        DOCKER_IMAGE_NAME = "devsec_spring_maven:${TIMESTAMP}"

        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'  // Set to Java 17 path
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }
    stages {
        stage('Build Images') {
            steps {
                sh 'mvn clean install'
                sh 'mvn package'
                // Run OWASP Dependency Check to generate the vulnerability report
                echo'Check scan dependencies'
                sh ' '
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

        // stage('SonarQube Analysis') {
        //     steps {
        //         script {
        //             withSonarQubeEnv('sonarqube_server') {
        //                 sh """
        //                 mvn sonar:sonar \
        //                 -Dsonar.projectKey=sqp_019f2885144ada3796a9931347d41bbe78036c02 \
        //                 -Dsonar.projectName="Spring API Automate Scan Jenkins Pipeline"
        //                 """
        //             }
        //         }
        //     }
        // }
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonarqube_server') {
                        sh '''
                        JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \
                        /var/opt/sonar-scanner-4.8.0.2856-linux/bin/sonar-scanner \
                        -Dsonar.projectKey=main \
                        -Dsonar.projectName="Scan multi Project Automate Scan" \
                        -Dsonar.sources="src/main/java" \
                        -Dsonar.java.binaries="target/classes"
                        '''
                    }
                }
            }
        }


        
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
                echo "Not yet deploying to dev environment."
                echo "Skipping deploy stage for now."
            }
        }
    }
}
