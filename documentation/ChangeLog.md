## LANG_IDE_NAME release ChangeLog
[Latest features on top]

### (NextVersion)
 * Added support for Eclipse dark theme. Namely:  
   * Syntax/source coloring now have different settings for dark theme.
   * Fixed tools console colors, as well as content assist pop-up colors.   
 
 * Fixed: Unindent (Shift-Tab) broken, does nothing after empty lines in selection.
 
 * Fixed: when invoking toolchain programs, add tool directory to beginning of PATH, not end.
 * Fixed: In Linux, the "Build Target" group UI widget height is broken, too short.
 * Fixed: if build tool reports many error messages, the Eclipse project build will take too long to finish.
 * Added UserGuide note about using Homebrew GDB in OS X.
 * Fixed: "Run As"/"Debug As" launch shortcut incorrectly matching pre-existing launch configurations.
 * Fixed: project not being refreshed when Build Target build invoked directly from Project Explorer.
 * Fixed: "null" text inserted when cancel pressed in Variables dialog.
 * Fixed: Occasional AssertionFailure when creating new projects in nested locations (project would not show up in Explorer).
 * Fixed: Arguments field in launch configuration is not multi-line.
 * Added support for Build Configurations.
   * Available build targets are displayed in the Project Explorer. 
   You can configure which targets are enabled for a workspace build or not. Or run/debug a specific target.
   * Default build targets are: #TODO
   * Project Build Configuration property page update to configure build targets.
 
  ▶ Recommended/tested CDT version is now 8.7.0

 * Fixed: PATH of external tools bungled if original PATH not set (Windows) 

 * Added: Show error message dialog if starting Eclipse with a Java version below the minimum.
 * Fixed: `Tab policy: "Spaces Only"` preference ignored when pressing TAB.
 * Added Content Assist preference page, with auto-activation options.
 * Fixed: the preference pages are now searchable in the Preferences dialog search field, by means of relevant keywords.

 * Upgraded minimum Java version to Java 8
  ▶ Recommended/tested CDT version: 8.6.0
  
 * Doc - Installation guide: Added note for users in China.

 * Added: Toggle Comment action (Ctrl+/) .
 * Added editor Go To Matching Bracket action (shortcut: Ctrl+Shift+P)

 * Added New Project wizard.
 
  ▶ Recommended/tested CDT version: 8.5.0
  
 * Added: Source menu with shift rigth/left operations.

  