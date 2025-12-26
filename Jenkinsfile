pipeline {
    agent any

    tools {
        jdk 'JDK21'          // Configure this name in Jenkins Global Tool Config
        maven 'Maven3'       // Configure this name in Jenkins Global Tool Config
    }

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies / Build') {
            steps {
                sh "mvn clean install"
            }
        }

        stage('Run Tests') {
            steps {
                // Playwright Java installs browsers automatically on first run
                // If using Linux agent w/o GUI, Playwright uses headless by default
                bat "mvn clean test"
            }
        }

//         stage('Archive Test Reports') {
//             steps {
//                 junit 'target/surefire-reports/*.xml'
//             }
//         }
    }

    post {
//         always {
//             archiveArtifacts artifacts: 'target/**/*.log', allowEmptyArchive: true
//         }
        success {
            echo 'Tests executed successfully'
        }
        failure {
            echo 'Build failed. Please review test reports.'
        }
    }
}
