See the [About](documentation/About.md) page for user information.

## Developers Guide

[![Build Status](https://bruno-medeiros.ci.cloudbees.com/job/Goclipse/badge/icon)](https://bruno-medeiros.ci.cloudbees.com/job/Goclipse/)

#### Setting up the development environment:
 * Clone the Git repository.
 * In Eclipse, use the import existing projects wizard, navigate to the Git repository, and add all the  Eclipse projects that are present in the root of the Git repo. Java Compiler settings should automatically be configured, since each project has project-specific settings stored in source control.
 * Setup the target platform: Open the target platform file: `releng/target-platform/Goclipse.target` and set it as your target platform.
 
#### Running the tests in Eclipse:
 * In `releng/launches` there is one or several Eclipse launch files for running the tests, so if this project is added to your Eclipse workspace, the launches will show up automatically in `Run Configurations...`, as "JUnit Plug-in Tests". 

#### Automated Building and Testing:
Using Maven (and Tycho), it is possible to automatically build Goclipse, create an update site, and run all the tests. Download [Maven](http://maven.apache.org/) (minimum version 3.0), and run the following commands on the root folder of the repository:
 * Run `mvn package` to build the IDE feature into a p2 repository (which is a local update site).  It will be placed at `bin-maven/features.repository/repository`
 * Run `mvn integration-test` to build the IDE as above and also run the test suites. 
 * Also, running `mvn package -P build-ide-product` will build a pre-packaged Eclipse installation with Goclipse already installed. This is not released to the public, but can potentially be of some use internally.
 * There is a CloudBees account with Jenkins continous integrations for Goclipse:

[![PoweredByCloudBees](http://www.cloudbees.com/sites/default/files/Button-Powered-by-CB.png)](https://bruno-medeiros.ci.cloudbees.com/job/Goclipse/)

#### Creating and deploying a new release:
A release is a web site with an Eclipse p2 update site. The website may contain no web pages at all, rather it can be just the p2 site. To create and deploy a new release:

 1. Ensure the version numbers of all plugins/features/etc. are properly updated, if they haven't been already.
 1. Run `mvn clean integration-test` to perform the Tycho build (see section above). Ensure all tests pass.
 1. Create and push a new release tag for the current release commit. 
 1. Go to the Github releases page and edit the newly present release. Add the corresponding ([ChangeLog.md](documentation/ChangeLog.md)) entries to the release notes. 
 1. Locally, run `ant -f releng/ CreateProjectSite`. This last step will prepare the project web site under `bin-maven/ProjectSite`.
 1. To actually publish the project site, run `ant -f releng/ PublishProjectSite -DprojectSiteGitURL=<some git URL>`. What happens here is that the whole project site will be pushed into a Git repository, to then be served in some way (for example Github Pages). If `projectSiteGitURL` is not specified, the default value in releng-build.properties will be used.
   * For more info on the Release Engineering script, run `ant -f releng/`, this will print the help.
 1. A branch or tag named `latest` should also be created in Github, pointing to the latest release commit. The previous `latest` tag can be deleted/overwritten. The documentation pages use this tag/branch in their links.

## Project design info and notes

#### About `src-lang/` and `melnorme.lang` code:
The `melnorme.lang` code, or simply Lang code, is IDE functionality not specific to any language, designed to potentially be used by other language IDEs. To achieve this some constraints need to be observed:
 * Lang code can only depend on other `melnorme` code, or on Eclipse.org plugins (including DLTK). But not on IDE specific code.  The only exception to this are the `_Actual` classes, which contain bindings to IDE-specific code (such as ids or other IDE constants, or even methods)
 * Lang code should be place on its own source folder (`src-lang/` usually). This is to make it easier to compare and update the code with the `src-lang/` of another IDE. If the Lang code is identical, only the `_Actual` classes should show up as differences.
