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
        failure {
            script {
                def emailTemplate = load 'emailTemplate.groovy'
                def emailBody = emailTemplate.getEmailBody(
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n")
                )
                emailext body: emailBody, 
                         mimeType: 'text/html',
                         to: "${EMAIL_TO}", 
                         subject: 'Build failed in Jenkins: ${env.PROJECT_NAME} - #${env.BUILD_NUMBER}'
            }
        }
        unstable {
            script {
                def emailTemplate = load 'emailTemplate.groovy'
                def emailBody = emailTemplate.getEmailBody(
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n")
                )
                emailext body: emailBody, 
                         mimeType: 'text/html',
                         to: "${EMAIL_TO}", 
                         subject: 'Unstable build in Jenkins: ${env.PROJECT_NAME} - #${env.BUILD_NUMBER}'
            }
        }
        changed {
            script {
                def emailTemplate = load 'emailTemplate.groovy'
                def emailBody = emailTemplate.getEmailBody(
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n")
                )
                emailext body: emailBody, 
                         mimeType: 'text/html',
                         to: "${EMAIL_TO}", 
                         subject: 'Jenkins build is back to successful: ${env.PROJECT_NAME} - #${env.BUILD_NUMBER}'
            }
        }
        aborted {
            script {
                def emailTemplate = load 'emailTemplate.groovy'
                def emailBody = emailTemplate.getEmailBody(
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n")
                )
                emailext body: emailBody, 
                         mimeType: 'text/html',
                         to: "${EMAIL_TO}", 
                         subject: 'Pipeline aborted: ${env.PROJECT_NAME} - #${env.BUILD_NUMBER}'
            }
        }
    }
}
