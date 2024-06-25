// Parallel
pipeline {
    agent any 
    stages {
        stage ('Stages Running in Parallel') {
            parallel {
                stage ('SonarScan') {
                    // Here, we can mention labels to run on sonar slave node
                    steps {
                        echo "Executing Sonar Scan"
                        sleep 10
                    }
                }
                stage ('FortifyScan') { 
                    // Here, we can mention labels to run on fortify slave node
                    steps {
                        echo "Executing Fortify Scan"
                        sleep 10
                    }
                }
                stage ('Checkmarx Scan') { 
                    // Here, we can mention labels to run on checkmarx slave node
                    steps {
                        echo "Executing Checkmarx Scan"
                        sleep 10
                    }
                }
            }
        }

    }
}


// 
pipeline {
    agent any 
    stages {
        stage ('Build') {
            steps {
                echo "Building the Cart service"
            }
        }
        stage ('Stages Running in Parallel') {
            failFast true
            parallel {
                stage ('SonarScan') {
                    steps {
                        echo "Executing Sonar Scan"
                        sleep 10
                    }
                }
                stage ('FortifyScan') {
                    steps {
                        echo "Executing Fortify Scan"
                        sleep 10
                        error "Simulating error during fortify"
                    }
                }
                stage ('Checkmarx Scan') {
                    steps {
                        echo "Executing Checkmarx Scan"
                        sleep 10
                    }
                }
            }
        }
    }
}