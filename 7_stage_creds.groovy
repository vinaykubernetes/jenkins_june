pipeline {
    agent any
    environment {
        // credentials('id'), this id should be the same from jenkins credentials
        GITHUB_CREDS = credentials('devops-github-creds')
    }
    stages {
        stage('Build') {
            steps {
                echo "Github Credentials are ${GITHUB_CREDS}"
                echo "Username is ${GITHUB_CREDS_USR}"
                echo "Password is ${GITHUB_CREDS_PSW}"
            }
        }
    }
}

withCredentials([usernamePassword(credentialsId: 'devops-github-creds', usernameVariable: 'MY_USERNAME', passwordVariable: 'MY_PASSWORD')]) {
  // available as an env variable, but will be masked if you try to print it out any which way
  // note: single quotes prevent Groovy interpolation; expansion is by Bourne Shell, which is what you want
  sh 'echo $PASSWORD'
  // also available as a Groovy variable
  echo USERNAME
  // or inside double quotes for string interpolation
  echo "username is $USERNAME"

  docker login -u "$MY_USERNAME" -P "MY_PASSWORD"
}