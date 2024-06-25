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


//2****

Pipeline {
    agent {
        label 'mvn-slave' // Mostly we use labels only, not nodes
    }
    stages {
        stage('label_stage') {
            steps { 
                // This should give the private IP of my java_slave machine
                sh 'hostname -i'
            }
        }
    }
}


//3********

pipeline {
    // The below agent is at pipeline level and applies for all the stages
    agent none
    stages {
        stage('Build') {
            agent {
                node {
                    label 'mvn-slave'
                    customWorkspace "/home/ec2-user/customvinay"
                }
            }
            steps {
                echo "Hello!, Executing node in agent"
                sh 'hostname -i'
                sh 'cat imp.txt'
            }
        }
    }
}

// 4************

pipeline{
    agent none
    stages {
        stage('Build') {
            agent {
                node {
                    label 'mvn-slave'
                    customWorkspace "/home/ec2-user/customvinay"
                }
            }
            steps {
                echo "Hello!, Welcome to node pipeline"
                sh 'hostname -i'
                sh 'cat imp.txt'
                git branch: 'main', url: 'https://github.com/spring-projects/spring-petclinic.git'
                //sh 'git clone https://github.com/spring-projects/spring-petclinic.git'
            }
        }
    }
}



// 5*********** 
