package com.googlecode.goclipse.debug.launch;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.builder.GoDependencyManager;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.gdb.GdbConnection;
import com.googlecode.goclipse.debug.model.GoDebugTarget;
import com.googlecode.goclipse.preferences.PreferenceConstants;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.util.Util;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author steel
 */
public class GoLaunchConfigurationDelegate implements ILaunchConfigurationDelegate2 {

  public static final String ID = "com.googlecode.goclipse.debug.launchConfig";

  @Override
  public boolean buildForLaunch(ILaunchConfiguration configuration, String mode,
      IProgressMonitor monitor) throws CoreException {
    String project = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, "");
    String mainfile = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, "");
    if (project.isEmpty() || mainfile.isEmpty()) {
      return false;
    }
    return true;
  }

  @Override
  public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode,
      IProgressMonitor monitor) throws CoreException {

    return true;
  }

  @Override
  public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
    Launch launch = new Launch(configuration, mode, null);
    return launch;
  }

  @Override
  public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode,
      IProgressMonitor monitor) throws CoreException {
    return true;
  }

  @Override
  public void launch(final ILaunchConfiguration configuration, final String mode,
      final ILaunch launch, IProgressMonitor monitor) throws CoreException {
    execute("debug".equals(mode), configuration, launch, monitor);
  }

  private IProject getProject(String projectName, MessageConsoleStream con) {
    if (projectName == null) {
      if (con != null) {
        con.println("could not find project " + projectName);
      }

      return null;
    }

    IProject prj = null;
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IResource res = root.findMember(projectName);

    try {
      if (res == null) {
        if (con != null) {
          con.println("could not find project " + projectName + " in workspace "
              + root.getLocation().toOSString());
        }
      } else {
        if (res instanceof IProject) {
          prj = (IProject) res;
          if (!prj.isOpen()) {
            if (con != null) {
              con.println("project " + prj.getName() + " is closed!");
            }
          } else if (prj.getNature(GoNature.NATURE_ID) == null) {
            if (con != null) {
              con.println("project " + prj.getName() + " is not a Go project");
            }
            prj = null;
          }
        }
      }
    } catch (CoreException ce) {
      GoDebugPlugin.logError(ce);
    }

    return prj;
  }

  private void execute(boolean isDebug, ILaunchConfiguration configuration, ILaunch launch,
      IProgressMonitor monitor) throws CoreException {
    String project = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, "");
    String mainfile = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, "");
    String prgArgs = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_ARGS, "");

    File workingDir = getWorkingDirectory(configuration);

    IProject prj = getProject(project, null);

    if (prj != null) {
      IPath src = Path.fromOSString(mainfile);

      if (Environment.INSTANCE.isCmdFile(src)) {
        IPath binRel = Environment.INSTANCE.getBinOutputFolder(prj);
        IPath exeBase = prj.getLocation().append(binRel);
        String cmdName = GoDependencyManager.getCmdName(src);
        IPath executablePath = GoDependencyManager.getExecutablePath(cmdName, prj);
        String executableName = executablePath.lastSegment();
        if (!Util.isWindows()) {
          executablePath = Path.fromOSString(".").append(executableName);
        } else {
          executablePath = Path.fromOSString(executableName);
        }
        String cmdLine = exeBase.append(executablePath).toOSString();
        List<String> args = new ArrayList<String>();

        if (isDebug) {
          String gdbPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GDB_PATH);

          if (gdbPath != null && gdbPath.length() > 0) {
            File gdbFile = new File(gdbPath);

            if (gdbFile.exists()) {
              // fsf-gdb --interpreter mi 6.out
              args.add(gdbPath);
              args.add("--interpreter");
              args.add("mi");
            } else {
              abort("Unable to start debug session; GDB not found at '" + gdbPath
                  + "'. Please adjust the GDB setting in the Go > Debugger preference page.");
            }
          } else {
            abort("Unable to start debug session; the path to GDB must be set in the Go > Debugger preference page.");
          }
        }

        args.add(cmdLine);
        List<String> argList = argsAsList(prgArgs);
        if (argList != null) {
          args.addAll(argList);
        }
        
        Process p = DebugPlugin.exec(args.toArray(new String[] {}), workingDir);
        
        if (isDebug) {
          // TODO: pay attention to the VERBOSE flag
          
          IProcess process = DebugPlugin.newProcess(launch, p, executableName);
          
          launch.addProcess(process);
          
          GdbConnection gdbConnection = new GdbConnection(process);

          GoDebugTarget debugTarget = new GoDebugTarget(executableName, process, gdbConnection);

          debugTarget.start(launch);
        } else {
          IProcess process = DebugPlugin.newProcess(launch, p, executableName);
          
          launch.addProcess(process);
        }
      } else {
        abort("Executable source files must be in the 'cmd' folder");
      }
    }
  }

  public MessageConsole findMessageConsole(String name) {
    ConsolePlugin plugin = ConsolePlugin.getDefault();
    IConsoleManager conMan = plugin.getConsoleManager();
    IConsole[] existing = conMan.getConsoles();
    for (int i = 0; i < existing.length; i++)
      if (name.equals(existing[i].getName()))
        return (MessageConsole) existing[i];
    // no console found, so create a new one
    MessageConsole myConsole = new MessageConsole(name, null);
    conMan.addConsoles(new IConsole[] {myConsole});
    return myConsole;
  }

  /**
   * builds a list of arguments to be sent to the command splits at space but not inside double
   * quotes
   * 
   * @param prgArgs
   * @return
   */
  private List<String> argsAsList(String prgArgs) {
    List<String> argList = new ArrayList<String>();

    if (prgArgs == null || prgArgs.trim().length() == 0) {
      return argList;
    }

    boolean inQuote = false;
    boolean escape = false;
    StringBuffer arg = new StringBuffer();
    int i = 0;
    while (i < prgArgs.length()) {
      char c = prgArgs.charAt(i);
      i++;
      if (escape) {
        arg.append(c);
        escape = false;
        continue;
      }
      if (c == '\\') {
        escape = true;
        continue;
      }
      if (inQuote) {
        arg.append(c);
        if (c == '"') {
          inQuote = false;
        }
      } else {
        if (c == '"') {
          inQuote = true;
          arg.append(c);
        } else {
          if (Character.isSpaceChar(c)) {
            if (arg.length() > 0) {
              argList.add(arg.toString());
              arg.delete(0, arg.length());
            }
          } else {
            arg.append(c);
          }
        }
      }
    }
    argList.add(arg.toString());
    return argList.size() == 0 ? null : argList;
  }

  /**
   * Returns the working directory specified by the given launch configuration, or <code>null</code>
   * if none.
   * 
   * @param configuration launch configuration
   * @return the working directory specified by the given launch configuration, or <code>null</code>
   *         if none
   * @exception CoreException if unable to retrieve the attribute
   */
  public File getWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
    IPath path = getWorkingDirectoryPath(configuration);

    if (path == null) {
      File dir = getDefaultWorkingDirectory(configuration);

      if (dir != null) {
        if (!dir.isDirectory()) {
          abort("Working directory does not exist:" + dir.toString());
        }

        return dir;
      }
    } else {
      if (path.isAbsolute()) {
        File dir = new File(path.toOSString());

        if (dir.isDirectory()) {
          return dir;
        }

        // This may be a workspace relative path returned by a variable.
        // However variable paths start with a slash and thus are thought to
        // be absolute
        IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);

        if (res instanceof IContainer && res.exists()) {
          return res.getLocation().toFile();
        }

        abort("Working directory does not exist:" + path.toString());
      } else {
        IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);

        if (res instanceof IContainer && res.exists()) {
          return res.getLocation().toFile();
        }

        abort("Working directory does not exist:" + path.toString());
      }
    }

    return null;
  }

  /**
   * Returns the working directory path specified by the given launch configuration, or
   * <code>null</code> if none.
   * 
   * @param configuration launch configuration
   * @return the working directory path specified by the given launch configuration, or
   *         <code>null</code> if none
   * @exception CoreException if unable to retrieve the attribute
   */
  private IPath getWorkingDirectoryPath(ILaunchConfiguration configuration) throws CoreException {
    String path = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_WORKING_DIRECTORY,
        (String) null);

    if (path != null) {
      path = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(path);
      return new Path(path);
    }

    return null;
  }

  /**
   * Returns the default working directory for the given launch configuration, or <code>null</code>
   * if none. Subclasses may override as necessary.
   * 
   * @param configuration
   * @return default working directory or <code>null</code> if none
   * @throws CoreException if an exception occurs computing the default working directory
   */
  private File getDefaultWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
    // default working directory is the project if this config has a project
    IProject project = getProject(
        configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, (String) null), null);

    if (project != null) {
      return project.getLocation().toFile();
    }

    return null;
  }

  private void abort(String message) throws CoreException {
    throw new DebugException(new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID,
        DebugException.TARGET_REQUEST_FAILED, message, null));
  }

}
