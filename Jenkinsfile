pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/Java-Techie-jt/devops-automation'
        BRANCH = 'main'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Cloning the repository from ${REPO_URL}..."
                }
                checkout([$class: 'GitSCM', branches: [[name: "*/${BRANCH}"]], extensions: [], userRemoteConfigs: [[url: "${REPO_URL}"]]])
            }
        }

        stage('Build project') {
            steps {
                script {
                    echo 'Compiling the project...'
                }
                sh './mvnw clean install'
            }
        }

        stage('Test') {
            parallel {
                stage('Test Units') {
                    steps {
                        script {
                            echo 'Running test...'
                        }
                        sh './mvnw test'
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    echo 'Packaging the application...'
                }
                sh './mvnw package'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            script {
                echo 'Pipeline completed successfully.'
            }
        }
        failure {
            script {
                echo 'The pipeline has failed.'
            }
        }
        always {
            script {
                echo 'Pipeline completed.'
            }
        }
    }
}
