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
