pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '1'))
    }
    stages {
        stage('Build') {
            steps {
                echo "Hello World!"
            }
        }
    }
}

stage

steps
agent
parallel
when
tools
environment
options
post

agent 
tools
stages
options
parameters
shared libraries
post 
environment

steps:
rerty
timeout
error
script
sleep
input