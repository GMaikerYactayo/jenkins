pipeline {
    agent any
    environment {
        REPO_URL = 'https://github.com/GMaikerYactayo/jenkins'
        BRANCH = 'main'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Clonando el repositorio desde ${REPO_URL}..."
                }
                checkout([$class: 'GitSCM', branches: [[name: "*/${BRANCH}"]], extensions: [], userRemoteConfigs: [[url: "${REPO_URL}"]]])
            }
        }
        stage('Build') {
            steps {
                script {
                    echo 'Compilando el proyecto...'
                }
                sh './mvnw clean install'
            }
        }
        stage('Test') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        script {
                            echo 'Ejecutando pruebas unitarias...'
                        }
                        sh './mvnw test -Dtest=*UnitTest'
                    }
                }
                stage('Integration Tests') {
                    steps {
                        script {
                            echo 'Ejecutando pruebas de integración...'
                        }
                        sh './mvnw verify -Dtest=*IntegrationTest'
                    }
                }
            }
        }
        stage('Package') {
            steps {
                script {
                    echo 'Empaquetando la aplicación...'
                }
                sh './mvnw package'
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
        stage('Deploy') {
            steps {
                script {
                    echo 'Preparándose para desplegar...'
                    def blueGreen = input message: '¿Desplegar a producción?', parameters: [booleanParam(defaultValue: true, description: '¿Desplegar?', name: 'deploy')]
                    if (blueGreen) {
                        sh './deploy-blue-green.sh'
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                echo 'Pipeline completado exitosamente.'
            }
            // Aquí puedes agregar una notificación en caso de éxito, por ejemplo, a Slack o correo electrónico.
        }
        failure {
            script {
                echo 'El pipeline ha fallado.'
            }
            // Aquí puedes agregar una notificación en caso de fallo.
        }
        always {
            script {
                echo 'Pipeline finalizado.'
            }
            // Aquí puedes agregar pasos que deben ejecutarse siempre, como limpiar archivos temporales.
        }
    }
}
