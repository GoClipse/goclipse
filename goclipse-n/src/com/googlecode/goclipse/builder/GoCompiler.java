package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Util;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.parser.ImportParser;
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
	 * @param target
	 */
	public void goGetDependencies(final IProject project, IProgressMonitor monitor, java.io.File target) {
		
		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		final IPath projectLocation = project.getLocation();
		final String compilerPath   = preferenceStore.getString(PreferenceConstants.GO_TOOL_PATH);
		final IFile  file           = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));

		try {
			
			/**
			 * TODO Allow the user to set the go get locations
			 * manually.
			 */
			
			List<Import> imports    = getImports(target);
			List<String> cmd        = new ArrayList<String>();
			List<Import> extImports = new ArrayList<Import>();
			
			monitor.beginTask("Importing external libraries for "+file.getName()+":", 5);
			
			for (Import imp: imports) {
				if (imp.getName().startsWith("code.google.com") ||
					imp.getName().startsWith("github.com")      ||
					imp.getName().startsWith("bitbucket.org")   ||
					imp.getName().startsWith("launchpad.net")   ||
					imp.getName().contains(".git")              ||
					imp.getName().contains(".svn")              ||
					imp.getName().contains(".hg")               ||
					imp.getName().contains(".bzr")              ){
					
					cmd.add(imp.getName());
					extImports.add(imp);
				}
			}
			
			monitor.worked(1);
			
			//String[] cmd     = { compilerPath, GoConstants.GO_GET_COMMAND, "-u" };
			cmd.add(0, "-u");
			cmd.add(0, "-fix");
			cmd.add(0, GoConstants.GO_GET_COMMAND);
			cmd.add(0, compilerPath);
			
			String   goPath  = buildGoPath(project, projectLocation, true);
			String   PATH    = System.getenv("PATH");
			
	        ProcessBuilder builder = new ProcessBuilder(cmd).directory(target.getParentFile());
	        builder.environment().put(GoConstants.GOROOT, Environment.INSTANCE.getGoRoot(project));
	        builder.environment().put(GoConstants.GOPATH, goPath);
	        builder.environment().put("PATH", PATH);
	        Process p = builder.start();
      
			monitor.worked(3);
			
			try {
				p.waitFor();

			} catch (InterruptedException e) {
				Activator.logInfo(e);
			}

			InputStream   is  = p.getInputStream();
			InputStream   es  = p.getErrorStream();
			StreamAsLines sal = new StreamAsLines();
			sal.setCombineLines(true);
			sal.process(is);
			sal.process(es);
			
			Activator.logInfo(sal.getLinesAsString());
			boolean exMsg = true;
			
			try {
	            project.deleteMarkers(MarkerUtilities.MARKER_ID, false, IResource.DEPTH_ZERO);
            } catch (CoreException e1) {
	            Activator.logError(e1);
            }
			
			MarkerUtilities.deleteFileMarkers(file);
			if (sal.getLines().size() > 0) {
				
				for (String line : sal.getLines()) {
					if (line.startsWith("package")) {
						String impt = line.substring(0,line.indexOf(" -"));
						impt = impt.replaceFirst("package ", "");
						for (Import i:extImports) {
							if (i.path.equals(impt)) {
								MarkerUtilities.addMarker(file, i.getLine(), line.substring(line.indexOf(" -")+2), IMarker.SEVERITY_ERROR);
							}
						}
						
					} else if (line.contains(".go:")) {
						try {
							String[] split 	    = line.split(":");
							String   path 	    = "GOPATH/"+split[0].substring(split[0].indexOf("/src/")+5);
							IFile    extfile    = project.getFile(path);
							int      lineNumber = Integer.parseInt(split[1]);
							String   msg 		= split[3];
														
							if(extfile!=null && extfile.exists()){
								MarkerUtilities.addMarker(extfile, lineNumber, msg, IMarker.SEVERITY_ERROR);
								
							} else if (exMsg) {
								exMsg = false;
								MarkerUtilities.addMarker(file, "There are problems with the external imports in this file.\n" +
										                        "You may want to attempt to resolve them outside of eclipse.\n" +
										                        "Here is the GOPATH to use: \n\t"+goPath);
							}
							
						} catch (Exception e){
							Activator.logError(e);
						}
					}
				}
			}
			
			monitor.worked(1);
			
		} catch (IOException e1) {
			Activator.logInfo(e1);
		}
	}

	/**
	 * @param projectLocation
	 * @return
	 */
	public static String buildGoPath(IProject project, final IPath projectLocation, boolean extGoRootFavored) {
		
		String delim = ":";
		if (Util.isWindows()){
			delim = ";";
		}
		
		String       goPath = projectLocation.toOSString();
		String[]     path   = Environment.INSTANCE.getGoPath(project);
		final String GOPATH = path[0];

		if ( GOPATH != null && !"".equals(GOPATH) ) {
			if (extGoRootFavored) {
				goPath = GOPATH + delim + goPath;
			} else {
				goPath = goPath + delim + GOPATH;
			}
		}
		
		for(int i = 1; i < path.length; i++){
			goPath = goPath + delim + path[i];
		}

		return goPath;
	}

	/**
	 * @param project
	 * @param pmonitor
	 * @param fileList
	 */
	public void compileCmd(final IProject project, IProgressMonitor pmonitor, java.io.File target) {
		
		final IPath  projectLocation = project.getLocation();
		final IFile  file            = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));
		final String pkgPath         = target.getParentFile().getAbsolutePath().replace(projectLocation.toOSString(), "");
		final IPath  binFolder       = Environment.INSTANCE.getBinOutputFolder(project);
		
		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		final String           compilerPath    = preferenceStore.getString(PreferenceConstants.GO_TOOL_PATH);
		final String           outExtension    = (Util.isWindows() ? ".exe" : "");

		try {
			// the path exist to find the cc
			String   path    = System.getenv("PATH");
			String   outPath = null;
			String[] cmd     = {};
			
			MarkerUtilities.deleteFileMarkers(file);
			if(Environment.INSTANCE.isCmdSrcFolder(project, (IFolder)file.getParent())){
				outPath = projectLocation.toOSString() + File.separator + binFolder +  File.separator + target.getName().replace(GoConstants.GO_SOURCE_FILE_EXTENSION, outExtension);
				cmd     = new String[]{
						        compilerPath,
						        GoConstants.GO_BUILD_COMMAND,
						        GoConstants.COMPILER_OPTION_O,
						        outPath, file.getName() }
						  ;
				
			} else {
				MarkerUtilities.deleteFileMarkers(file.getParent());
				outPath = projectLocation.toOSString() + File.separator + binFolder +  File.separator + target.getParentFile().getName() + outExtension;
				cmd = new String[] {
			        compilerPath,
			        GoConstants.GO_BUILD_COMMAND,
			        GoConstants.COMPILER_OPTION_O,
			        outPath };
			}
			
 			String goPath = buildGoPath(project, projectLocation, false);

 			ProcessBuilder builder = new ProcessBuilder(cmd).directory(target.getParentFile());
			builder.environment().put(GoConstants.GOROOT, Environment.INSTANCE.getGoRoot(project));
			builder.environment().put(GoConstants.GOPATH, goPath);
			builder.environment().put("PATH", path);
			
			
			RunExternalProcessTask processTask = new RunExternalProcessTask(builder, project, pmonitor,
				GoBuildManager.getDefault().processListenersHelper);
			
			ExternalProcessNotifyingHelper processHelper = null;
			try {
				processHelper = processTask.startProcessAndAwait();
				
			} catch (CoreException ce) {
				if(ce.getCause() instanceof TimeoutException && pmonitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				LangCore.logStatus(ce.getStatus());
			}
			
			refreshProject(project, pmonitor);
			int errorCount = 0;
			
			StreamAsLines sal = new StreamAsLines();
			sal.setCombineLines(true);
			sal.process(new ByteArrayInputStream(processHelper.getStdOutBytes().toByteArray()));
			sal.process(new ByteArrayInputStream(processHelper.getStdErrBytes().toByteArray()));
			
			if (sal.getLines().size() > 0) {
		    	errorCount = processCompileOutput(sal, project, pkgPath, file);
		    }

		} catch (IOException e1) {
			Activator.logInfo(e1);
		}
	}
	
	/**
	 * @param project
	 * @param pmonitor
	 * @param fileList
	 */
	public void compileAll(final IProject project, IProgressMonitor pmonitor, IFolder target) {
		
		final IPath  projectLocation = project.getLocation();
		
		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		final String           compilerPath    = preferenceStore.getString(PreferenceConstants.GO_TOOL_PATH);

		try {
			// the path exist to find the cc
			String   path    = System.getenv("PATH");
			String[] cmd     = {};
			
			MarkerUtilities.deleteFileMarkers(target);
			cmd = new String[] {
		        compilerPath,
		        GoConstants.GO_BUILD_COMMAND
		    };
			
			String goPath = buildGoPath(project, projectLocation, false);

			File file = new File(target.getLocation().toOSString());
 			ProcessBuilder builder = new ProcessBuilder(cmd).directory(file);
			builder.environment().put(GoConstants.GOROOT, Environment.INSTANCE.getGoRoot(project));
			builder.environment().put(GoConstants.GOPATH, goPath);
			builder.environment().put("PATH", path);
			Process p = builder.start();

			try {
				p.waitFor();

			} catch (InterruptedException e) {
				Activator.logInfo(e);
			}
			
			refreshProject(project, pmonitor);
		    InputStream is = p.getInputStream();
		    InputStream es = p.getErrorStream();
		    StreamAsLines sal = new StreamAsLines();
		    sal.setCombineLines(true);
		    sal.process(is);
		    sal.process(es);
		    
		    String currentPackage = "";
		    for ( String line : sal.getLines()) {
		    	System.out.println(line);
		    	if(line.startsWith("#")) {
		    		currentPackage = line.replace("#", "").trim();
		    		continue;
		    	}
		    	String[] elements = line.split(":");
		    	String message = line.substring(elements[0].length()+elements[1].length()+2);
		    	path = target.getName()+File.separator+elements[0];
		    	IResource res = project.findMember(path);
		    	MarkerUtilities.addMarker(res, Integer.parseInt(elements[1]), message, IMarker.SEVERITY_ERROR);
		    }

		} catch (IOException e1) {
			Activator.logInfo(e1);
		}
	}
	
	
	/**
	 * @param project
	 * @param pmonitor
	 * @param fileList
	 */
	public void compilePkg(final IProject project, IProgressMonitor pmonitor, final String pkgpath, java.io.File target) {
		
		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		final String           compilerPath    = preferenceStore.getString(PreferenceConstants.GO_TOOL_PATH);
		
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
			String  goPath  = buildGoPath(project, projectLocation, false);
			
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
			StreamAsLines sal = StreamAsLines.buildStreamAsLines(project, file, pkgPath, p);
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
	 * @param fileList
	 */
	public void installAll(final IProject project, IProgressMonitor pmonitor) {
		
		final IPath  projectLocation = project.getLocation();
		//final IFile  file            = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));
		//final String pkgPath         = target.getParentFile().getAbsolutePath().replace(projectLocation.toOSString(), "");

		final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		final String           compilerPath    = preferenceStore.getString(PreferenceConstants.GO_TOOL_PATH);
		
		try {
			String[] cmd = { compilerPath, GoConstants.GO_INSTALL_COMMAND, "all" };

			String  goPath  = buildGoPath(project, projectLocation, false);
			
			// PATH so go can find cc
			String path = System.getenv("PATH");
			String goroot = Environment.INSTANCE.getGoRoot(project);
			ProcessBuilder builder = new ProcessBuilder(cmd).directory(project.getLocation().toFile());
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
			//clearPackageErrorMessages(project, pkgPath);
			StreamAsLines sal = StreamAsLines.buildStreamAsLines(project, null, null, p);
			int errorCount = 0;
		    if (sal.getLines().size() > 0) {
		    	errorCount = processCompileOutput(sal, project, null, null);
		    }

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
	 * 
	 * @param project
	 * @param dependencies
	 * @return
	 */
	private boolean dependenciesExist(IProject project, String[] dependencies) {
		ArrayList<String> sourceDependencies = new ArrayList<String>();
		for (String dependency : dependencies) {
			if (dependency.endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION)) {
				sourceDependencies.add(dependency);
			}
		}
		return GoBuilder.dependenciesExist(project, sourceDependencies.toArray(new String[] {}));
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
		boolean iswindows = Util.isWindows();
		
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
			
			int goPos = line.indexOf(GoConstants.GO_SOURCE_FILE_EXTENSION);

			if (goPos > 0) {
				
				int fileNameLength = goPos + GoConstants.GO_SOURCE_FILE_EXTENSION.length();

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
	
	/**
	 * Returns the current compiler version (ex. "6g version release.r58 8787").
	 * Returns null if we were unable to recover the compiler version. The
	 * returned string should be treated as an opaque token.
	 * 
	 * @return the current compiler version
	 */
	public static String getCompilerVersion() {
		
		String version = null;
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String compilerPath = preferenceStore.getString(PreferenceConstants.GO_TOOL_PATH);

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
	
	/**
	 * TODO this needs to be centralized into a common index...
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private List<Import> getImports(File file) throws IOException {
		Lexer 		  lexer        = new Lexer();
		Tokenizer 	  tokenizer    = new Tokenizer(lexer);
		ImportParser  importParser = new ImportParser(tokenizer, file);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = "";
		StringBuilder builder = new StringBuilder();
		while( (temp = reader.readLine()) != null ) {
			builder.append(temp);
			builder.append("\n");
		}
		
		reader.close();
		lexer.scan(builder.toString());
		List<Import> imports = importParser.getImports();
		
		return imports;
	}


}
