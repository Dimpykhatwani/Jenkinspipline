@Library('my-library') 
pipeline {
    agent any

    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '',  daysToKeepStr: '', numToKeepStr: '10' )
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build and Deploy for Test Branch') {
            when {
                branch 'test'
            }
            stages {
                stage('Image Build') {
                    steps {
                        sh 'docker build . -t registry.toshalinfotech.com/test/lub/test:0.$BUILD_NUMBER'
                    }
                }

                stage('Push to Registry') {
                    steps {
                        sh 'docker push registry.toshalinfotech.com/test/lub/test:0.$BUILD_NUMBER'
                    }
                }

                stage('Override Env Variable') {
                    steps {
                        sh 'echo test=registry.toshalinfotech.com/test/lub/test:0.$BUILD_NUMBER > .env'
                    }
                }

                stage('Docker Compose Up') {
                    steps {
                        sh 'docker compose --env-file .env up -d'
                    }
                }
            }
        }

        stage('Build and Deploy for Develop Branch') {
            when {
                branch 'develop'
            }
            stages {
                stage('Image Build') {
                    steps {
                        sh 'docker build . -t registry.toshalinfotech.com/test/lub/test:0.$BUILD_NUMBER'
                    }
                }

                stage('Push to Registry') {
                    steps {
                        sh 'docker push registry.toshalinfotech.com/test/lub/test:0.$BUILD_NUMBER'
                    }
                }

                stage('Override Env Variable') {
                    steps {
                        sh 'echo test=registry.toshalinfotech.com/test/lub/test:0.$BUILD_NUMBER > .env'
                    }
                }

                stage('Docker Compose Up') {
                    steps {
                        sh 'docker compose --env-file .env up -d'
                    }
                }
            }
        }
    }

    environment {
        EMAIL_TO = 'dimpy.khatwani@toshalinfotech.com'
    }

    post {
    always {
        script {
            def sendEmail(String subjectPrefix) {
                def emailTemplate = load 'emailTemplate.groovy'
                def changes = env.CHANGES ?: "No changes detected"
                def buildLog = currentBuild.rawBuild.getLog(50).join("\n")
                def emailBody = emailTemplate.getEmailBody(
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    changes, 
                    buildLog
                )
                emailext body: emailBody, 
                         mimeType: 'text/html',
                         to: "${EMAIL_TO}", 
                         subject: "${subjectPrefix} in Jenkins: ${env.PROJECT_NAME} - #${env.BUILD_NUMBER}"
            }

            if (currentBuild.result == 'FAILURE') {
                sendEmail('Build failed')
            } else if (currentBuild.result == 'UNSTABLE') {
                sendEmail('Unstable build')
            } else if (currentBuild.result == 'SUCCESS') {
                sendEmail('Build back to successful')
            } else if (currentBuild.result == 'ABORTED') {
                sendEmail('Pipeline aborted')
            }
        }
    }
}
}
