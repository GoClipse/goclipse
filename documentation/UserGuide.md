## User Guide

*Note:* For an overview of LANG_IDE_NAME features, see [Features](Features.md#ddt-features). This also serves to document what overall functionalities are available.


### Eclipse basics

If you are new to Eclipse, you can learn some of the basics of the Eclipse IDE with this short intro article: 
[An introduction to Eclipse for Visual Studio users
](http://www.ibm.com/developerworks/opensource/library/os-eclipse-visualstudio/)

Also, to improve Eclipse performance and startup time, it is recommended you tweak the JVM parameters. There is a tool called Eclipse Optimizer that can do that automatically, it is recommended you use it. Read more about it [here](http://www.infoq.com/news/2015/03/eclipse-optimizer). (Installing/enabling the JRebel optimization is not necessary as that only applies to Java developers)

### Configuration

A [LANG_NAME installation](https://LANG TODO) is required for most IDE functionality. Access Eclipse preferences from the menu `Window / Preferences`, navigate to the `LANG_NAME` preference page, and configure the LANG_NAME installation path under the appropriate field.

For functionality such as code completion, open definition, and editor outline, you will need:
 * The LANG TODO tool. 
 
The path to the executable of these two tools should be configured in the `LANG_NAME / Tools` preference page. The path can be an absolute path, or just the executable name, in which case, the executable will be searched in the PATH environment variable.


### Project setup

##### Project creation:
A new LANG_NAME project is created with the New Project Wizard: from the Project Explorer context menu, select `New / Project...` and then `LANG_NAME / LANG_NAME Project`. The same wizard is used to add a pre-existing project: simply use the location field to select a pre-existing location for your project.

##### Building:
A project has a set of Build Targets, each being a command invocation that builds the source code into one or more artifacts, and reports back possible compilation errors to the IDE. Build Targets can be configured directly from the Project Explorer. 

Build Targets can be enabled for a regular project build, or for auto-check. Auto-check is invoked when an editor is saved and no syntax errors are present in the source code. Normally it does not produce any artifacts, it just checks for compilation errors. **Note that auto-check is a different setting than the Eclipse workspace "Project / Build Automatically" option**. LANG_IDE_NAME ignores the later option by default. Auto-check is also not invoked if a file is saved automatically due to a regular build being requested. 

From the context menu of a Build Target, you can also directly create a Run or Debug launch configuration for one the generated executables. 

### Editor and Navigation

##### Editor newline auto-indentation:
The editor will auto-indent new lines after an Enter is pressed. Pressing Backspace with the cursor after the indent characters in the start of the line will delete the indent and preceding newline, thus joining the rest of the line with the previous line. Pressing Delete before a newline will have an identical effect.
This is unlike most source editors - if instead you want to just remove one level of indent (or delete the preceding Tab), press Shift-Tab. 

##### Content Assist / Open Definition:
Content Assist (also know as Code Completion, Auto Complete) is invoked with `Ctrl-Space`. 

The Open Definition functionality is invoked by pressing F3 in the source editor. 
Open Definition is also available in the editor context menu and by means of editor *hyper-linking* 
(hold Ctrl and click on a reference with the mouse cursor). 

### Launch and Debug:
To run a LANG_NAME project that builds to an executable, you will need to create a launch configuration. Locate the main menu, open 'Run' / 'Run Configurations...'. Then double click 'LANG_NAME Application" to create a new LANG_NAME launch, and configure it accordingly. You can run these launches from the 'Run Configurations...', or for quicker access, from the Launch button in the Eclipse toolbar.

Alternatively, to automatically create and run a launch configuration (if a matching one doesn't exist already), you can select a LANG_NAME project in the workspace explorer, open the context menu, and do 'Run As...' / 'LANG_NAME Application'. (or 'Debug As...' for debugging instead). If a matching configuration exists already, that one will be run.

Whenever a launch is requested, a build will be performed beforehand. This behavior can be configured under general Eclipse settings, or in the launch configuration.

##### Debugging

| **Windows note:** _Using Cygwin GDB doesn't work very well, if at all. The recommended way to debug in Windows is to use the GDB of [mingw-w64](http://mingw-w64.org/), or the one of [TDM-GCC](http://tdm-gcc.tdragon.net/)._ |
|----|

| **OS X note:** _The GDB that is included with OS X doesn't work properly. You'll need to install the latest GDB from Homebrew. See [this article](http://ntraft.com/installing-gdb-on-os-x-mavericks/) for details._ |
|----|


You can debug a LANG_NAME program by running a launch in debug mode. You will need a GDB debugger. To configure debug options (in particular, the path to the debugger to use), open the launch under 'Run' / 'Debug Configurations...', and then navigate to the 'Debugger' tab in the desired launch configuration:

<div align="center">
<a><img src="screenshots/UserGuide_DebuggerLaunchConfiguration.png" /><a/> 
</div>

GDB debugger integration is achieved by using the CDT plugins. To configure global debugger options, go the 'C/C++'/'Debug'/'GDB' preference page.

**Note that for debugging to work**, the program must be compiled with debug symbols information, and those debug symbols must be on a format that GDB understands. Otherwise you will get GDB error messages such "(no debugging symbols found)" or "file format not recognized".
