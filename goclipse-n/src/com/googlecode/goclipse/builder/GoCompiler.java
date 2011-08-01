package com.googlecode.goclipse.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.utils.ObjectUtils;

/**
 * 
 */
public class GoCompiler {
	private static final QualifiedName COMPILER_VERSION_QN = new QualifiedName(Activator.PLUGIN_ID, "compilerVersion");
	
	private Map<String, String> env; //environment for build

	private String version;
	private long versionLastUpdated = 0;
	
	/**
	 * Returns the current compiler version (ex. "6g version release.r58 8787").
	 * Returns null if we were unable to recover the compiler version. The returned
	 * string should be treated as an opaque token.
	 * 
	 * @return the current compiler version
	 */
	public static String getCompilerVersion() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String compilerPath = preferenceStore.getString(PreferenceConstants.COMPILER_PATH);
		
		if (compilerPath == null) {
			return null;
		}
		
		final ExternalCommand compilerCmd = new ExternalCommand(compilerPath);
	    StreamAsLines output = new StreamAsLines();
		compilerCmd.setResultsFilter(output);
		
		String result = compilerCmd.execute(Collections.singletonList("-V"));
		
		if (result != null) {
			return null;
		} else {
			for (String line : output.getLines()) {
				return line;
			}
		}
		
		return null;
	}
				
	public GoCompiler() {
		
	}
	
	public void compile(final IProject project,
			IProgressMonitor pmonitor, String target, String ... dependencies) {
		if (!dependenciesExist(project, dependencies)){
			Activator.logWarning("Missing dependency for '"+target+"' not compiling");
			return;
		}
		
		SubMonitor monitor = SubMonitor.convert(pmonitor, 130);
		Activator.logInfo("compile():" + dependencies);
					
		final IPath prjLoc = project.getLocation();
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String compilerPath = preferenceStore.getString(PreferenceConstants.COMPILER_PATH);
	    //String goarch = preferenceStore.getString(PreferenceConstants.GOARCH);
		IPath pkgPath = Environment.INSTANCE.getPkgOutputFolder(project);
		final ExternalCommand compilePackageCmd = new ExternalCommand(compilerPath);
		compilePackageCmd.setEnvironment(env);
		// get the architecture
	    //Arch arch = Arch.getArch(goarch);
	    StreamAsLines output = new StreamAsLines();
	    output.setCombineLines(true);
		compilePackageCmd.setResultsFilter(output);
		
		Activator.logInfo("building " + target);
		List<String> args = new ArrayList<String>();

		args.clear();
		//show all errors option
		args.add(GoConstants.COMPILER_OPTION_E);
		//include folder
		args.add(GoConstants.COMPILER_OPTION_I);
		args.add(prjLoc.append(pkgPath).toOSString());
		//output file option
		args.add(GoConstants.COMPILER_OPTION_O);
		//output file
		args.add(target);
		
		compilePackageCmd.setWorkingFolder(prjLoc.toOSString());
		IResource firstDependency = null;
		for (String dependency : dependencies) {
			if (dependency.endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION)){
				IResource resource = project.findMember(dependency);
				if (firstDependency == null){
					firstDependency = resource;
				}
				MarkerUtilities.deleteFileMarkers(resource);
				args.add(dependency);
			}
		}
		String result = compilePackageCmd.execute(args);
		if (result != null) {
			MarkerUtilities.addMarker(firstDependency, -1, result, IMarker.SEVERITY_ERROR);
		}
		processCompileOutput(output, project);
		
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			Activator.logInfo(e);
		}
	}
	
	private void processCompileOutput(StreamAsLines output,  IProject project) {
		for (String line: output.getLines()) {
			Activator.logInfo(line);
	         int goPos = line.indexOf(GoConstants.GO_SOURCE_FILE_EXTENSION);
	         if (goPos < 0){
	        	 Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, "Could not parse error message (missing .go): "+line));
	         } else {
	        	 int fileNameLength = goPos + GoConstants.GO_SOURCE_FILE_EXTENSION.length();
	        	 String fileName = line.substring(0, fileNameLength);
	        	 IResource resource = project.findMember(fileName);
	        	 line = line.substring(fileNameLength + 1);
	        	 String[] str = line.split(":", 2);
	        	 int location = -1; //marker for trouble
	        	 try {
	        		 location = Integer.parseInt(str[0]);
	        	 }catch(NumberFormatException nfe) {        		 
	        	 }
	             if (location != -1 && str.length > 1) {
		 			MarkerUtilities.addMarker(resource, location, str[1].trim(), IMarker.SEVERITY_ERROR);
	             } else {
	            	 //play safe. to show something in UI
	            	 MarkerUtilities.addMarker(resource, 0, line, IMarker.SEVERITY_ERROR);
		         }
			}
		}
	}
	         
	public void setEnvironment(Map<String, String> goEnv) {
		this.env = goEnv;
	}
	
	public String getVersion() {
		// The getCompilerVersion() call takes about ~30 msec to compute. Because it's expensive,
		// we cache the value for a short time.
		final long TEN_SECONDS = 10 * 1000;
		
		if ((System.currentTimeMillis() - versionLastUpdated) > TEN_SECONDS) {
			version = getCompilerVersion();
			versionLastUpdated = System.currentTimeMillis();
		}
		
		return version;
	}
	
	private boolean dependenciesExist(IProject project,
			String[] dependencies) {
		ArrayList<String> sourceDependencies = new ArrayList<String>();
		for (String dependency : dependencies) {
			if (dependency.endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION)) {
				sourceDependencies.add(dependency);
			}
		}
		return GoBuilder.dependenciesExist(project, sourceDependencies.toArray(new String[] {}));
	}

	public void updateVersion(IProject project) {
		try {
			project.setPersistentProperty(COMPILER_VERSION_QN, getVersion());
		} catch (CoreException ex) {
			Activator.logError(ex);
		}
	}

	public boolean requiresRebuild(IProject project) {
		String storedVersion;
		
		try {
			storedVersion = project.getPersistentProperty(COMPILER_VERSION_QN);
		} catch (CoreException ex) {
			storedVersion = null;
		}
		
		String currentVersion = getVersion();
		
		if (currentVersion == null) {
			// We were not able to get the latest compiler version - don't force a rebuild.
			return false;
		} else {
			return !ObjectUtils.objectEquals(storedVersion, currentVersion);
		}
	}
	
}
