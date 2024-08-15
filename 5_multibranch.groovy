// The multibranch pipeline type enables you to implement different Jenkinsfiles for different branches of the same project.
// 9. Here whenever we are building this pipeline it is automatically deploying into all stages from build to prod But this is a wrong approach
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