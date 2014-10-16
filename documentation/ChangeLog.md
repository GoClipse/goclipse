## release ChangeLog

### Goclipse 0.8.x
 * Fixed: Project Explorer no longer shows GOPATH entry if that entry location is the as the project location.
 * Added gocode process argument information to gocode log console.
 * Added Ctrl-click open definition functionality using `go oracle`.
   * Path to `oracle` command can be configured in the Go preference page.
   * Go oracle Open Definition can now also be invoked under an editor with F3 key.
 * gocode Code Completion now works with files outside of an Eclipse project.
 * Added display of an error dialog when gocode Content Assist fails.
 * Added a timeout of 5 seconds for gocode process invocation during Content Assist. 
 * Removed "RELEASE/DEBUG" config option from launch configuration, since it didn't actually do anything.
 * Fixed bug where some items in the Workbench "Source" menu would disappear when switching editors.
 
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