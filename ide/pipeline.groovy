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
                changed {
                    emailext attachLog: true, 
                        body: "PLease go to ${BUILD_URL} and verify the build", 
                        compressLog: true, 
                        to : "test@jenkins",
                        recipientProviders: [upstreamDevelopers(), requestor()], 
                        subject: "Job \'${JOB_NAME}\' (${BUILD_NUMBER}) ${currentBuild.result}"
                }
            }
        }
    }
}
