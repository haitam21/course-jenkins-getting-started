pipeline {
    agent any

    triggers { pollSCM('* * * * *') }

    stages {
        stage('checkout'){
            steps {
                git branch: 'main', url: 'https://github.com/haitam21/jgsu-spring-petclinic.git'
            }
        }
        stage('Build') {
            steps {
                
                sh "./mvnw clean package"
            }

            post {
                always {
                    junit '*target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}
