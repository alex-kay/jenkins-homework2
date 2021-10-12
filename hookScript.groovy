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