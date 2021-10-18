#!/bin/bash

sudo apt update
sudo apt install wget git -y
sudo apt install openjdk-11-jdk -y
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt-get update
sudo apt-get install jenkins -y

JENKINS_PATH="/var/lib/jenkins/"
sudo mkdir -p $JENKINS_PATH

git clone https://github.com/alex-kay/jenkins-homework2.git
sudo cp -r ./jenkins-homework2/init.groovy.d/ $JENKINS_PATH

sudo rm -rf ./jenkins-homework2/
sudo chown jenkins:jenkins $JENKINS_PATH

sudo systemctl start jenkins