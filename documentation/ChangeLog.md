## release ChangeLog

### Goclipse 0.8.x
 * Fix "go fmt" clearing entire file source if Go source was invalid (#60)
 * Improved syntax highlighting preferences. Can now customize bold, italic, underline, etc. for each syntax element.
 * More lenient with CDT versions. GoClipse can now be installed with any CDT 8.x.x.
  * However, there is a small change using GoClipse with a version other than the recommned/tested one will break things. Each GoClipse release will list a recommented/tested CDT version.
 * Updated/tested GoClipse to CDT 4.4. 
  * Added Dynamic printf ruler action to source editor.
  * For more info on these new CDT 4.4 debug features, see: https://wiki.eclipse.org/CDT/User/NewIn84#Debug
 * Added build console preference page. 
  * Can now customize build console text colors.
  * Activate console on error message option.
 * Added #45 - Allow specifying extra options to 'go build' (available in project properties). They default to `-gcflags "-N -l"`
 * Fixed: Redundant build messages are no longer displayed, if no changes occurred in a project since last build.
 * Fixed #68: Ctrl-click hyperlink to open symbol defintion no longer working.


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