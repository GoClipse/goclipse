## Installation

### Requirements: 
You will need: 
 * **Eclipse 4.3** (Kepler) or later (http://www.eclipse.org/)
 * A **1.7 Java VM** or later (http://www.java.com/)
 * Go development tools and libraries installed on target machine (http://golang.org/doc/install.html) 
Currently, only the plan 9 style compilers are supported. GCCGO may be supported in the future

#### Instructions:
 1. Use your existing Eclipse, or download a new Eclipse package from http://www.eclipse.org/downloads/. 
  * For an Eclipse package without any other IDEs or extras (such a VCS tools), download the ["Platform Runtime Binary"](http://download.eclipse.org/eclipse/downloads/drops4/R-4.3.1-201309111000/#PlatformRuntime). 
 1. Start Eclipse, go to `Help -> Install New Software...`
 1. Click the `Add...` button to add a new update site, enter the http://goclipse.googlecode.com/svn/trunk/goclipse-update-site/ URL in the Location field, click OK.
 1. Select the recently added update site in the `Work with:` dropdown. Type `Goclipse` in the filter box. Now the Goclipse feature should appear below.
 1. Select the `Goclipse` feature, and complete the wizard. 
  * Dependencies such as CDT will automatically be added during installation.
 1. Restart Eclipse. After that take a look at the configuration section in the [User Guide](UserGuide.md#user-guide).
  

#### Updating:
If you followed the installation steps above, you can update DDT to the latest release by clicking
`Help -> Check for Updates...` on the Eclipse IDE.
