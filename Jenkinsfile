pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/GMaikerYactayo/jenkins'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean install'
            }
        }
        stage('Test') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        sh './mvnw test -Dtest=*UnitTest'
                    }
                }
                stage('Integration Tests') {
                    steps {
                        sh './mvnw verify -Dtest=*IntegrationTest'
                    }
                }
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
        stage('Deploy') {
            steps {
                script {
                    def blueGreen = input message: 'Deploy to production?', parameters: [booleanParam(defaultValue: true, description: 'Deploy?', name: 'deploy')]
                    if (blueGreen) {
                        sh './deploy-blue-green.sh'
                    }
                }
            }
        }
    }
}
