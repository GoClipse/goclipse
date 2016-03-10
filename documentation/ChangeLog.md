## release ChangeLog

### (NextVersion)

### 0.14.1
 * Added `godef` integration. `godef` is used as an alternative to oracle for the Open Definition operation. 
   * I wasn't able to use it extensively, but it seems to be more powerful than oracle (it can recover from some compiler errors for example), and faster. Indeed, most other Go IDEs/editors are using godef, so this bring Goclipse in line with that. 
 * Added "Format automatically on editor save." preference.
 * gofmt preferences moved to "Go/Tools" preference page.
 * Minor improvement to editor caret position preservation when invoking gofmt.
 * Added signing to releases.
 * Removed GOOS and GOARCH from preferences, they no longer have an apparent use.
 * Removed godoc preference. 
 * Fixed: Incorrect icon for errors and warnings in preference page status.
 * Fixed: Can't save preference pages with empty fields.
 * Fixed: Problem markers for the #build-tests target would not be created for Eclipse projects inside a GOPATH entry.
 
### 0.14.0
 * Added on-the-fly parser errors in the editor (uses Go `oracle`).
 * Added some new Snippets: `for`, `foreach`, `switch`, `main`, `apl` and a few others.
 * Added improved download functionality in gocode/oracle preference page. 
 * Pressing F2 in the editor now shows information popup for problem under cursor (same as the mouse-over hover).
 * Project builder is no longer invoked when workspace "Build Automatically" setting is enabled and a file is saved. (this was considered a misfeature anyways)
 * Dirty editors are now automatically saved if a build is invoked directly from a Build Target in the Project Explorer. (if the workspace "Save automatically before build" option is enabled).
 * Improved Auto-Indent when Enter pressed before a closing brace. 
 * Added preference to control whether project location is implicitly added to GOPATH or not.
 * Added gocode preference to enable/disable showing the error dialog if failures occur during Content Assist.
 * Minimum and recommended CDT version is now `8.8`.

