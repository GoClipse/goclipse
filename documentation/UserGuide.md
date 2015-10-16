## User Guide

*Note:* For an overview of LANG_IDE_NAME features, see [Features](Features.md#ddt-features). This also serves to document what overall functionalities are available.

### Configuration

A [LANGUAGE installation](https://LANG TODO) is required for most IDE functionality. Access Eclipse preferences from the menu `Window / Preferences`, navigate to the `LANGUAGE` preference page, and configure the LANGUAGE installation path under the appropriate field.

For functionality such as code completion, open definition, and editor outline, you will need:
 * The LANG TODO tool. 
 
The path to the executable of these two tools should be configured in the `LANGUAGE / Tools` preference page. The path can be an absolute path, or just the executable name, in which case, the executable will be searched in the PATH environment variable.

### Eclipse basics

If you are new to Eclipse, you can learn some of the basics of the Eclipse IDE with this short intro article: 
[An introduction to Eclipse for Visual Studio users
](http://www.ibm.com/developerworks/opensource/library/os-eclipse-visualstudio/)

Also, to improve Eclipse performance and startup time, it is recommended you tweak the JVM parameters. There is a tool called Eclipse Optimizer that can do that automatically, it is recommended you use it. Read more about it [here](http://www.infoq.com/news/2015/03/eclipse-optimizer). (Installing/enabling the JRebel optimization is not necessary as that only applies to Java developers)

### Project setup

##### Project creation:
A new LANGUAGE project is created with the New Project Wizard: from the Project Explorer context menu, select `New / Project...` and then `LANGUAGE / LANGUAGE Project`. The same wizard is used to add a pre-existing project: simply use the location field to select a pre-existing location for your project.


### Launch and Debug:
To run a LANGUAGE project that builds to an executable, you will need to create a launch configuration. Locate the main menu, open 'Run' / 'Run Configurations...'. Then double click 'LANGUAGE Application" to create a new LANGUAGE launch, and configure it accordingly. You can run these launches from the 'Run Configurations...', or for quicker access, from the Launch button in the Eclipse toolbar.

Alternatively, to automatically create and run a launch configuration (if a matching one doesn't exist already), you can select a LANGUAGE project in the workspace explorer, open the context menu, and do 'Run As...' / 'LANGUAGE Application'. (or 'Debug As...' for debugging instead). If a matching configuration exists already, that one will be run.

Whenever a launch is requested, a build will be performed beforehand. This behavior can be configured under general Eclipse settings, or in the launch configuration.

##### Debugging

| **Windows note:** _Using Cygwin GDB doesn't work very well, if at all. The recommended way to debug in Windows is to use the GDB of [mingw-w64](http://mingw-w64.org/), or the one of [TDM-GCC](http://tdm-gcc.tdragon.net/)._ |
|----|

| **OS X note:** _The GDB that is included with OS X doesn't work properly. You'll need to install the latest GDB from Homebrew. See [this article](http://ntraft.com/installing-gdb-on-os-x-mavericks/) for details._ |
|----|


You can debug a LANGUAGE program by running a launch in debug mode. You will need a GDB debugger. To configure debug options (in particular, the path to the debugger to use), open the launch under 'Run' / 'Debug Configurations...', and then navigate to the 'Debugger' tab in the desired launch configuration:

<div align="center">
<a><img src="screenshots/UserGuide_DebuggerLaunchConfiguration.png" /><a/> 
</div>

GDB debugger integration is achieved by using the CDT plugins. To configure global debugger options, go the 'C/C++'/'Debug'/'GDB' preference page.

**Note that for debugging to work**, the program must be compiled with debug symbols information, and those debug symbols must be on a format that GDB understands. Otherwise you will get GDB error messages such "(no debugging symbols found)" or "file format not recognized".
