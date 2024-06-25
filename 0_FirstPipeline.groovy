// This is the comment

/*
Multi line comments
*/

//agent
// any: We will execute the pipeline or stage with any available agent
// label: Ideally this is a string, which informs our jenkins to run on a particular slave. 
// none: When we apply none, no global agent will be picked. The individual space should specify the respective agent, based on their requirements.
pipeline { //Top level field
    agent any
    stages {
        stage('FirstStage') { // Name: Can be a userfriendly name, but needs to be specific for the task performing
            steps {
                echo "Welcome to first pipeline"
            }

        }
    }
}