--
 * Partially fixed outline not showing some method declarations. Now it works when editor is saved, but not if the editor is dirty. (#177)
 * Fixed NPE when invoking Open Definition in a file that is available in the Eclipse workspace under more than one path/location. (#167)
 * Fixed bug with Content Assist snippets using the `${word_selection}` variable.
 * Fixed workspace resource locking when a build is invoked directly from a Build Target in the Project Explorer.
 * Fixed regression: Console view always activates when a build is invoked. (#158)
 * When debugging, fixed toggling breakpoints on and off for files that are outside the workspace.
 * When debugging, fixed opening source files that are are outside the workspace.
 * Fixed line breakpoint icon.
 * Fixed: in New Go File Wizard, "Browse" button not working.
 * Fixed: "Restore Defaults" in "Go Compiler" settings page sometimes causes GOPATH field to be enabled when it shouldn't. 
 * Fixed: project Build Targets settings pages shows wrong default for Program Path field.
 
### 0.13.0
 * Added support for Eclipse dark theme (#131). Namely:  
   * Syntax/source coloring now have different settings for dark theme.
   * Fixed Tools Console colors, as well as content assist pop-up colors.
 * Added number literals syntax highlighting. (#138)
 * Syntax highlighting now works on the source compare editor/viewer.
 * Added per-project compiler installation preferences. (#152)
 * Newly created launch configurations now have the debug option "Stop on startup at:" set to false by default. This way debugging won't stop on the C `main`, which is essentially useless outside of C/C++.

--
 * Fixed: Quick outline breaks when file begins with `/* */`. (#160) 
 * Fixed: Preferences: custom setting for gofmt command is not saved. (#166)
 * Fixed: AssertionFailureException pressing Enter after source with more closing braces than opening ones.
 * Fixed: Unindent (Shift-Tab) broken, does nothing after empty lines in selection.
 * Fixed: Outline broken, due to error "CommonException: Invalid line, out of bounds". (#156) 

### 0.12.1
 * Go build now displays GOOS and GOARCH environment variables in build/tools console, if a non-default value is set.
 * Added UserGuide note about using Homebrew GDB in OS X.
 * Fixed: when invoking Go toolchain programs, add tool directory to beginning of PATH, not end.
 * Fixed: In Linux, the "Build Target" group UI widget height is broken, too short. (#150)
 * Fixed: if build tool reports many error messages, the Eclipse project build will take too long to finish.
 * Fixed: "Run As"/"Debug As" launch shortcut incorrectly matching pre-existing launch configurations.
 * Fixed: project not being refreshed when Build Target build invoked directly from Project Explorer.
 * Fixed: "null" text inserted when cancel pressed in "Variables..." and other dialogs.
 * Fixed: Launch Configuration Main tab fields incorrectly modified when a new Build Target is selected (if Build Target is not using defaults)
 

### 0.12.0
 * Added: support for Build Targets:
   * Build Targets are displayed in the Project Explorer.  Here you can also configure which targets are enabled for a workspace build.
   * Predefined Build Targets for Go are: `./... #build`, `./... #build-tests`, `./... #[run-tests]`. See the updated [Build section](documentation/UserGuide.md#build) in the UserGuide for more information.
   * Project Build Configuration property page updated to support configuring Build Targets.
   * Note that a `./...` Build Target is not launchable. To run a Go launch, you need to specify a specific Go package.
 * Added: Explicit GOPATH preference option to use the same value as the GOPATH environment variable.
 * No longer required to set an explicit value for the GOOS and GOARCH preference for running tools such as Go Oracle. You can just leave those fields at the default setting.
 * No longer showing error dialog when gocode fails. Now, only the editor status line is updated. 
 * Removed `go fix` menu operation.
 * Updated [Configuration](documentation/UserGuide.md#configuration) section in the User Guide.

 * Fixed: Occasional AssertionFailure when creating new projects in nested locations (the project would not show up in Explorer).
 * Fixed: Arguments field in launch configuration is not multi-line.

### 0.11.2
  ▶ Recommended/tested CDT version is now 8.7
 * Fixed: Debugging not working on Eclipse 4.5 Mars (CDT 8.7), for certain platform/GDB combinations.
 * Fixed: NPE launching a Go launch with a Go package that doesn't exists.
 * Doc: added note about Cygwin GDB not being recommended for debugging.

### 0.11.1
 * Fixed #135: Cannot set breakpoints in editor.
 * Fixed: PATH of external tools bungled if original PATH not set (Windows). 

### 0.11.0
Important changes:

 * Added: rewrote Outline page to use 'oracle describe', fixes some outline parsing errors.
  * Added: Quick-Outline (`Ctrl-O`).
 * Builder: The `install -v` options are no longer hardcoded either, but are now part of the Project build options. (Fixes #119)
   * Warning! This is a breaking change: Existing projects will need to add `install -v` to the Project build options (you can use the Restore Defaults button).
 * Added: Content Assist of code snippets. Configurable in `Preferences/Go/Editor/Code Snippets`. 
 * Added: Content Assist preference page, with auto-activation options.
 * Upgraded minimum Java version to Java 8
  * Added: Show error message dialog if starting Eclipse with a Java version below the minimum.
 * Doc - User Guide: Added note about Eclipse Optimizer

  ▶ Recommended/tested CDT version: 8.6.0

Other changes:

 * Minor changes to Content Assist icons (now has an overlay for private Go elements).
 * Doc - Installation guide: Added note for users in China.
 * Added #113: Append GOROOT to PATH when invoking Go oracle and other tools.
 * Fixed #121: Oracle Open Definition not working for type references: "selected name does not refer to a source element".
 * Fixed: #127 "null argument" error dialog when double-clicking on Go project.
 * Fixed #132: Calling oracle with incorrect position when source file has non-ASCII characters. * Fixed: the preference pages are now searchable in the Preferences dialog search field, by means of relevant keywords.
 * Fixed: `Tab policy: "Spaces Only"` preference ignored when pressing TAB.


### Goclipse 0.10.1
 * Fixed: Bug `java.lang.ClassNotFoundException: LANG_PROJECT_ID.ide.ui.LANGUAGE_UIPreferencesInitializer`.
 * Fixed #118: Errors and warnings not showing in Problems view.
 * Fixed #117: Incorrect highlight for single-quoted single quote.
 * Added: source highlighting option for characters (instead of being the same highlighting as strings).

### Goclipse 0.10.0
 * Implemented #100: Make the go build target parametizable. Now the `./...` target can be changed in the project's Build Options.
   * Warning! This is a breaking change: Existing projects will need to have `./...` added to the project build options.
 * Fixed #112: InvalidPathException invoking content assist. 
   * This also fixes potential performance problems invoking content assist.
   * However the documentation popup information will no longer be show for any content assist entries.
 * Added two new syntax highlighting options: Operators, and Syntax Control Characters (braces, parentheses, semicolon, etc.).
 * Added an improved New Project wizard.
 * Editor Ctrl-click (hyperlink) now only uses Go Oracle for the operation, the previous hyperlink has now been fully removed due to being buggy.
   * Improved: for Go Oracle integration, if the tool terminates with a non-zero exit code, instead of showing an error dialog, 
a workbench status line message is shown, a beep is sounded, and the Tools console is activated.   
 * Added Source submenu to editor context menu.
 * Added `Show In > Project Explorer` command to `Show In` menu in Go perspective (requires that the perspective be reset: `Window/Perspective/Reset Perspective...`) .
 * Clean up UI and behavior of main Go preference page.
 * Removed `gocode` bundle in Goclipse as it was very out of date (closes #88).
   * Added button/tool in Tools pref page to automatically download gocode from Github. 
 * Added button/tool in Tools pref page to automatically download Go oracle from Github. 
 * Added: Improved build error support: an error squigly line is now show in the column where the error occurred.
   * Unfortunately this does not seem to be currently supported by the Go compiler.
 * Added: builder will report a warning message if .go files are containted directly in a Go workspace `src` directory.
 * Fixed #105: Project Explorer does not expand GOROOT for Go 1.4.
 * Fixed some minor issues with Go To Matching Bracket action (shortcut: `Ctrl+Shift+P`).

Other:
 * Changed project site location to: http://goclipse.github.io/ (since Google Code is shutting down).


### Goclipse 0.9.1
 * Fixed: Builder does not ignore folders with names starting with "_" or ".".
   * Now using "./..." pattern to specify files to compile.
 * Fixed: Project wizard: only create "bin,pkg,src" folders if project is not already in the GOPATH.
 * Renamed `gocode log` console to `Oracle/gocode log`. Clarified UserGuide about the use of these tools.
 * Fixed: no error dialog is shown to the user if 'Run As'/'Debug As' context action results in a error.
 * Fixed: 'Run As'/'Debug As' now properly creates the Go package of the new launch configuration.
 * Fixed: The "Program" selection field, of the Go launch configuration tab, now asks for a Go main package, instead of Go source file.
 
▶ Recommended/tested CDT version: 8.4.0

### Goclipse 0.9.0
 * Added: A simplified project builder that delegates to the `go install` command all the build work. (fixes #64)
   * This should improve compilation speed, since Goclipse no longer tries to figure out package dependencies itself. (and is likely more robust)
   * Disabled continuous testing feature, as the feature became broken.
   * Goclipse now uses one global build console only, instead of one per project.
 * Added Ctrl-click open definition functionality using the [Go Oracle tool](http://golang.org/s/oracle-user-manual).
   * Path to the `oracle` command can be configured in the Go/Tools preference page.
   * Open Definition using Go Oracle can now also be invoked in an editor with the F3 key.
 * Added #84: Goclipse projects can now be created inside the "src" folder of a GOPATH entry, and invoking the toolchain (gocode, oracle, build, etc.) will be handled correctly for this scenario.
   * Note: with such projects, the project location will not be implicitly added to the GOPATH entries, as the project is part of the GOPATH already. 
 * Added: Project Explorer UI improvements:
   * GOROOT entry in now sorted at the top, and has own icon.
   * GOPATH entries have a location sub-label.
   * GOPATH entry no longer shown if that entry location is the same as the project location.
 * gocode handling improvements:
   * Added gocode process argument information to gocode log console.
   * gocode Code Completion now works with files outside of an Eclipse project.
   * Added display of an error dialog when gocode Content Assist fails.
   * Set timeout of 5 seconds for gocode process invocation during Content Assist. 
   * Fixed: Provide correct GOPATH 'lib-path' argument to gocode. 
 * Removed "RELEASE/DEBUG" config option from launch configuration, since it didn't actually do anything.
 * Fixed: bug in Go Project Wizard where using "Create project from existing source" would make the finish button not work.
 * Fixed: bug where some items in the Workbench "Source" menu would disappear when switching editors.
 
▶ Recommended/tested CDT version: 8.4.0
 
### Goclipse 0.8.1
 * Changed the Eclipse update site URL to: http://goclipse.github.io/releases/
 * Updated minimum required CDT version to 8.4. 
  * For more info on new CDT debug features, see: https://wiki.eclipse.org/CDT/User/NewIn84#Debug 
  * Added Dynamic printf action to Go editor ruler.
 * Changed Goclipse to be more lenient with CDT versions: it can now be installed with any CDT version except with major version changes. However each DDT release will still have a preferred CDT "major.minor" version, that has been tested against Goclipse. **Using a version other than the recommended one is not guaranteed to work correctly**.
  * Current recommended CDT version is 8.4.
 * Improved syntax highlighting preferences. Can now customize bold, italic, underline, etc. for each syntax element.
 * Added build console preference page. 
  * Can now customize build console text colors, and "Activate console on error messages" option.
 * Added #45: Allow specifying extra options to 'go build' (available in project properties). They default to `-gcflags "-N -l"`
 * Added: error message if `gocode` server could not be started.
 * Fixed #60: "go fmt" clearing entire file source if Go source was invalid.
 * Fixed #68: Ctrl-click hyperlink to open symbol defintion no longer working.
 * Fixed: Redundant build messages being displayed, even if no changes occurred in a project since last build.
 * Fixed: No build commands are printed in the build console when building non-main packages.
 

### Goclipse 0.8.0
 * Added debugging support using CDT's integration with GDB.
 * Goclipse now requires a JVM of version 7. 
 * Changed internal id for Go projects, so old Go projects are no longer recognized, they will need to be recreated.
 * Implemented #46: Eclipse console for Go builder.
 * Implemented #23: Auto indent when Enter pressed. Closing braces are automatically added as well.
   * Also, auto-deindent is performed when backspace pressed at line indent.
   * Added preference page to customize Auto-Indent behavior (under Editor/Typing)
 * Fixed issue with Project Explorer `Go Elements` content.
 * Fixed potential bug reading output of go test tool, for auto test feature.
 * Removed deprecated options in Go project build options property page.
 * Moved Continuous/Automatic Unit Testing tab to separate property page.
  * Fixed bug: Continuous Unit Testing property page saving its options without OK pressed.
  * Fixed bug: Continuous Unit Testing property page restore-defaults not working.