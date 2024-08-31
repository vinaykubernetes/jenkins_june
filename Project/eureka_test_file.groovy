pipeline {
    agent { 
        label 'k8s-slave' 
    }
    parameters {
        choice(name: 'buildOnly',
            choices: 'no\nyes', 
            description: 'This will only build the application'  
        )
        choice(name: 'scanOnly',
            choices: 'no\nyes', 
            description: 'This will only scan the application'  
        )
        choice(name: 'dockerPush',
            choices: 'no\nyes',
            description: 'This will only build the application, docker build and docker push' 
        )
        
    }
    environment {
        APPLICATION_NAME = "eureka"
        SONAR_URL = "http://54.161.55.166:9000" 
        SONAR_TOKEN = credentials('pass the ID')
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
                script {
                    build().call()
                }
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
        stage('Sonar') {
            steps {
                withSonarQubeEnv('soanrQube') {
                    sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.porjectKey= i27-${env.APPLICATION_NAME} \
                        -Dsonar.host.url=${env.SONAR_URL} \
                        -Dsonar.login=${env.SONAR_TOKEN} 
                    """
               } 
               timeout(time: 5, unit: 'MINUTES') {
                    script {
                        waitForQualityGate abortPipeline: true 
                    }
                }
            } 
        }
        stage('Docker_Build_&_Push') {
            steps {
                script {
                    dockerBuildandPush().call()
                }
            }
        }
        stage('Deploy to Dev') {
            steps {
                dockerDeploy(DEV, 5761, 8761).call()
            }
        }
        stage(Deploy to Test) {
            steps {
                dockerDeploy(TEST, 6761, 8761).call()
            }
        }
    }
}

def build() {
    return {
        echo "We are building ${env.APPLICATION_NAME} application"
        sh "mvn clean package -DskipTests=true"
        archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
}
def dockerBuildandPush() {
    return {
            sh "ls -la"
            sh "cp ${workspace}/target/127-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd"
            sh "ls -la ./.cicd"
            // Docker Build 
            sh "docker build --force-rm --no-cache --pull --rm=true --build-arg JAR_SOURCE=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd"
            sh "docker images"
            sh "docker login -u ${DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"
            sh "docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"
    }
}

def dockerDeploy(envDeploy, hostPort, contPort) {
    return {
        withCredentials([usernamePassword(credentialsId: 'maha_docker_vm_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            script {
                sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no $USERNAME@$docker_server_ip docker pull ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:$GIT_COMMIT"

                    try {
                        sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no $USERNAME@$docker_server_ip docker stop ${APPLICATION_NAME}-$envDeploy"

                        sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no $USERNAME@$docker_server_ip docker rm ${APPLICATION_NAME}-$envDeploy"
                    } 

                    catch(err) {
                        echo "Caught an error: $err"
                    }

                sh "sshpass -p '$PASSWORD' -v ssh -o strictHostKeyChecking=no ${USERNAME}@${docker_server_ip} docker run -d -p $hostPort:$contPort --name: ${APPLICATION_NAME}-$envDeploy ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
            }
        }
    }
}