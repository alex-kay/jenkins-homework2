#!groovy

// i took this from https://gist.github.com/ivan-pinatti/4c546abbf98bb91d1af3c773adb195ce
// imports
import hudson.model.*;
import jenkins.model.*;
import hudson.tools.*;
import hudson.tasks.Mailer
import hudson.util.Secret;
import jenkins.plugins.slack.SlackNotifier.*
import org.jenkinsci.plugins.github.GitHubPlugin
import org.jenkinsci.plugins.github.config.GitHubServerConfig
import com.cloudbees.hudson.plugins.folder.Folder
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper


jenkins.model.Jenkins.instance.doQuietDown();

// parameters
def systemMessage = "Hello! This is my custom Jenkins message."

// get Jenkins instance
def instance = Jenkins.getInstance()

// set Jenkins system message
instance.setSystemMessage(systemMessage)

//get Jenkins configuration
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()

String adminAddress = "jenkins_admin@example.com"
// set admin address
jenkinsLocationConfiguration.setAdminAddress(adminAddress)
jenkinsLocationConfiguration.save()

// save current Jenkins state to disk
instance.save()

// SMTP
def env = System.getenv()

// Variables
def SystemAdminMailAddress = env['SMTP_SYSAD_EMAIL']
def SMTPUser = env['SMTP_USER']
def SMTPPassword = env['SMTP_PASSWORD']
def SMTPPort = env['SMTP_PORT']
def SMTPHost = env['SMTP_HOST']

// Constants
def mailServer = instance.getDescriptor("hudson.tasks.Mailer")
def extmailServer = instance.getDescriptor("hudson.plugins.emailext.ExtendedEmailPublisher")


//Jenkins Location
println "--> Configuring JenkinsLocation"
jenkinsLocationConfiguration.setAdminAddress(SystemAdminMailAddress)
jenkinsLocationConfiguration.save()

//E-mail Server
mailServer.setSmtpAuth(SMTPUser, SMTPPassword)
mailServer.setSmtpHost(SMTPHost)
mailServer.setSmtpPort(SMTPPort)
mailServer.setCharset("UTF-8")

//Extended-Email
extmailServer.smtpAuthUsername=SMTPUser
extmailServer.smtpAuthPassword=Secret.fromString(SMTPPassword)
extmailServer.smtpHost=SMTPHost
extmailServer.smtpPort=SMTPPort
extmailServer.charset="UTF-8"
extmailServer.defaultSubject="\$PROJECT_NAME - Build # \$BUILD_NUMBER - \$BUILD_STATUS!"
extmailServer.defaultBody="\$PROJECT_NAME - Build # \$BUILD_NUMBER - \$BUILD_STATUS:\n\nCheck console output at \$BUILD_URL to view the results."

// Save the state
    instance.save()


// Git 

def desc = instance.getDescriptor("hudson.plugins.git.GitSCM")

desc.setGlobalConfigName(env['GIT_NAME'])
desc.setGlobalConfigEmail(env['GIT_EMAIL'])

desc.save()

// Folder

def folder1 = Jenkins.instance.createProject(Folder.class, "folder1")
def folder2 = Jenkins.instance.createProject(Folder.class, "folder1/folder2")
def folder3 = Jenkins.instance.createProject(Folder.class, "folder3")
// FolderOwnershipHelper.setOwnership(demoFolder, new OwnershipDescription(true, "user"))

CASC_JENKINS_CONFIG="/jenkins/casc_configs"