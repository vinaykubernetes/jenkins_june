pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                timeout (time: 300, unit: 'SECONDS') {
                    input message: "Are you building the application?", ok: "Yes", submitter: "vinay"
                }
                echo "Building the application"
            }
        }
    }
}




