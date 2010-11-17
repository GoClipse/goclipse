package com.googlecode.goclipse.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoLinker {

	void createExecutable(IProject prj, String target, String[] dependencies) {
		if (!GoBuilder.dependenciesExist(prj, dependencies)){
			SysUtils.warning("Missing dependency for '"+target+"' not compiling");
			return;
		}

		IFile res;
		if (dependencies.length > 0){
			res = prj.getFile(dependencies[0]);
		} else {
			res = prj.getFile(target);
		}
		final IPath prjLoc = prj.getLocation();

		// do linker
		String linkerPath = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.LINKER_PATH);
		ExternalCommand link = new ExternalCommand(linkerPath);
		link.setEnvironment(GoConstants.environment());
		link.setWorkingFolder(prjLoc.toOSString());
		StreamAsLines output = new StreamAsLines();
		link.setResultsFilter(output);
		List<String> args = new ArrayList<String>();

		link.setCommand(linkerPath);
		args.clear();
		args.add(GoConstants.COMPILER_OPTION_L);
		args.add(Environment.INSTANCE.getPkgOutputFolder(prj).toOSString());
		args.add(GoConstants.COMPILER_OPTION_O);
		args.add(target);
		for (String dependency : dependencies) {
			args.add(dependency);
		}
		String rez = link.execute(args);
		if (rez != null) {
			addMarker(res, rez);
		}
		for (String line : output.getLines()) {
			addMarker(res, line);
		}
		try {
			prj.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			addMarker(prj, e.getLocalizedMessage());
		}
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
}