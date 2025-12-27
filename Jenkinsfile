pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
    }

    parameters {

        choice(
            name: 'BROWSER',
            choices: [
                'CHROME',
                'CHROMIUM',
                'EDGE',
                'FIREFOX',
                'WEBKIT'
            ],
            description: 'Select browser to run tests'
        )

        choice(
            name: 'HEADLESS',
            choices: [
                'true',
                'false'
            ],
            description: 'Enable or disable headless execution'
        )

        choice(
            name: 'TEST_CLASS',
            choices: [
                'All Tests',
                'CartTest',
                'CheckoutCompleteTest',
                'CheckoutStepOneTest',
                'CheckoutStepTwoTest',
                'FooterComponentTest',
                'HeaderComponentTest',
                'InventoryTest',
                'ItemDetailTest',
                'LoginTest'
            ],
            description: 'Select which test class to execute'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies / Build') {
            steps {
                bat "mvn clean install"
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def mvnCmd = "mvn clean test -Dbrowser=${params.BROWSER} -DisHeadless=${params.HEADLESS}"

                    if (params.TEST_CLASS != 'All Tests') {
                        mvnCmd = "${mvnCmd} -Dtest=${params.TEST_CLASS}"
                    }

                    bat mvnCmd
                }
            }
        }
    }

    post {
        success {
            echo 'Tests executed successfully'
        }
        failure {
            echo 'Build failed. Please review test reports.'
        }
    }
}