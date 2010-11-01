package com.googlecode.goclipse.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoPacker {

	void createArchive(IProject prj, 
			IProgressMonitor pmonitor, String target, String[] dependencies) {
		IFile res = prj.getFile(target);
		final IPath prjLoc = prj.getLocation();

		String packerPath = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.PACKER_PATH);
		ExternalCommand pack = new ExternalCommand(packerPath);
		pack.setEnvironment(GoConstants.environment());
		pack.setWorkingFolder(prjLoc.toOSString());
		StreamAsLines output = new StreamAsLines();
		pack.setResultsFilter(output);
		List<String> args = new ArrayList<String>();

		pack.setCommand(packerPath);
		args.clear();
		args.add(GoConstants.PACKER_OPTIONS_GRC);
		args.add(target);
		for (String dependency : dependencies) {
			args.add(dependency);
		}
		String rez = pack.execute(args);
		if (rez != null) {
			addMarker(res, rez);
		}
		for (String line : output.getLines()) {
			addMarker(res, line);
		}
		pmonitor.worked(50);
		try {
			prj.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			addMarker(prj, e.getLocalizedMessage());
		}
		pmonitor.done();
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