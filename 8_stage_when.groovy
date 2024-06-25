// When 

pipeline {
    agent any
    environment {
        DEPLOY_TO = 'production'
    }
    stages {
        stage('Deploy') {
            when {
                environment name: 'DEPLOY_TO', value: 'production'
            }
            steps {
                echo "Deploying...."
            }
        }
    }
}

// equals condition
pipeline {
    agent any
    environment {
        DEPLOY_TO = "production"
    }
    stages { 
        stage('Deploy') {
            when {
                //environment name: 'DEPLOY_TO', value: "production" 
                //equals expected: "production", actual: "${DEPLOY_TO}"
                //equals expected: 14, actual: "${BUILD_NUMBER}"
                equals expected: 14, actual: currentBuild.number
            }
            steps {
                echo "Deploying...."
            }
        }
    } 
}  

// Not Condition 
//Note: Here the output is success, If the expected is not matching with the value in environment, then it should success because we are using not condition)

pipeline {
    agent any
    environment {
        DEPLOY_TO = "production"
    }
    stages { 
        stage('Deploy') {
            when {
                not {
                    equals expected: "prod", actual: "${DEPLOY_TO}"
                }
                //environment name: 'DEPLOY_TO', value: "production" 
                //equals expected: 14, actual: "${BUILD_NUMBER}"
                //equals expected: 14, actual: currentBuild.number
            }
            steps {
                echo "Deploying...." 
            }
        }
    }
} 

// Not Condition 
//Note: Here the output is Skipped (If the expected is matching with the value in environment, then it should be Skipped because we are using not condition)

pipeline {
    agent any
    environment {
        DEPLOY_TO = "production"
    }
    stages { 
        stage('Deploy') {
            when {
                not {
                    equals expected: "production", actual: "${DEPLOY_TO}"
                }
                //environment name: 'DEPLOY_TO', value: "production" 
                //equals expected: 14, actual: "${BUILD_NUMBER}"
                //equals expected: 14, actual: currentBuild.number
            }
            steps {
                echo "Deploying...." 
            }
        }
    }
} 


//Branch based deployment
// for this it should be a multibranch pipeline
// If i want to deploy my microservice or my application into a particular stage then the pipeline branch should be release
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo "Welcome to build stage"
            }
        }
        stage('Deploy to Dev') {
            steps {
                echo "Deploying to Dev environment"
            }
        }
        stage('Deploy to Stage') {
            when {
                expression {
                    // This stage should execute with either production or with staging branch
                    BRANCH_NAME ==~ /(production|staging)/
                }
            }
            steps {
                echo "Deploying to Stage environment"
            }
        }
    }
}



// allOf

pipeline {
    agent any
    environment {
        DEPLOY_TO = 'production' //this is static. we shall see dynamic in parameter section
    }
    stages {
        stage('Build') {
            steps {
                echo "Welcome to build stage"
            }
        }
        stage('Deploy to Dev') {
            steps {
                echo "Deploying to Dev environment"
            }
        }
        stage('Deploy to Stage') {
            when {
                allOf {
                    // The below all conditions should be satisfied inorder for this stage to execute
                    branch 'production'
                    environment name: 'DEPLOY_TO', value: 'production'
                }
            }
            steps {
                echo "Deploying to Stage environment"
            }
        }
    }
}


pipeline {
    agent any
    environment {
        DEPLOY_TO = 'productions'
    }
    stages {
        stage('Build') {
            steps {
                echo "Welcome to build stage"
            }
        }
        stage('Deploy to Dev') {
            steps {
                echo "Deploying to Dev environment"
            }
        }
        stage('Deploy to Stage') {
            when {
                allOf {
                    branch 'production'
                    environment name: 'DEPLOY_TO', value: 'production'
                }
            }
            steps {
                echo "Deploying to Stage environment"
            }
        }
    }
}

//anyOf

pipeline {
    agent any
    environment {
        DEPLOY_TO = 'production'
    }
    stages {
        stage('Build') {
            steps {
                echo "Welcome to build stage"
            }
        }
        stage('Deploy to Dev') {
            steps {
                echo "Deploying to Dev environment"
            }
        }
        stage('Deploy to stage') {
            when {
                anyOf { 
                    // Any of the below conditions should satisfy for this stage to be executed
                    branch 'production'
                    environment name: 'DEPLOY_TO', value: 'productions'
                }
            }
            steps {
                echo "Deploying to stage environment"
            }
        }
    }
}


// I want to execute stage environment when the branch is release/**** and tag in prod only

pipeline {
    agent any
    stages {
        stage('UAT') {
            when {
                branch 'release-*'
            }
            steps {
                echo "Deploying to stage environment"
            }
        }
        stage('prod') {
            when {
                // This stage should execute with tag only
                // buildingTag()
                //v1.2.3
                tag pattern: "v\\d{1,2}.\\d{1,2}.\\d{1,2}", comparator: "REGEXP" 
            }
            steps {
                echo "Deploying to production environment" 
            }
        }
    }
}
























