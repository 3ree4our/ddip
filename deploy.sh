#!/bin/bash

# 1. env variable
echo "Project Name: $PROJECT_NAME"
echo "Project Version: $PROJECT_VERSION"
echo "1. Env variable setting complete"

# 2. cron delete
touch crontab_delete
crontab crontab_delete
rm crontab_delete
echo "2. Cron delete complete"

# 3. remove existing container if it exists
echo "Removing existing container if it exists..."
docker rm -f ${PROJECT_NAME}
echo "3. Existing container removal complete"

# 3.1 remove existing image if it exists
echo "Removing existing image if it exists..."
docker rmi $DOCKER_HUB_USER_NAME/${PROJECT_NAME}:${PROJECT_VERSION}
echo "3.1 Existing image removal complete"

# 4. pull the latest image from Docker Hub
echo "Pulling latest image..."
docker pull $DOCKER_HUB_USER_NAME/${PROJECT_NAME}:${PROJECT_VERSION}
echo "4. Image pull complete"

# 5. start Docker container
echo "Starting Docker container with image tag..."
docker run -d \
    --name $PROJECT_NAME \
    --network=docker-network \
    -p 443:443 \
    -v /home/ubuntu/keystore.p12:/app/keystore.p12 \
    -e PROJECT_NAME=$PROJECT_NAME \
    -e PROJECT_VERSION=$PROJECT_VERSION \
    -e SPRING_DATASOURCE_URL=jdbc:mariadb://ddip-db.c1smcu2wa2ue.us-east-2.rds.amazonaws.com:3306/$PROJECT_NAME \
    -e SPRING_DATASOURCE_USERNAME=$DB_USER_NAME \
    -e SPRING_DATASOURCE_PASSWORD=$DB_USER_PASSWORD \
    -e SPRING_REDIS_PASSWORD=$REDIS_PASSWORD \
    -e SSL_KEY_STORE_PASSWORD=$SSL_KEY_STORE_PASSWORD \
    -e JWT_SECRET_KEY=$JWT_SECRET_KEY \
    -e NAVER_API_URL=$NAVER_API_URL \
    -e NAVER_API_KEY_ID=$NAVER_API_KEY_ID \
    -e NAVER_API_KEY_SECRET=$NAVER_API_KEY_SECRET \
    $DOCKER_HUB_USER_NAME/${PROJECT_NAME}:${PROJECT_VERSION} > log.out 2> err.out
echo "5. Starting server complete"

# 6. cron registration
echo "Registering cron job..."
touch crontab_new
echo "* * * * * check-and-restart.sh" 1>>crontab_new
crontab crontab_new
rm crontab_new
echo "6. Cron registration complete"
