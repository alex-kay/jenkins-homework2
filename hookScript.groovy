#!groovy

// i took this from https://gist.github.com/ivan-pinatti/4c546abbf98bb91d1af3c773adb195ce
// imports
import hudson.model.*;
import jenkins.model.*;
import hudson.tools.*;
import hudson.tasks.Mailer
import hudson.util.Secret;
import jenkins.plugins.git.GitSCMSource
import jenkins.plugins.slack.SlackNotifier.*
import org.jenkinsci.plugins.github.GitHubPlugin
import org.jenkinsci.plugins.github.config.GitHubServerConfig
import com.cloudbees.hudson.plugins.folder.Folder
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper
import org.jenkinsci.plugins.workflow.libs.FolderLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import com.synopsys.arc.jenkins.plugins.ownership.jobs.JobOwnerHelper


// jenkins.model.Jenkins.instance.doQuietDown();

println "--> setting System Message"
// parameters
def systemMessage = "Hello! This is my custom Jenkins message."

// get Jenkins instance
def instance = Jenkins.getInstance()

// set Jenkins system message
instance.setSystemMessage(systemMessage)


// save current Jenkins state to disk
instance.save()

def env = System.getenv()

println "--> setting SMTP and admin email address"
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
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
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

println "--> setting Github"

GitHubServerConfig server = new GitHubServerConfig('')
server.setName('GitHubAPI')
server.setApiUrl('https://api.github.com')
GitHubPlugin.configuration().getConfigs().add(server)

// Git 

def desc = instance.getDescriptor("hudson.plugins.git.GitSCM")
desc.setGlobalConfigName(env['GIT_NAME'])
desc.setGlobalConfigEmail(env['GIT_EMAIL'])

desc.save()

// Folder

def folder1 = instance.createProject(Folder.class, "folder1")
def folder2 = folder1.createProject(Folder.class, "folder2")
def folder3 = instance.createProject(Folder.class, "folder3")
def pipelineLibrarySource = new GitSCMSource("pipeline-library", "https://github.com/alex-kay/jenkins-shared-lib.git", null, null, null, false)
LibraryConfiguration lc = new LibraryConfiguration("pipeline-library", new SCMSourceRetriever(pipelineLibrarySource))
lc.setImplicit(true)
lc.setDefaultVersion("master")
folder1.addProperty(new FolderLibraries([lc]))
folder1.setDescription("This folder has a shared library from https://github.com/alex-kay/jenkins-shared-lib")
FolderOwnershipHelper.setOwnership(folder, new OwnershipDescription(true, "okurylo"))

WorkflowJob sampleproject = folder1.createProject(WorkflowJob.class, "Ownership_Plugin_System_Master")
sampleproject.setDefinition(new CpsFlowDefinition("buildPlugin(platforms: ['master'], repo: 'https://github.com/jenkinsci/ownership-plugin.git')", true))
JobOwnerHelper.setOwnership(sampleproject, new OwnershipDescription(true, "okurylo"))

instance.save()

CASC_JENKINS_CONFIG="/jenkins/casc_configs"

