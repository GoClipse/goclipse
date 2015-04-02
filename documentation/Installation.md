## Installation

#### Requirements: 
 * Eclipse 4.4.1 (Luna SR1) or later.
 * A **1.7** Java VM or later. Otherwise LANG_IDE_NAME will silently fail to start.

#### Instructions:
 1. Use your existing Eclipse, or download a new Eclipse package from http://www.eclipse.org/downloads/. 
  * For an Eclipse package without any other IDEs or extras (such a VCS tools), download the ["Platform Runtime Binary"](http://archive.eclipse.org/eclipse/downloads/drops4/R-4.4.1-201409250400/#PlatformRuntime). 
 1. Start Eclipse, go to `Help -> Install New Software...`
 1. Click the `Add...` button to add a new update site, enter the URL: **LANG_IDE_UPDATE_SITE** in the Location field, click OK.
 1. Select the recently added update site in the `Work with:` dropdown. Type `LANG_IDE_NAME` in the filter box. Now the LANG_IDE_NAME feature should appear below.
 1. Select the `LANG_IDE_NAME` feature, and complete the wizard. 
  * LANG_IDE_NAME dependencies such as CDT will automatically be added during installation.
 1. Restart Eclipse. After that take a look at the setup section in the [User Guide](UserGuide.md#user-guide).
  

#### Updating:
If you already have LANG_IDE_NAME installed, and want to update it to a newer release, click `Help -> Check for Updates...`.

#### *Note for users in China*
Note: if you are behind the Great Firewall of China, you are very likely to encounter problems installing LANG_IDE_NAME: blocked connections, timeouts, or slow downloads. This is because the update site is hosted in Github, which is blocked or has limited access. These alternative steps might help you perform the installation:

* Download the website from LANG_IDE_WEBSITE_GIT_REPO__REMOVE"GIT"/archive/master.zip, unpack the archive and use the `releases` directory as a Local repository instead of the Update Site URL. However, you will need to redownload the archive above whenever you want to update LANG_IDE_NAME to a newer version.
* Download an Eclipse installation which already contains CDT (C Development Tools), so it doesn't have to be installed at the same time as LANG_IDE_NAME.