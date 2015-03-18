## release ChangeLog

### Goclipse 0.10.0
 * Added: `Show In > Project Explorer` command to `Show In` menu in Go perspective (requires that the perspective be reset: `Window/Perspective/Reset Perspective...`) .
 * Clean up UI and behavior of main Go preference page.
 * Added: two new syntax highlighting options: Operators, and Syntax Control Characters (braces, parentheses, semicolon, etc.).
 * Added: An improved New Project wizard.
// * Added: improved build error reporting: an error squigly line is now show in the column where the error occurred.
 * Added: Source menu to editor context menu: FIXME entries missing
 * Fixed #112: InvalidPathException invoking content assist. 
  * This also fixes potential performance problems invoking content assist.
  * However the documentation popup information will no longer be show for any content assist entries.
 * Removed `gocode` bundle in Goclipse as it was very out of date (closes #88).
   * Added button/tool in Tools pref page to automatically download gocode from Github. 
 * Fixed #105: Project Explorer does not expand GOROOT for Go 1.4  

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