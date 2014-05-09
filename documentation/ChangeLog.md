## release ChangeLog

### Goclipse 0.8.0
 * other changes after 0.7.6 ???
 * Added debugging support using CDT's integration with GDB.
   * Some problems have been noted when examining variable contents.
 * Fixed issue with Project Explorer `Go Elements` content.
 * Goclipse now requires a JVM of version 7. 
 * Implemented #46: Eclipse console for Go builder.
 * Implemented #23: Auto indent when Enter pressed. Closing braces are automatically added as well.
   * Also, auto-deindent is performed when backspace pressed at line indent.
   * Added preference page to customize Auto-Indent behavior (under Editor/Typing)
 * Changed internal id for Go projects, so old Go projects are no longer recognized, they will need to be recreated.