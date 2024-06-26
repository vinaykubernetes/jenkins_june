// This is for parameters example
pipeline {
    agent any
    parameters {
        // string, text, booleanParam, choice, password 
        string (name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'who should i say hello to?')
        string (name: 'BRANCH_NAME', defaultValue: 'main', description: 'which branch should i build?')
        booleanParam(
            name: 'TOGGLE',
            defaultValue: true,
            description: 'toggle this value'
        )
        choice(
            name: 'env',
            choices: ['dev', 'tst', 'stg', 'prd'],
            description: 'Select the env you want to deploy'
        )
    }
    stages {
        stage('Example') {
            steps {
                echo "Hello ${params.PERSON}"
                echo "branch: ${params.BRANCH_NAME}"
                echo "boolean parameter is: ${params.TOGGLE}"
                echo "Deploying to: ${params.ENV} environment"
            }
        }
    }
}
