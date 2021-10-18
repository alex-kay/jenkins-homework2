#!groovy

import hudson.model.*;
import jenkins.model.*;
import hudson.tools.*;
import hudson.tasks.Mailer;
import hudson.util.Secret;

def instance = Jenkins.getInstance()
env = System.getenv()

println "--> setting System Message"

def systemMessage = env['JENKINS_SYSTEM_MESSAGE']
instance.setSystemMessage(systemMessage)

println "--> setting SMTP and admin email address"

def SystemAdminMailAddress = env['JENKINS_SYSTEM_ADMIN_EMAIL']
def SMTPUser = env['JENKINS_SMTP_USER']
def SMTPPassword = env['JENKINS_SMTP_PASSWORD']
def SMTPPort = env['JENKINS_SMTP_PORT']
def SMTPHost = env['JENKINS_SMTP_HOST']

def mailServer = instance.getDescriptor("hudson.tasks.Mailer")
def extmailServer = instance.getDescriptor("hudson.plugins.emailext.ExtendedEmailPublisher")

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setAdminAddress(SystemAdminMailAddress)
jenkinsLocationConfiguration.save()

mailServer.setSmtpAuth(SMTPUser, SMTPPassword)
mailServer.setSmtpHost(SMTPHost)
mailServer.setSmtpPort(SMTPPort)
mailServer.setCharset("UTF-8")

extmailServer.smtpAuthUsername=SMTPUser
extmailServer.smtpAuthPassword=Secret.fromString(SMTPPassword)
extmailServer.smtpHost=SMTPHost
extmailServer.smtpPort=SMTPPort
extmailServer.charset="UTF-8"
extmailServer.defaultSubject="\$PROJECT_NAME - Build # \$BUILD_NUMBER - \$BUILD_STATUS!"
extmailServer.defaultBody="\$PROJECT_NAME - Build # \$BUILD_NUMBER - \$BUILD_STATUS:\n\nCheck console output at \$BUILD_URL to view the results."

instance.save()