* Job-name: i27-eureka
* branch-name: master
* Jenkins-slave work directory: /home/i27k8s10/jenkins/workspace/i27-eureka_master/target/i27-eureka-0.0.1-snapshot.jar

# Naming convention of "i27-eureka-0.0.1-snapshot.jar"
* artifactId-version.jar 

# Exisitng JAR Format: 
* i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
* i27-eureka-0.0.1-SNAPSHOT.jar

# The JAR Format we want: 
* i27-${env.APPLICATION_NAME}-${currentBuild.number}-${BRANCH_NAME}.${env.POM_PACKAGING}"
* 127-eureka-03-master.jar

* ${workspace} - /home/i27k8s10/jenkins/workspace/i27-eureka_master/

* github repo-name: i27devopsb2

* docker_hub: docker.io/i27devopsb2
* docker_hub username: i27devopsb2
* docker_repo-name: eureka

* docker build -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd 
  docker.io/i27devopsb2/eureka:e77d378

-----------------------------------------------------------------------------------------------------------------------
# Session -5
## Sonar

# First create a token in sonarQube UI
1. Ipaddress:9000 >> click on right side A >> My Account >> enter name, Type: Global analysis token (It will be available    for all the projects), User Token (It will be availble for only one user) >> mention expiration date >> Generate

2. Configure sonarQube credentials in jenkins. 
manage jenkins >> credentials

3. Inside pipeline >> environment mention SONAR_URL AND SONAR_CREDS.

4. Create a project in manual way in SonarQube Dashboard
## project display name:
* i27-eureka
## Project key
* 127-eureka

5. After this it will ask to provide the generated token.

6. After filling all the above fields sonarQube will provide a command to run sonar as below
*  mvn clean verify sonar:sonar \
       -Dsonar.projectKey=i27-${env.APPLICATION_NAME} \ 
       -Dsonar.host.url=${env.SONAR_URL} \
       -Dsonar.login=${env.SONAR_TOKEN}

7. Create a Quality gate 
* Name: i27-gate

# Add condition
* Click on "on overall code" radio button 
# Quality gate fails when
* select "code smells" from the drop down
* operator is greater than value "10" (Means if the "code smells are greater than 10 then sonar stage should fail")

* So, till now, we created a quality gate with code smells as a condition and made that gate as a default.
* Now, when the jenkins build triggers, my sonar should pass/fail based on the results(Gate results). 

* Here sonarQube is showing errors and it was a failure but jenkins pipeline is successfull.
* So, sonarQube should tell jenkins pipeline (sonar stage to fail).
* For this we have to use waitForQualityGate method. 

* waitForQualityGate: Wait for SonarQube analysis to be completed and return quality gate status
This step pauses Pipeline execution and wait for previously submitted SonarQube analysis to be completed and returns quality gate status. Setting the parameter abortPipeline to true will abort the pipeline if quality gate status is not green.

Requirements: 

SonarQube server 6.2+
Configure a webhook in your SonarQube server pointing to <your Jenkins instance>/sonarqube-webhook/. The trailing slash is mandatory!
Use withSonarQubeEnv step to run your analysis prior to use this step

---------------------------------------------------------------------------------------------------------------------------

# Create a DEV server for deploying the code  (In realtime we will not do Docker Deployments)
* Just for knowledge purpose we are doing it
* We should not say in interview that we have done Docker Deployments.

# If the slave machine wants to communicate with other machine it should have authentication mechanism.
* First we need to connect to a server. There are multiple ways,
* cat /etc/passwd (Here all created users are available)(DEV machine)
* ssh maha@Dev_server_ip (Gitbash here)
* Add credentials in Jenkins(Refer notes)

* In pipeline syntax >> snippet gernerator >> select "withCredentials: Bind credentials to variables"
* Add Bindings >> username and password (Separated)
* username variable: USERNAME
* password variable: PASSWORD

* withCredentials([()])....
* Now, we have to install sshpass (sudo apt install sshpass)(sshpass is a package) in Dev server & java-slave machine as well or we need to put it in ansible yaml file.
* manage jenkins >> system >> Global properties >> click on checkbox "Environment variables"
Name: docker_server_ip
value: paste the docker_server_ip (34.54.435.230)
save it.

Under prod stage or under any other stage we can keep "cleanWs()" to remove the workspace/job we are building.
