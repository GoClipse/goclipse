/**
 * 
 */
package com.googlecode.goclipse.debug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.builder.Arch;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * @author steel
 */
public class LaunchConfigurationDelegate implements ILaunchConfigurationDelegate2{

   @Override
   public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
      String project = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, "");
      String mainfile = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, "");
      if (project.isEmpty()||mainfile.isEmpty()) {
    	  return false;
      }
      return createExecutable(project, mainfile);
   }

   @Override
   public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
         throws CoreException {
      
      return true;
   }

   @Override
   public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
      Launch launch = new Launch(configuration, mode, null);
      return launch;
   }

   @Override
   public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
      return true;
   }

   @Override
   public void launch(final ILaunchConfiguration configuration,final String mode, final ILaunch launch, IProgressMonitor monitor)
         throws CoreException {
	  execute(configuration, launch, monitor);	
   }
   
   /**
	 * 
	 * @param project
	 *            name
	 * @param mainfile
	 *            path in project
	 * @return
	 */
	public boolean createExecutable(String project, String mainfile) {
		boolean ok = true;
		try {
			MessageConsole console = findMessageConsole("Go Console");
			MessageConsoleStream con = console.newMessageStream();

			IProject prj = getProject(project, con);
			if (prj != null) {

				String binRel = Environment.INSTANCE.getOutputFolder(prj);
				IPath exeBase = prj.getLocation().append(binRel)
						.append(GoConstants.EXE_FILE_DIRECTORY);
				IPath objBase = prj.getLocation().append(binRel)
						.append(GoConstants.OBJ_FILE_DIRECTORY);
				IPath src = Path.fromOSString(mainfile);
				IResource res = prj.findMember(src);
				String fName = src.removeFileExtension().segment(
						src.segmentCount() - 1);
				String goarch = Activator.getDefault().getPreferenceStore()
						.getString(PreferenceConstants.GOARCH);
				Arch arch = Arch.getArch(goarch);

				IPath objFolder = objBase.append(src.removeFirstSegments(1)
						.removeFileExtension());
				IPath exeFolder = exeBase.append(src.removeFirstSegments(1)
						.removeLastSegments(1));

				String objFileName = objFolder.toOSString()
						+ arch.getExtension();
				String exeFileName = exeFolder.toOSString() + File.separator
						+ fName + arch.getExecutableExt();
				File objFile = new File(objFileName);
				if (!objFile.exists()) {
					con.println("file "
							+ mainfile
							+ " was not properly built (there is no obj for it)");
					ok = false;
				}
				if (ok) {
					File eFile = new File(exeFileName);
					if (eFile.exists()
							&& eFile.lastModified() >= objFile.lastModified()) {
						con.println("executable " + exeFileName
								+ " is up to date");
						ok = false; // exe is up to date
					}
				}

				if (ok) {
					con.println("Creating executable for: " + mainfile
							+ " in project " + project);

					File exef = new File(exeFolder.toOSString());
					if (!exef.exists()) {
						exef.mkdirs();
					}
					// do linker
					String linkerPath = Activator.getDefault()
							.getPreferenceStore()
							.getString(PreferenceConstants.LINKER_PATH);
					ExternalCommand compile = new ExternalCommand(linkerPath);
					compile.setEnvironment(GoConstants.environment());
					compile.setWorkingFolder(exeFolder.toOSString());
					StreamAsLines output = new StreamAsLines();
					compile.setResultsFilter(output);
					List<String> args = new ArrayList<String>();

					compile.setCommand(linkerPath);
					args.clear();
					args.add(GoConstants.COMPILER_OPTION_L);
					args.add(objBase.toOSString());
					args.add(GoConstants.COMPILER_OPTION_O);
					args.add(exeFileName);
					args.add(objFileName);
					con.println("linking ");
					con.println(linkerPath + " " + args);
					String rez = compile.execute(args);
					if (rez != null) {
						con.println("rez:" + rez);
						addMarker(res, rez);
					}
					for (String line : output.getLines()) {
						con.println(line);
						addMarker(res, line);
					}
					con.println("link finished");
					prj.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
			}
			try {
				con.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (CoreException ce) {
			ce.printStackTrace();
		}

		return ok;
	}

	private void addMarker(IResource res, String message) {
		if (res == null) {
			return;
		}
		try {
			IMarker marker = res.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker.setAttribute(IMarker.LINE_NUMBER, 0);
		} catch (CoreException ce) {
			SysUtils.debug(ce);
		}
	}

	private IProject getProject(String project, MessageConsoleStream con) {
		IProject prj = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource res = root.findMember(project);
		try {
			if (res == null) {
				if (con != null) {
					con.println("could not find project " + project
							+ " in workspace " + root.getLocation().toOSString());
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
							con.println("project " + prj.getName()
								+ " is not a Go project");
						}
						prj = null;
					}
				}
			}
		} catch (CoreException ce) {
			SysUtils.debug(ce);
		}
		return prj;
	}

	public void execute(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String project = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, "");
		String mainfile = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, "");
		String prgArgs = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_ARGS, "");
		IProject prj = getProject(project, null);
		if (prj != null) {
			String binRel = Environment.INSTANCE.getOutputFolder(prj);
			IPath exeBase = prj.getLocation().append(binRel)
					.append(GoConstants.EXE_FILE_DIRECTORY);
			IPath src = Path.fromOSString(mainfile);
			IPath exeFolder = exeBase.append(src.removeFirstSegments(1)
					.removeLastSegments(1));
			String fName = src.removeFileExtension().segment(
					src.segmentCount() - 1);
			String goarch = Activator.getDefault().getPreferenceStore()
					.getString(PreferenceConstants.GOARCH);
			Arch arch = Arch.getArch(goarch);
			String cmdLine = exeFolder.toOSString() + File.separator + fName
								+ arch.getExecutableExt();
			String workingDirectory = exeFolder.toOSString();
			List<String> args = new ArrayList<String>();
			args.add(cmdLine);
			List<String> argList = argsAsList(prgArgs);
			if (argList != null) {
				args.addAll(argList);
			}
			Process p = DebugPlugin.exec(args.toArray(new String[]{}), new File(workingDirectory));
			IProcess process= DebugPlugin.newProcess(launch, p, fName + arch.getExecutableExt());
			
			
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
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	/**
	 * builds a list of arguments to be sent to the command splits at space but
	 * not inside double quotes
	 * 
	 * @param prgArgs
	 * @return
	 */
	private List<String> argsAsList(String prgArgs) {
		System.out.println(prgArgs);
		List<String> argList = new ArrayList<String>();
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

}
