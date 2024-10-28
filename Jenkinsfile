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
                    defectDojoTokenId: 'defectdojo_token', // Jenkins credentials ID for DefectDojo token
                    scanType: 'Dependency Check Scan',
                    filePath: 'target/dependency-check-report.xml',
                    active: true,
                    verified: true,
                    closeOldFindings: true,
                    deduplicationOnEngagement: true,
                    tags: 'v1,dependency-check',
                    testTitle: 'Dependency Check Maven',
                    description: 'Automated scan upload from Jenkins pipeline'
                )
            }
        }
        stage('Deploy to dev env') {
            steps {
                echo "Not Yet Deployed"
                echo "Skipping deploy stage for now."
            }
        }
    }
}
