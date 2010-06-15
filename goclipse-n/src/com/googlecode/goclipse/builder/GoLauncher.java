package com.googlecode.goclipse.builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoLauncher {

	/**
	 * 
	 * @param project name
	 * @param mainfile path in project
	 * @return
	 */
	public static boolean createExecutable(String project, String mainfile) {
		boolean ok = true;
		try {
			MessageConsole console = GoConstants.findConsole("GO");
			MessageConsoleStream con = console.newMessageStream();
			con.println("Creating executable for: " + mainfile + " in project "
					+ project);

			IProject prj = getProject(project, con);
			if (prj != null) {
				
				String binRel = Environment.INSTANCE.getOutputFolder(prj);
				IPath exeBase = prj.getLocation().append(binRel).append(
						GoConstants.EXE_FILE_DIRECTORY);
				IPath objBase = prj.getLocation().append(binRel).append(
						GoConstants.OBJ_FILE_DIRECTORY);
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
					con
							.println("file "
									+ mainfile
									+ " was not properly built (there is no obj for it)");
					ok = false;
				}
				File exef = new File(exeFolder.toOSString());
				if (!exef.exists()) {
					exef.mkdirs();
				}
				if (ok) {
					// do linker
					String linkerPath = Activator.getDefault()
							.getPreferenceStore().getString(
									PreferenceConstants.LINKER_PATH);
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
	
	private static void addMarker(IResource res, String message) {
		if (res == null) {
			return;
		}
		try {
			IMarker marker = res.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker.setAttribute(IMarker.LINE_NUMBER, 0);
		}catch(CoreException ce) {
			SysUtils.debug(ce);
		}
	}

	private static IProject getProject(String project, MessageConsoleStream con) {
		IProject prj = null;
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource res = root.findMember(project);
		try {
			if (res == null) {
				con.println("could not find project " + project
						+ " in workspace " + root.getLocation().toOSString());
			} else {
				if (res instanceof IProject) {
					prj = (IProject) res;
					if (!prj.isOpen()) {
						con.println("project " + prj.getName() + " is closed!");

					} else if (prj.getNature(GoNature.NATURE_ID) == null) {
						con.println("project " + prj.getName()
								+ " is not a Go project");
						prj = null;
					}
				}
			}
		} catch (CoreException ce) {
			SysUtils.debug(ce);
		}
		return prj;
	}

	public static void execute(ILaunchConfiguration configuration) {
		String project;
		try {
			project = configuration.getAttribute("PROJECT_NAME", "");
			String mainfile = configuration.getAttribute("MAIN_FILE", "");
		    String prgArgs = configuration.getAttribute("PROGRAM_ARGS", "");
		    execute(project, mainfile, prgArgs);
		} catch (CoreException e) {
			SysUtils.debug(e);
		}
	    
	}
	public static void execute(String project, String mainfile, String prgArgs) {	
		MessageConsole console = GoConstants.findConsole("GO");
		MessageConsoleStream con = console.newMessageStream();
		try {
			prgArgs = prgArgs==null?"":prgArgs;
			con.println("launching " + mainfile + "\n----------------------------");
			IProject prj = getProject(project, con);
			if (prj != null) {
				String binRel = Environment.INSTANCE.getOutputFolder(prj);
				IPath exeBase = prj.getLocation().append(binRel).append(
						GoConstants.EXE_FILE_DIRECTORY);
				IPath src = Path.fromOSString(mainfile);
				IPath exeFolder = exeBase.append(src.removeFirstSegments(1)
						.removeLastSegments(1));
				String fName = src.removeFileExtension().segment(
						src.segmentCount() - 1);
				String goarch = Activator.getDefault().getPreferenceStore()
						.getString(PreferenceConstants.GOARCH);
				Arch arch = Arch.getArch(goarch);
				ExternalCommand run = new ExternalCommand(exeFolder.toOSString() + File.separator + fName + arch.getExecutableExt());
				run.setEnvironment(GoConstants.environment());
				run.setWorkingFolder(exeFolder.toOSString());
				ConsoleFilter outFilter = new ConsoleFilter(con);
				ConsoleFilter errFilter = new ConsoleFilter(con);
				errFilter.setPrefix("#>");
				run.setResultsFilter(outFilter);
				run.setErrorFilter(errFilter);
				List<String> args = new ArrayList<String>();
				args.add(prgArgs);
				String rez = run.execute(args);
				if (rez != null) {
					con.println("rez");
				}
				con.println("\n----------------------------\n" + "done " + mainfile);
				
			}
		}catch (Exception e) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(outStream));
			con.println(outStream.toString());
			try {
				outStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			con.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
