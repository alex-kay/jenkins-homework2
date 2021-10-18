#!groovy

import jenkins.model.*
import org.jfrog.hudson.*
import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

println("--> Configure credentials");

env = System.getenv()

String credentialsId = env['CREDENTIALS']
String credentialsUsername = env['USERNAME']
String credentialsPassword = env['PASSWORD']

UsernamePasswordCredentialsImpl mainCredentials = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, 'Main Credentials', credentialsUsername, credentialsPassword)
SystemCredentialsProvider.getInstance().addCredentials(Domain.global(), mainCredentials)
