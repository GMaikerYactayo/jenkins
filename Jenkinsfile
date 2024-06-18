pipeline {
    agent any

    tools {
        dockerTool 'Docker'
    }

    environment {
        REPO_URL = 'https://github.com/GMaikerYactayo/jenkins'
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

        stage('Deploy to Kubernetes'){
            steps{
                script{
                    echo 'Deploying to Kubernetes...'
                    withCredentials([file(credentialsId: 'k8s', variable: 'CONFIG_FILE')]) {
                        sh "kubectl --kubeconfig=${CONFIG_FILE} version"
                    }
                }
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

        stage('Build docker image'){
            steps{
                script{
                    echo 'Building image....'
                }
                sh 'docker version'
                sh 'docker build -t maikergonzales/jenkins-service:v1 -f Dockerfile .'
            }
        }

        stage('Push image to Hub'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'PASSWORD_DOCKER', variable: 'dockerhubpwd')]) {
                        echo '${dockerhubpwd}'
                        sh 'docker login -u maikergonzales -p ${dockerhubpwd}'
                    }
                    sh 'docker push maikergonzales/jenkins-service:v1'
                }
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