## release ChangeLog
[Latest features on top]

### (NextVersion)
 * Added customization of the build command for Build Targets:
It's now possible to specify a command other than the default one (the $$SDK tool). 
   * Note however that the $$IDE still expects the output of the command (the error messages) to be in the same format as the default tool.
 * Added a setting to invoke a special build target when a $IDE editor is saved.
   * This allows invoking a build command (informally called a "check-build") that only checks for compiler errors, but doesn't not produce binaries. This has the potential to be faster than a full build.
   * Default is `` for $$IDE
 * Added support for modifying the environment variables of a Build Target's build command.  
  
 * Fixed "IllegalStateException: The service has been unregistered" on Mars.2 when Eclipse is closed.
 * Added signing to releases.
 * Fixed incorrect icon for errors and warnings in preference page status.
 * Fixed: can't save preference pages with empty fields.
 
 * Fixed bug with Content Assist snippets using the `${word_selection}` variable.
 * Dirty editors are now automatically saved if a build is invoked directly from a Build Target in the Project Explorer. (if the workspace "Save automatically before build" option is enabled).
 * Fixed workspace resource locking when a build is invoked directly from a Build Target in the Project Explorer.
 * Fixed regression: Console view always activates when a build is invoked.
 * Pressing F2 in the editor now shows information popup for problem under cursor (same as the mouse-over hover).
 * Improvement to Auto-Indent when Enter pressed before a closing brace. 
 * Minimum and recommended CDT version is now `8.8`.
 * When debugging, fixed toggling breakpoints on and off for files that are outside the workspace.
 * When debugging, fixed opening source files that are are outside the workspace.
 * Fixed line breakpoint icon.
 
 * Project builder is no longer invoked when workspace "Build Automatically" setting is enabled and a file is saved. (this was considered a misfeature anyways)
 * Fixed: project Build Targets settings pages shows wrong default for Program Path field
 
 * Syntax highlighting now works on the source compare editor/viewer.
 * Added per-project compiler/SDK installation preferences.
 * Newly created launch configurations now have the debug option "Stop on startup at:" set to false by default. This way debugging won't stop on the C `main`, which is essentially useless outside of C/C++.
 * Added Content Assist support of name-only proposal insertion by pressing `Ctrl+Enter`. (only applicable to proposals that insert arguments)
 * Fixed: AssertionFailureException pressing Enter after source with more closing braces than opening ones.
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

  