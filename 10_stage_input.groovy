// The input directive in Jenkins is used to pause the execution of a pipeline and prompt the user for input during the build process. 
//  This feature is particularly useful for interactive pipelines where human intervention is required to make decisions or provide information before proceeding with subsequent stages
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




