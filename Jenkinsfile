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
                stage('Test Integration Repository') {
                    steps {
                        script {
                            echo 'Running ProductRepositoryTest...'
                        }
                        sh './mvnw test -Dtest=ProductRepositoryTest'
                    }
                }

                stage('Test Units Service') {
                    steps {
                        script {
                            echo 'Running ProductServiceTest...'
                        }
                        sh './mvnw test -Dtest=ProductServiceTest'
                    }
                }

                stage('Test Units Controller') {
                    steps {
                        script {
                            echo 'Running ProductControllerTest...'
                        }
                        sh './mvnw test -Dtest=ProductControllerTest'
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
