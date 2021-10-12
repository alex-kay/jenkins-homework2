#!/bin/bash

sudo apt update
sudo apt install wget -y
sudo apt install openjdk-11-jdk -y
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt-get update
sudo apt-get install jenkins -y

INIT_PATH="/var/lib/jenkins/init.groovy.d"
sudo mkdir -p /var/lib/jenkins/init.groovy.d

cat > $INIT_PATH/hookScript.groovy <<- EOF
#!groovy

// i took this from https://gist.github.com/ivan-pinatti/4c546abbf98bb91d1af3c773adb195ce
// imports
import jenkins.model.Jenkins

// parameters
def systemMessage = "Insert your Jenkins system message here."

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// set Jenkins system message
jenkins.setSystemMessage(systemMessage)

// save current Jenkins state to disk
jenkins.save()
EOF

sudo chown jenkins:jenkins $INIT_PATH

sudo systemctl start jenkins