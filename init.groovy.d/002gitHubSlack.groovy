#!groovy

import hudson.model.*;
import jenkins.model.*;
import hudson.tools.*;
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.github.GitHubPlugin
import org.jenkinsci.plugins.github.config.GitHubServerConfig
import jenkins.plugins.slack.SlackNotifier.*

def instance = Jenkins.getInstance()
env = System.getenv()

println "--> setting Git and Github"

def gitDesc = instance.getDescriptor("hudson.plugins.git.GitSCM")
gitDesc.setGlobalConfigName(env['GIT_NAME'])
gitDesc.setGlobalConfigEmail(env['GIT_EMAIL'])
gitDesc.save()

GitHubServerConfig server = new GitHubServerConfig('')
server.setName('GitHubAPI')
server.setApiUrl('https://api.github.com')
GitHubPlugin.configuration().getConfigs().add(server)

println "--> setting Slack"

def slackDesc = Jenkins.instance.getExtensionList('jenkins.plugins.slack.SlackNotifier\$DescriptorImpl')[0]
slackDesc.tokenCredentialId = env['SLACK_TOKEN']
slackDesc.teamDomain = env['SLACK_DOMAIN']
slackDesc.room = env['SLACK_ROOM']
slackDesc.save()

instance.save()
