// Script allows us to write custom code in groovy
// This script block should be available in steps block 
// If we are having any complex environment, We can use script under steps block
// Script uses groovy as the programming language
pipeline { 
    agent any 
    stages {
        stage("hello") {
            steps {
                echo "Just hello" 

            }
        }
        stage('ScriptedStage') { 
            steps {
                script {
                    def course = "DevOps" 
                    if(course == "DevOps") 
                    {
                        println("Welcome Vinay!, You are enrolled to ${course}")
                    }
                    else 
                        println("Do Enroll")
                    //just sleep for 10 seconds
                    // This will pause the pipelines, till the time mentioned   
                    sleep 10
                    echo "Script block ended *************************"
                }
            }
        }
    }
}

/*
def course = 'DevOps'
if(course == 'DevOps')
{
    println("Welcome to DevOps course!, You are enrolled")
}
else
    println("Do Enroll")
*/


pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo "Just Hello"
            }
        }
        stage('scripting') {
            steps {
                script {
                    def course = 'DevOps' 
                    if(course == 'DevOps')
                    {
                        println("congrats! You are enrolled to ${course}")
                    }
                    else {

                        println("Do enroll")
                    }
                }
            }
        }
    }
}

















