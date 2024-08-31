pipeline {
    agent {
        label 'k8s-slave'
    }
    environment {
        APPLICATION_NAME = "eureka"
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()
        DOCKER_HUB = docker.io/i27devopsb2
        DOCKER_CREDS = credentials('pass the ID') 
    }
    tools {
        maven 'my_maven'
        JDK 'my_jdk'
    }
    stages {
        stage('Build') {
            steps {
                echo "We are building ${env.APPLICATION_NAME} application"
                sh "mvn clean package -DskipTests=true"
                archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
            }
        }
        stage('unit_tests') {
            steps {
                sh "mvn test"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Docker_Format') {
            steps {
                script { //i27-eureka-buildnumber-branch-name.jar 
                    sh """
                        echo "Actual format is: i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                        echo "custom format is: i27-${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                    """
                }
            }
        }
        stage('Docker_Build_&_Push') {
            steps {
                script {
                    sh """
                        ls -la
                        cp ${workspace}/target/127-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd
                        ls -la ./.cicd
                        // Docker Build 
                        docker build --force-rm --no-cache --pull --rm=true --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd
                        docker images
                        docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}
                        docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} 
                    """
                }
            }
        }
        stage('Deploy to Dev') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maha_docker_vm_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    //some block
                    // with the help of this block, the slave will be connecting to docker-vm and execute the commands to create the containers
                    // sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@$docker_server_ip"
                    script {
                        // Pull the image on the docker server
                        sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no $USERNAME@$docker_server_ip docker pull ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"

                        // The host port is what external users or systems connect to.
                        // The container port is what the application inside the container listens to.
                        // The mapping between the two allows external access to services running in Docker containers.

                        try {
                        // Stop the container
                        sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no $USERNAME@$docker_server_ip docker stop ${APPLICATION_NAME}-Dev"

                        // Remove the container  
                        sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no $USERNAME@${docker_server_ip} docker rm ${APPLICATION_NAME}-Dev"
                        }   
                        
                        catch(err) {
                            echo "Caught the error: $err" 
                        }

                        // Create a container  
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@${docker_server_ip} docker run -d -p 5761:8761 --name ${APPLICATION_NAME}-Dev ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
                    }
                }
            }
        }
        stage('Deploy to Test') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maha-docker_vm_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    //some block
                    // with the help of this block, the slave will be connecting to docker-vm and execute the commands to create the containers
                    // sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@$docker_server_ip"
                    script {
                        // Pull the image on the docker server
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@${docker_server_ip} docker pull ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"

                        // The host port is what external users or systems connect to.
                        // The container port is what the application inside the container listens to.
                        // The mapping between the two allows external access to services running in Docker containers.

                        try {
                        // Stop the container
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@${docker_server_ip} docker stop ${APPLICATION_NAME}-tst"

                        // Remove the container  
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@${docker_server_ip} docker rm ${APPLICATION_NAME}-tst"
                        } 
                        
                        catch(err) {
                            echo "Caught the error: $err"
                        }

                        // Create a container
                        sh "sshpass -p '$PASSWORD' -v ssh -o StrictHostKeyChecking=no $USERNAME@${docker_server_ip} docker run -d -p 6761:8761 --name ${APPLICATION_NAME}-tst ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
                    }
                }
            }
        }
    }
}



eureka-06-branch_name.packaging

${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING} 