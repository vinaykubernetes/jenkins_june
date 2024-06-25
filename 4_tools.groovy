pipeline {
    agent any
    tools {
        maven 'Maven3.8.8'
    }
    stages {
        stage('Maven') {
            steps {
                echo "Hello!, Welcome to tools usage"
                sh "mvn --version"
            }
        }
        stage('OtherMaven') {
            tools {
                jdk "JDK17"
            }
            steps {
                echo "Maven version with JDK17"
                sh "mvn --version"
            }
        }
    }
}


