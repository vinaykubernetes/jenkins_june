// retry: In Jenkins, the retry mechanism is a feature used to automatically re-execute a stage or step in a pipeline if it fails. 
// This is particularly useful in scenarios where transient errors may occur, such as network issues or temporary unavailability of resources, allowing for more robust and resilient CI/CD processes.

// error: The primary purpose of the error step is to signal that an error has occurred, which results in the immediate failure of the current build. 
// This is particularly useful when certain conditions are not met, or when critical errors are detected during execution.
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                retry (3) {
                    echo "Welcome to retry pipeline usage"
                    //error "Testing the retry block" 
                }
                echo "Printing after 3 retries"
                
            }
        }
    }
}

// timeout: The timeout feature in Jenkins is used to set a time limit for a build or stage within a pipeline. If the build or stage exceeds the specified timeout duration, Jenkins will automatically abort the process. 
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                timeout(time: 2, unit: 'SECONDS') { // Values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS
                    echo "Sleeping for 60 seconds"
                    sleep 60
                }
            }
        }
    }
}


// Try an exmaple with both retry & timeout

pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                script {
                    retry(2) {
                        timeout(time: 2, unit: 'MINUTES') {
                            sh 'run-tests.sh'
                        }
                    }
                }
            }
        }
    }
}

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    // Retry the build stage up to 3 times with a 2-second delay between attempts
                    retry(3) {
                        // Timeout the build stage if it takes longer than 5 minutes 
                        timeout(time: 5, unit: 'MINUTES') {
                            echo 'Starting build...'
                            // Simulate a build process that might fail
                            if (new Random().nextBoolean()) {
                                error 'Build failed due to a random error!'
                            } else {
                                echo 'Build succeeded!'
                            }
                        }
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    // Retry the test stage up to 2 times with a 1-minute timeout
                    retry(2) {
                        timeout(time: 1, unit: 'MINUTES') {
                            echo 'Starting tests...'
                            // Simulate a test process that might fail
                            if (new Random().nextBoolean()) {
                                error 'Tests failed due to a random error!'
                            } else {
                                echo 'Tests passed!'
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
