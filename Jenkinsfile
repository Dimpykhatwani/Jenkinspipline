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
                def emailUtils = load 'emailUtils.groovy'
                emailUtils.sendEmail(
                    'Build failed in Jenkins:', 
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n"), 
                    "${EMAIL_TO}"
                )
            }
        }
        unstable {
            script {
                def emailUtils = load 'emailUtils.groovy'
                emailUtils.sendEmail(
                    'Unstable build in Jenkins:', 
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n"), 
                    "${EMAIL_TO}"
                )
            }
        }
        changed {
            script {
                def emailUtils = load 'emailUtils.groovy'
                emailUtils.sendEmail(
                    'Jenkins build is back to successful:', 
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n"), 
                    "${EMAIL_TO}"
                )
            }
        }
        aborted {
            script {
                def emailUtils = load 'emailUtils.groovy'
                emailUtils.sendEmail(
                    'Pipeline aborted:', 
                    env.BUILD_URL, 
                    env.PROJECT_NAME, 
                    env.BUILD_NUMBER, 
                    env.CHANGES, 
                    currentBuild.rawBuild.getLog(100).join("\n"), 
                    "${EMAIL_TO}"
                )
            }
        }
    }
}
