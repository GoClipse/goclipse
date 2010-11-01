package com.googlecode.goclipse.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoCompiler {
	private Map<String, String> env; //environment for build

	public GoCompiler() {
	}

					
	public void compile(final IProject project,
			IProgressMonitor pmonitor, String target, String ... dependencies) {
		SubMonitor monitor = SubMonitor.convert(pmonitor, 130);
		SysUtils.debug("compile():" + dependencies);
					
		final IPath prjLoc = project.getLocation();
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String compilerPath = preferenceStore.getString(PreferenceConstants.COMPILER_PATH);
	    String goarch = preferenceStore.getString(PreferenceConstants.GOARCH);
		IPath pkgPath = Environment.INSTANCE.getPkgOutputFolder(project);
		final ExternalCommand compilePackageCmd = new ExternalCommand(compilerPath);
		compilePackageCmd.setEnvironment(env);
		// get the architecture
	    Arch arch = Arch.getArch(goarch);
	    StreamAsLines output = new StreamAsLines();
		compilePackageCmd.setResultsFilter(output);
		
		SysUtils.debug("building " + target);
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
				deleteMarkers(resource);
				args.add(dependency);
			}
		}
		String result = compilePackageCmd.execute(args);
		if (result != null) {
			addMarker(firstDependency, -1, result, -1, -1, IMarker.SEVERITY_ERROR);
		}
		processCompileOutput(output, project);
		
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processCompileOutput(StreamAsLines output,  IProject project) {
		for (String line: output.getLines()) {
	         SysUtils.debug(line);
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
		 			addMarker(resource, location, str[1].trim(), -1, -1, IMarker.SEVERITY_ERROR);
	             } else {
	            	 //play safe. to show something in UI
			 		addMarker(resource, 0, str[1].trim(), -1, -1, IMarker.SEVERITY_ERROR);
		         }
			}         
	             }
	         }
	         
	private void addMarker(IResource file, int line, String message, int beginChar, int endChar, int severity) {
		if (file == null || !file.exists()) {
			return;
		}
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (line == -1) {
				line = 1;
			}
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker.setAttribute(IMarker.LINE_NUMBER, line);

			// find error type to mark location (experimental)
			if (beginChar >= 0) {
				marker.setAttribute(IMarker.CHAR_START, beginChar);
			}
			if (endChar >= 0){
				marker.setAttribute(IMarker.CHAR_END, endChar);
			}

		} catch (CoreException e) {
			SysUtils.debug(e);
		}
	}

	private void deleteMarkers(IResource file) {
		try {
			if (file != null && file.exists()) {
				file.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
			}
		} catch (CoreException ce) {
			SysUtils.debug(ce);
		}
	}

	public void setEnvironment(Map<String, String> goEnv) {
		this.env = goEnv;
		
	}
}
