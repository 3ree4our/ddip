pipeline {
    agent any

    stages {
        stage('Environment Setup') {
            steps {
                script {
                    sh 'chmod +x ./gradlew'
                    def projectName = sh(script: './gradlew -q printProjectName', returnStdout: true).trim()
                    def projectVersion = sh(script: './gradlew -q printProjectVersion', returnStdout: true).trim()
                    env.PROJECT_NAME = projectName
                    env.PROJECT_VERSION = projectVersion
                    env.JAR_PATH = "${WORKSPACE}/build/libs/${env.PROJECT_NAME}-${env.PROJECT_VERSION}.jar"
                }
                echo 'Environment variables set'
            }
        }

        stage('Test') {
            steps {
                dir("${WORKSPACE}") {
                    sh './gradlew :clean :test'
                }
                echo 'Tests complete'
            }
        }

        stage('Build') {
            steps {
                sh "chmod u+x ${WORKSPACE}/gradlew"
                dir("${WORKSPACE}") {
                    sh './gradlew :clean :build -x test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def dockerImageTag = "${env.PROJECT_NAME}:${env.PROJECT_VERSION}"
                    env.DOCKER_IMAGE_TAG = dockerImageTag
                    sh "docker system prune -a -f"
                    sh "docker rmi $DOCKER_HUB_USER_NAME/${dockerImageTag} || true"
                    sh "docker build --no-cache=true --build-arg JAR_FILE=${env.JAR_PATH} -t ${dockerImageTag} -f ${WORKSPACE}/Dockerfile ${WORKSPACE}/build/libs/"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'DOCKER_HUB_ACCESS_TOKEN', variable: 'DOCKER_HUB_ACCESS_TOKEN')]) {
                        sh '''
                        echo $DOCKER_HUB_ACCESS_TOKEN | docker login -u $DOCKER_HUB_USER_NAME --password-stdin
                        '''
                    }
                    def dockerImageTag = env.DOCKER_IMAGE_TAG
                    sh "docker tag ${dockerImageTag} $DOCKER_HUB_USER_NAME/${dockerImageTag}"
                    sh "docker push $DOCKER_HUB_USER_NAME/${dockerImageTag}"
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    withCredentials([
                        sshUserPrivateKey(credentialsId: 'EC2_DEPLOY_KEY_FOR_DDIP', keyFileVariable: 'EC2_DEPLOY_KEY_FOR_DDIP'),
                        string(credentialsId: 'DB_ROOT_PASSWORD', variable: 'DB_ROOT_PASSWORD'),
                        string(credentialsId: 'DB_MASTER_USER_NAME', variable: 'DB_MASTER_USER_NAME'),
                        string(credentialsId: 'DB_MASTER_USER_PASSWORD', variable: 'DB_MASTER_USER_PASSWORD'),
                        string(credentialsId: 'DB_SLAVE_USER_NAME', variable: 'DB_SLAVE_USER_NAME'),
                        string(credentialsId: 'DB_SLAVE_USER_PASSWORD', variable: 'DB_SLAVE_USER_PASSWORD'),
                        string(credentialsId: 'REDIS_PASSWORD', variable: 'REDIS_PASSWORD'),
                        string(credentialsId: 'DDIP_ELASTICSEARCH_PASSWORD', variable: 'DDIP_FELASTICSEARCH_PASSWORD'),
                        string(credentialsId: 'JWT_SECRET_KEY', variable: 'JWT_SECRET_KEY'),
                        string(credentialsId: 'SSL_KEY_STORE_PASSWORD', variable: 'SSL_KEY_STORE_PASSWORD'),
                        string(credentialsId: 'NAVER_API_URL', variable: 'NAVER_API_URL'),
                        string(credentialsId: 'NAVER_API_KEY_ID', variable: 'NAVER_API_KEY_ID'),
                        string(credentialsId: 'NAVER_API_KEY_SECRET', variable: 'NAVER_API_KEY_SECRET'),
                        string(credentialsId: 'DDIP_S3_ACCESS_KEY', variable: 'DDIP_S3_ACCESS_KEY'),
                        string(credentialsId: 'DDIP_S3_SECRET_KEY', variable: 'DDIP_S3_SECRET_KEY'),
                        string(credentialsId: 'DDIP_S3_BUCKET_NAME', variable: 'DDIP_S3_BUCKET_NAME')
                    ]) {
                        sh '''
                        #!/bin/bash
                        scp -i "$EC2_DEPLOY_KEY_FOR_DDIP" ${WORKSPACE}/docker-compose.yml ubuntu@$EC2_IP_FOR_DDIP:$EC2_DEPLOY_PATH/
                        ssh -i "$EC2_DEPLOY_KEY_FOR_DDIP" ubuntu@"$EC2_IP_FOR_DDIP" << EOF
                            cd $EC2_DEPLOY_PATH
                            export EC2_IP_FOR_DDIP='${EC2_IP_FOR_DDIP}'
                            export PROJECT_NAME='${PROJECT_NAME}'
                            export PROJECT_VERSION='${PROJECT_VERSION}'
                            export DOCKER_HUB_USER_NAME='${DOCKER_HUB_USER_NAME}'
                            export DB_MASTER_USER_NAME='$DB_MASTER_USER_NAME'
                            export DB_MASTER_USER_PASSWORD='$DB_MASTER_USER_PASSWORD'
                            export DB_SLAVE_USER_NAME='$DB_SLAVE_USER_NAME'
                            export DB_SLAVE_USER_PASSWORD='$DB_SLAVE_USER_PASSWORD'
                            export DB_ROOT_PASSWORD='$DB_ROOT_PASSWORD'
                            export REDIS_PASSWORD='$REDIS_PASSWORD'
                            export DDIP_ELASTICSEARCH_PASSWORD='$DDIP_ELASTICSEARCH_PASSWORD'
                            export JWT_SECRET_KEY='$JWT_SECRET_KEY'
                            export SSL_KEY_STORE_PASSWORD='$SSL_KEY_STORE_PASSWORD'
                            export NAVER_API_URL='$NAVER_API_URL'
                            export NAVER_API_KEY_ID='$NAVER_API_KEY_ID'
                            export NAVER_API_KEY_SECRET='$NAVER_API_KEY_SECRET'
                            export DDIP_S3_ACCESS_KEY='$DDIP_S3_ACCESS_KEY'
                            export DDIP_S3_SECRET_KEY='$DDIP_S3_SECRET_KEY'
                            export DDIP_S3_BUCKET_NAME='$DDIP_S3_BUCKET_NAME'

                            docker stop \$PROJECT_NAME || true
                            docker rm \$PROJECT_NAME || true
                            docker-compose pull
                            docker-compose up -d --no-recreate
                            << EOF
                        '''
                    }
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: "build/libs/*.jar", fingerprint: true
                echo 'Artifacts archived'
            }
        }
    }
}
