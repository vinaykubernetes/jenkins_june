pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo "Executing multi branch pipeline"
            }
        }
        stage('Test') {
            steps {
                echo "Executing Test stage"
            }
        }
        stage('deploytodev') {
            steps {
                echo "Executing dev deployment stage"
            }
        }
        stage('deploytoprod') {
            steps {
                echo "Executing prod deployment stage"
            }
        }
    }
}