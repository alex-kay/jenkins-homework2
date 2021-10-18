#!groovy

import hudson.model.*;
import jenkins.model.*;
import hudson.tools.*;
import com.cloudbees.hudson.plugins.folder.Folder
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper
import org.jenkinsci.plugins.workflow.libs.FolderLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import com.synopsys.arc.jenkins.plugins.ownership.jobs.JobOwnerHelper

println "--> setting up folders"

def instance = Jenkins.getInstance()

def folder1 = instance.createProject(Folder.class, "folder1")
def folder2 = folder1.createProject(Folder.class, "folder2")
def folder3 = instance.createProject(Folder.class, "folder3")
def pipelineLibrarySource = new GitSCMSource("pipeline-library", "https://github.com/alex-kay/jenkins-shared-lib.git", null, null, null, false)
LibraryConfiguration lc = new LibraryConfiguration("pipeline-library", new SCMSourceRetriever(pipelineLibrarySource))
lc.setImplicit(true)
lc.setDefaultVersion("master")
folder1.addProperty(new FolderLibraries([lc]))
folder1.setDescription("This folder has a shared library from https://github.com/alex-kay/jenkins-shared-lib")
FolderOwnershipHelper.setOwnership(folder1, new OwnershipDescription(true, "okurylo"))

WorkflowJob sampleproject = folder3.createProject(WorkflowJob.class, "Sample Workflow")
sampleproject.setDefinition(new CpsFlowDefinition("buildPlugin(platforms: ['master'], repo: 'https://github.com/alex-kay/jenkins-homework.git')", true))
JobOwnerHelper.setOwnership(sampleproject, new OwnershipDescription(true, "okurylo"))

instance.save()