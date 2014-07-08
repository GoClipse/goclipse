## User Guide

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

### GoClipse Prerequisites and Configuration

Open Eclipse preferences, go to the Go preference page, and configure the GOROOT, GOPATH settings appropriately. You will need an installation of the Gocode tools.

### Project setup

##### Project creation:
A new Go project can be created in the Project Explorer view. Open `New / Project...` and then `Go / Go Project`. The Go perspective should open after creation, if it's not open already.

##### Project structure: 
A Go project has the structure of a Go workspace, and operates like one. Therefore it will contain the `bin`, `pkg`, and `src` directories. In the `src` folder you can create Go source files that will be compiled into a library package (and placed into `pkg`), or into an executable (and placed in `bin`). See http://golang.org/doc/code.html for more information on the organization of a Go workspace.

##### Build:
The `go` tool will be used to build the project. The output of this tool will be displayed in a console. Additionally, error markers resulting from the build will be collected and displayed in the the Go editor and Problems view.

Note that if the `Project / Build Automatically` option in the main menu is enabled (the default), a workspace build will be requested whenever any file is saved. Turn this on or off as desired.

### Editor and Navigation

##### Editor newline auto-indentation:
The editor will auto-indent new lines after an Enter is pressed. Pressing Backspace with the cursor after the indent characters in the start of the line will delete the indent and preceding newline, thus joining the rest of the line with the previous line. Pressing Delete before a newline will have an identical effect.
This is unlike most source editors - if instead you want to just remove one level of indent (or delete the preceding Tab), press Shift-Tab. 

##### Code-Completion/Auto-Complete:
Invoked with Ctrl-Space. This functionality is generally called Content Assist in Eclipse. 

Code completion is provided by means of the (gocode tool)[http://github.com/nsf/gocode]. GoClipse includes gocode already, but you can also use your own gocode instance, by configuring its location in the `Go / Gocode` preference page. This is useful to use newer versions of gocode, if available.

### Launch and Debug:
To run a Go project that builds to an executable, you will need to create a launch configuration. Locate the main menu, open 'Run' / 'Run Configurations...'. Then double click 'Go Application" to create a new launch, and configure it accordingly. You can run these launches from the 'Run Configurations...', or for quicker access, from the Launch button in the Eclipse toolbar.

Alternatively, to automatically create and run a launch configuration, you can select a Go project in the workspace explorer, open the context menu, and do 'Run As...' / 'Go Application'. (or 'Debug As...' for debugging instead). If a matching configuration exists already, that one will be run.

Whenever a launch is requested, a build will be performed beforehand. This behavior can be configured under general Eclipse settings, or in the launch configuration.

##### Debugging
You can debug a Go program by running a launch in debug mode. You will need a GDB debugger. To configure debug options (in particular, the path to the debugger to use), open the launch under 'Run' / 'Debug Configurations...', and then navigate to the 'Debugger' tab in the desired launch configuration:

<div align="center">
<a href="screenshots/UserGuide_DebuggerLaunchConfiguration.png?raw=true"><img src="screenshots/UserGuide_DebuggerLaunchConfiguration.png" /><a/> 
</div>

GDB debugger integration is achieved by using the CDT plugins. To configure global debugger options, go the 'C/C++'/'Debug'/'GDB' preference page.

**Note that for debugging to work**, the program must be compiled with debug symbols information, and those debug symbols must be on a format that GDB understands. Otherwise you will get GDB error messages such "(no debugging symbols found)" or "file format not recognized". 

TODO: info on Go optimizations that affect debugging
