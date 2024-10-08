#!/bin/bash

# check if docker is already installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Installing..."
    sudo apt-get update
    sudo apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

    sudo apt-get update
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io
else
    echo "Docker is already installed."
fi

# docker authorization
sudo usermod -aG docker ubuntu

# start docker service
sudo systemctl enable docker
sudo systemctl start docker

# pull MARIADB image if not exists
if [ ! "$(docker images -q mariadb:latest)" ]; then
    echo "Pulling MARIADB Docker image..."
    sudo docker pull mariadb:latest
fi

# create docker network if not exists
if [ ! "$(docker network ls | grep docker-network)" ]; then
    sudo docker network create docker-network
fi

# run MARIADB container if not exists
MARIADB_ROOT_PASSWORD=$1
DB_USER_NAME=$2
DB_USER_PASSWORD=$3

if [ ! "$(docker ps -a | grep mariadb-container)" ]; then
    echo "Starting MARIADB container..."
    sudo docker run -d --name mariadb-container --network=docker-network \
    -e MARIADB_ROOT_PASSWORD=$MARIADB_ROOT_PASSWORD \
    mariadb:latest

    sleep 30

    # create database
    sudo docker exec mariadb-container mysql -u root -p$MARIADB_ROOT_PASSWORD -e "CREATE DATABASE IF NOT EXISTS $PROJECT_NAME;"

    # create user and grant privileges
    sudo docker exec mariadb-container mysql -u root -p$MARIADB_ROOT_PASSWORD -e "CREATE USER IF NOT EXISTS '$DB_USER_NAME'@'%' IDENTIFIED BY '$DB_USER_PASSWORD'; GRANT ALL PRIVILEGES ON $PROJECT_NAME.* TO '$DB_USER_NAME'@'%'; FLUSH PRIVILEGES;"
fi

# pull Redis image if not exists
if [ ! "$(docker images -q redis:latest)" ]; then
    echo "Pulling Redis Docker image..."
    sudo docker pull redis:latest
fi

# run Redis container if not exists
REDIS_PASSWORD=$4

if [ ! "$(docker ps -a | grep redis-container)" ]; then
    echo "Starting Redis container..."
    sudo docker run -d --name redis-container --network=docker-network \
    -e REDIS_PASSWORD=$REDIS_PASSWORD \
    redis:latest
fi

echo "Docker setup complete."
