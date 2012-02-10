package com.googlecode.goclipse.builder;

import com.googlecode.goclipse.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * A utility class for dealing with Go markers.
 * 
 * @author devoncarew
 */
public class MarkerUtilities {
	public static final String MARKER_ID = "goclipse.goProblem";

	public static void addMarker(IResource res, String message) {
    addMarker(res, -1, message, IMarker.SEVERITY_ERROR);
	}
	
	public static void addMarker(IResource file, int line, String message, int severity) {
		addMarker(file, line, message, severity, -1, -1);
	}
	
	private static void addMarker(IResource file, int line, String message, int severity, int beginChar,
			int endChar) {
		if (file == null || !file.exists()) {
			return;
		}
		
		try {
			IMarker marker = file.createMarker(MARKER_ID);
			
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
			if (endChar >= 0) {
				marker.setAttribute(IMarker.CHAR_END, endChar);
			}

		} catch (CoreException e) {
			Activator.logError(e);
		}
	}

	public static void deleteFileMarkers(IResource resource) {
		try {
			if (resource != null && resource.exists()) {
				resource.deleteMarkers(MARKER_ID, false, IResource.DEPTH_ZERO);
			}
		} catch (CoreException ce) {
			Activator.logInfo(ce);
		}
	}

	public static void deleteAllMarkers(IProject project) {
		try {
			if (project != null && project.exists()) {
				project.deleteMarkers(MARKER_ID, true, IResource.DEPTH_INFINITE);
			}
		} catch (CoreException ce) {
			Activator.logInfo(ce);
		}
	}
	
}
