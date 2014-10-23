## Reporting issues

 Notes:

 * Please report the GoClipse version you are using (can be found on `"Help/About/Installation Details/Installed Software"`), and operating system and architecture.
 * Check the Eclipse Error Log for errors or Java exceptions (see 
 `"Help/About/Installation Details/Configuration/Error Log"`). 
 If you see any error entries that you think might be relevant to the issue, post them.

 * Optional: you might be asked to post/attach the Configuration Log: This can be found at `"Help/About/Installation Details/Configuration"`

##### Debugger issues

Debbugger issues can be hard to track down, because of all the components involved: Goclipse, CDT, GDB, OS/format of binary, and Go compiler version.

If you think you've found a problem with the debugger integration, please try the following first:
 * Use latest version of Goclipse.
 * Use recommended/tested version of CDT (this is the CDT version GoClipse has most recently been tested against). This is listed in the changelog for each GoClipse release.
 * Use latest stable GDB, and the latest stable Go compiler.
 * Make sure you are compiling with inline optimizations turned off, ie, compile with `-gcflags "-N -l"`.

Most importantly: 
 * Try to reproduce the problem outside of Eclipse, using just the command-line GDB interface. If the problem occurs there, then it's an issue in either GDB or the Go compiler tools, not GoClipse.

If it does seem to be a GoClipse problem, open a new issue in Github, with the following info:
 * Configuration Log: `"Help/About/Installation Details/Configuration"`
 * Minimal source used to reproduce the issue (if possible).
 * The contents of `gdb` console in Eclipse and `gdb traces` for your launch. (if you're looking for a place to upload large files, you can use https://gist.github.com)
