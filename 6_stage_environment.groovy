// This environment variable can be used at pipeline level and stage level

pipeline {
    agent any
    // This environment variable can be used accross all the stages 
    environment { // key value pairs
        name = "Vinay"
        course = "jenkins"
    }
    stages {
        stage('Build') {
            // These environment variables are specific to stage only
            environment {
                cloud = "GCP"
            }
            steps {
                echo "Welcome $name"
                echo "You are enrolled to $course course"
                echo "You are certified in ${cloud}"
            }
        }
    }
}

//

pipeline {
    agent any
    // This environment variable can be used accross all the stages 
    environment { // key value pairs
        name = "Vinay"
        course = "jenkins"
    }
    stages {
        stage('Build') {
            // These environment variables are specific to stage only
            environment {
                cloud = "GCP"
            }
            steps {
                echo "Welcome $name"
                echo "You are enrolled to $course course"
                echo "You are certified in ${cloud}"
            }
        }
        stage('SecondStage') { 
            steps {
                echo "Welcome $name"
                echo "You are enrolled to $course course"
                echo "You are certified in ${cloud}"
            }
        }
    }
}

// Lets test presedence (means, if we mention same variable in different layers or stages what will effect?)
// Note: whatever the variable we mention in stage level that will be taking into consideration first, compared to the same variable that is been available in the pipeline level
pipeline {
    agent any
    // This environment variable can be used accross all the stages 
    environment { // key value pairs
        name = "Vinay"
        course = "jenkins" 
    }
    stages {
        stage('Build') {
            // These environment variables are specific to stage only 
            environment {
                cloud = "GCP" 
                name = "vikas"
            }
            steps {
                echo "Welcome $name" // (Here, it will display the name as "vikas" as it will first prefer the variable name in stage level)
                echo "You are enrolled to $course course"
                echo "You are certified in ${cloud}"
            }
        }
    }
}

//

pipeline {
    agent any
    // This environment variable can be used accross all the stages 
    environment { // key value pairs
        name = "Vinay"
        course = "jenkins" 
    }
    stages {
        stage('Build') {
            // These environment variables are specific to stage only 
            environment {
                cloud = "GCP" 
                name = "vikas"
            }
            steps {
                echo "Welcome $name" // (Here, it will display the name as "vikas" as it will first prefer the variable name in stage level)
                echo "You are enrolled to $course course"
                echo "You are certified in ${cloud}"
                sh "printenv" // (By default, whatever the variables available in jenkins, all those variable will be printing here)
            }
        }
    }
}



















