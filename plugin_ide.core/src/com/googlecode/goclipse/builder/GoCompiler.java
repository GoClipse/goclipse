package com.googlecode.goclipse.builder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.QualifiedName;
import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoToolManager.RunGoToolTask;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.utils.ObjectUtils;

/**
 * GoCompiler provides the GoClipse interface to the go build tool.
 */
public class GoCompiler {

	private static final QualifiedName	COMPILER_VERSION_QN	= new QualifiedName(Activator.PLUGIN_ID, "compilerVersion");
	private String version;
	private long versionLastUpdated	= 0;
		
	/**
	 * 
	 */
	public GoCompiler() {}

	/**
	 * @param project
	 * @param pmonitor
	 * @param fileList
	 */
	public void compileCmd(final IProject project, IProgressMonitor pmonitor, java.io.File target) {
		
		final IPath  projectLocation = project.getLocation();
		final IFile  file            = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));
		final String pkgPath         = target.getParentFile().getAbsolutePath().replace(projectLocation.toOSString(), "");
		final IPath  binFolder       = Environment.INSTANCE.getBinOutputFolder();
		
		final String compilerPath = GoCore.getPreferences().getString(PreferenceConstants.GO_TOOL_PATH);
		final String outExtension = (MiscUtil.OS_IS_WINDOWS ? ".exe" : "");

		// the path exist to find the cc
		String   path    = System.getenv("PATH");
		String   outPath = null;
		String[] cmd     = {};
		
		MarkerUtilities.deleteFileMarkers(file);
		if(Environment.INSTANCE.isCmdSrcFolder(project, (IFolder)file.getParent())){
			outPath = projectLocation.toOSString() + File.separator + binFolder +  File.separator + target.getName().replace(".go", outExtension);
			cmd     = new String[]{
					        compilerPath,
					        GoConstants.GO_BUILD_COMMAND,
					        GoConstants.COMPILER_OPTION_O,
					        outPath, file.getName() };
		} else {
			MarkerUtilities.deleteFileMarkers(file.getParent());
			outPath = projectLocation.toOSString() + File.separator + binFolder +  File.separator + target.getParentFile().getName() + outExtension;
			cmd = new String[] {
		        compilerPath,
		        GoConstants.GO_BUILD_COMMAND,
		        GoConstants.COMPILER_OPTION_O,
		        outPath };
		}
		
		String   goPath = projectLocation.toOSString();
		
		ProcessBuilder pb = new ProcessBuilder(cmd).directory(target.getParentFile());
		pb.environment().put(GoConstants.GOROOT, Environment.INSTANCE.getGoRoot(project));
		pb.environment().put(GoConstants.GOPATH, goPath);
		pb.environment().put("PATH", path);
		
		
		RunGoToolTask processTask = GoToolManager.getDefault().createRunProcessTask(pb, project, pmonitor);
		
		ExternalProcessResult processResult = null;
		try {
			processResult = processTask.startProcess().strictAwaitTermination();
		} catch (CoreException ce) {
			if(ce.getCause() instanceof TimeoutException && pmonitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			LangCore.logStatus(ce.getStatus());
			return;
		}
		
		refreshProject(project, pmonitor);
		
		StreamAsLines sal = new StreamAsLines();
		sal.setCombineLines(true);
		sal.process(new ByteArrayInputStream(processResult.getStdOutBytes().toByteArray()));
		sal.process(new ByteArrayInputStream(processResult.getStdErrBytes().toByteArray()));
		
		if (sal.getLines().size() > 0) {
	    	processCompileOutput(sal, project, pkgPath, file);
	    }
		
	}
	
	/**
	 * @param project
	 * @param pmonitor
	 * @param fileList
	 */
	public void compilePkg(final IProject project, IProgressMonitor pmonitor, final String pkgpath, java.io.File target) {
		
		final String           compilerPath    = GoCore.getPreferences().getString(PreferenceConstants.GO_TOOL_PATH);
		
		final IPath  projectLocation = project.getLocation();
		final IFile  file            = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));
		
		ProcessBuilder builder    = null;
		String         pkgPath    = null;
		File           workingDir = null;
		
		if (target.isFile()) {
			pkgPath = target.getParentFile().getAbsolutePath().replace(
					projectLocation.toOSString(), "");
			workingDir = target.getParentFile();
			
		} else {
			pkgPath = target.getAbsolutePath().replace(
					projectLocation.toOSString(), "");
			workingDir = target;
		}
		
		try {
			String[] buildCmd = { compilerPath,
			         GoConstants.GO_BUILD_COMMAND,
			         GoConstants.COMPILER_OPTION_O,
			         pkgpath,
			         "."
			       };
			
			builder = new ProcessBuilder(buildCmd).directory(workingDir);
			
			// PATH so go can find cc
			String path = System.getenv("PATH");
			String goroot = Environment.INSTANCE.getGoRoot(project);
			String goPath = projectLocation.toOSString();
			
			builder.environment().put(GoConstants.GOROOT, goroot);
			builder.environment().put(GoConstants.GOPATH, goPath);
		    builder.environment().put("PATH", path);
		    
		    Process p = builder.start();
		    try {
				p.waitFor();

			} catch (InterruptedException e) {
				Activator.logInfo(e);
			}
		    
		    refreshProject(project, pmonitor);
			clearPackageErrorMessages(project, pkgPath);
			int errorCount = 0;
			StreamAsLines sal = StreamAsLines.buildStreamAsLines(p);
			if (sal.getLines().size() > 0) {
		    	errorCount = processCompileOutput(sal, project, pkgPath, file);
		    }
			
			GoTestRunner.scheduleTest(project, compilerPath, file, pkgPath,
					workingDir, goPath, path, goroot, errorCount);

		} catch (IOException e1) {
			Activator.logInfo(e1);
		}
	}

	/**
     * @param project
     * @param pmonitor
     */
    private void refreshProject(final IProject project, IProgressMonitor pmonitor) {
	    try {
	        project.refreshLocal(IResource.DEPTH_INFINITE, pmonitor);
	    } catch (CoreException e) {
	    	Activator.logInfo(e);
	    }
    }

	/**
     * @param project
     * @param pkgPath
     */
    private void clearPackageErrorMessages(final IProject project, final String pkgPath) {
	    IFolder folder = project.getFolder(pkgPath);
	    try {
	        for (IResource res:folder.members()){
	        	if(res instanceof IFile){
	        		MarkerUtilities.deleteFileMarkers(res);
	        	}
	        }
	    } catch (CoreException e) {
	        Activator.logError(e);
	    }
    }

	/**
	 * 
	 * @return
	 */
	public String getVersion() {
		// The getCompilerVersion() call takes about ~30 msec to compute.
		// Because it's expensive,
		// we cache the value for a short time.
		final long TEN_SECONDS = 10 * 1000;

		if ((System.currentTimeMillis() - versionLastUpdated) > TEN_SECONDS) {
			version = getCompilerVersion();
			versionLastUpdated = System.currentTimeMillis();
		}

		return version;
	}

	/**
	 * @param project
	 */
	public void updateVersion(IProject project) {
		try {
			project.setPersistentProperty(COMPILER_VERSION_QN, getVersion());
		} catch (CoreException ex) {
			Activator.logError(ex);
		}
	}

	/**
	 * @param project
	 * @return
	 */
	public boolean requiresRebuild(IProject project) {
		String storedVersion;

		try {
			storedVersion = project.getPersistentProperty(COMPILER_VERSION_QN);
		} catch (CoreException ex) {
			storedVersion = null;
		}

		String currentVersion = getVersion();
		
		if (currentVersion == null) {
			// We were not able to get the latest compiler version - don't force
			// a rebuild.
			return false;
		} else {
			return !ObjectUtils.objectEquals(storedVersion, currentVersion);
		}
	}
	
	/**
     * 
     * @param output
     * @param project
     * @param relativeTargetDir
     */
	private int processCompileOutput(final StreamAsLines output,
			                          final IProject      project,
			                          final String        relativeTargetDir,
			                          final IFile         file) {
		int errorCount = 0;
		boolean iswindows = MiscUtil.OS_IS_WINDOWS;
		
		for (String line : output.getLines()) {
			
			if (line.startsWith("#")) {
				continue;
				
			} else if(line.startsWith("can't load package:")) {
				/*
				 * when building a main package mixed with a
				 * lib package this error occurs and is not
				 * specific to any one file.  It is related
				 * to the organization of the project.
				 */
				IContainer container = file.getParent();
				if(container instanceof IFolder){
					IFolder folder = (IFolder)container;
					MarkerUtilities.addMarker(folder, 0, line, IMarker.SEVERITY_ERROR);
					errorCount++;
				}
				continue;
			}
			
			int goPos = line.indexOf(".go");

			if (goPos > 0) {
				
				int fileNameLength = goPos + ".go".length();

				// Strip the prefix off the error message
				String fileName = line.substring(0, fileNameLength);
				fileName = fileName.replace(project.getLocation().toOSString(), "");
				fileName = iswindows?fileName:fileName.substring(fileName.indexOf(":") + 1).trim();

				// Determine the type of error message
				if (fileName.startsWith(File.separator)) {
					fileName = fileName.substring(1);
					
				} else if (fileName.startsWith("." + File.separator)) {
					fileName = relativeTargetDir.substring(1) + File.separator + fileName.substring(2);
					
				} else if (line.startsWith("can't")) {
					fileName = relativeTargetDir.substring(1);
					
				} else {
					fileName = relativeTargetDir.substring(1) + File.separator + fileName;
				}
				
				// find the resource if possible
				IResource resource = project.findMember(fileName);
				if (resource == null && file != null) {
					resource = file;
					
				} else if (resource == null) {
					resource = project;
				}

				// Create the error message
				String msg = line.substring(fileNameLength + 1);
				String[] str = msg.split(":", 3);
				int location = -1; // marker for trouble
				int messageStart = msg.indexOf(": ");

				try {
					location = Integer.parseInt(str[0]);
				} catch (NumberFormatException nfe) {}
				
				// Determine how to mark the message
				if (location != -1 && messageStart != -1) {
					String message = msg.substring(messageStart + 2);
					MarkerUtilities.addMarker(resource, location, message, IMarker.SEVERITY_ERROR);
					errorCount++;
					
				} else {
					// play safe. to show something in UI
					MarkerUtilities.addMarker(resource, 1, line, IMarker.SEVERITY_ERROR);
					errorCount++;
				}
				
			} else {
				// runtime.main: undefined: main.main
				MarkerUtilities.addMarker(file, 1, line, IMarker.SEVERITY_ERROR);
				errorCount++;
			}
		}
		
		return errorCount;
	}
	
	/*
	 * Returns the current compiler version (ex. "6g version release.r58 8787").
	 * Returns null if we were unable to recover the compiler version. The
	 * returned string should be treated as an opaque token.
	 * @return the current compiler version
	 */
	private String getCompilerVersion() {
		String version = null;
		String compilerPath = GoCore.getPreferences().getString(PreferenceConstants.GO_TOOL_PATH);

		if (compilerPath == null || compilerPath.length() == 0) {
			return null;
		}

		try {
			String[] cmd = { compilerPath, GoConstants.GO_VERSION_COMMAND };

			Runtime runtime = Runtime.getRuntime();
			Process p = runtime.exec(cmd);

			try {
				p.waitFor();

			} catch (InterruptedException e) {
				Activator.logInfo(e);
			}

			InputStream is = p.getInputStream();
			InputStream es = p.getErrorStream();
			StreamAsLines output = new StreamAsLines();
			StreamAsLines errors = new StreamAsLines();
			output.process(is);
			errors.process(es);

			final String GO_VERSION = "go version ";

			for (String line : output.getLines()) {
				if (line != null && line.startsWith(GO_VERSION)) {
					version = line.substring(GO_VERSION.length());
					break;
				}
			}

		} catch (IOException e) {
			Activator.logInfo(e);
		}

		return version;
	}

}
