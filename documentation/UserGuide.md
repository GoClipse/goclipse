## User Guide

*Note:* For an overview of LANG_IDE_NAME features, see [Features](Features.md#ddt-features). This also serves to document 
the major functionalities available.

### Eclipse basics

If you are new to Eclipse, you can learn some of the basics of the Eclipse IDE with this short intro article: 

[An introduction to Eclipse for Visual Studio users
](http://www.ibm.com/developerworks/opensource/library/os-eclipse-visualstudio/)

Also, to improve Eclipse performance on modern machines, it is recommended you increase the memory available to 
the JVM. You can do so by modifying the _`eclipse.ini`_ file in your Eclipse installation. The two VM parameters 
in _`eclipse.ini`_ to note are _-Xms_ (initial Java heap size) and _-Xmx_ (maximum Java heap size). For a machine
with 4Gb of RAM or more, the following is recommended as minimum values:

```
-vmargs
-Xms256m
-Xmx1024m
```

### Project setup

##### Project creation:
A new LANGUAGE project can be created in the Project Explorer view. Open `New / Project...` and then `LANGUAGE / LANGUAGE Project`.


### Launch and Debug:
To run a LANGUAGE project that builds to an executable, you will need to create a launch configuration. Locate the main menu, open 'Run' / 'Run Configurations...'. Then double click 'LANGUAGE Application" to create a new LANGUAGE launch, and configure it accordingly. You can run these launches from the 'Run Configurations...', or for quicker access, from the Launch button in the Eclipse toolbar.

Alternatively, to automatically create and run a launch configuration (if a matching one doesn't exist already), you can select a LANGUAGE project in the workspace explorer, open the context menu, and do 'Run As...' / 'LANGUAGE Application'. (or 'Debug As...' for debugging instead). If a matching configuration exists already, that one will be run.

Whenever a launch is requested, a build will be performed beforehand. This behavior can be configured under general Eclipse settings, or in the launch configuration.

##### Debugging
You can debug a LANGUAGE program by running a launch in debug mode. You will need a GDB debugger. To configure debug options (in particular, the path to the debugger to use), open the launch under 'Run' / 'Debug Configurations...', and then navigate to the 'Debugger' tab in the desired launch configuration:

<div align="center">
<a><img src="screenshots/UserGuide_DebuggerLaunchConfiguration.png" /><a/> 
</div>

GDB debugger integration is achieved by using the CDT plugins. To configure global debugger options, go the 'C/C++'/'Debug'/'GDB' preference page.

**Note that for debugging to work**, the program must be compiled with debug symbols information, and those debug symbols must be on a format that GDB understands. Otherwise you will get GDB error messages such "(no debugging symbols found)" or "file format not recognized". See http://wiki.dlang.org/Debugging for more information on what debug symbols format each compiler produces.
