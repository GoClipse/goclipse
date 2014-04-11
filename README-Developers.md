See project page ( https://code.google.com/p/goclipse/ ) for user information.

## Developers Guide

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

#### Deploying a new release:
 * TODO goclipse version info
 * Consider running the com.googlecode.goclipse.gocode/create_gocode.py script to rebuild gocode
for (windows, darwin, linux) and (386, amd64).
 * Run `mvn integration-test` as described above.
 * TODO update site upload
 * There is a CloudBees account with continous integrations jobs for Goclipse:
  * https://bruno-medeiros.ci.cloudbees.com/  


## Project design info and notes

#### About `src-lang/` and `melnorme.lang` code:
The `melnorme.lang` code, or simply Lang code, is IDE functionality not specific to any language, designed to potentially be used by other language IDEs. To achieve this some constraints need to be observed:
 * Lang code can only depend on other `melnorme` code, or on Eclipse.org plugins (including DLTK). But not on IDE specific code.  The only exception to this are the `_Actual` classes, which contain bindings to IDE-specific code (such as ids or other IDE constants, or even methods)
 * Lang code should be place on its own source folder (`src-lang/` usually). This is to make it easier to compare and update the code with the `src-lang/` of another IDE. If the Lang code is identical, only the `_Actual` classes should show up as differences.