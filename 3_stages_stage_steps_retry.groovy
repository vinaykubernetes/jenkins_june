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

// timeout
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

