package com.googlecode.goclipse.builder;

import static melnorme.lang.ide.core.LangCore_Actual.BUILD_PROBLEM_ID;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.Activator;

/**
 * A utility class for dealing with Go markers.
 * 
 * @author devoncarew
 */
@Deprecated
public class MarkerUtilities {
	
	public static void addMarker(IResource res, String message) {
		addMarker(res, -1, message, IMarker.SEVERITY_ERROR);
	}

	public static void addMarker(IResource file, int line, String message, int severity) {
		addMarker(file, line, message, severity, -1, -1);
	}

	private static void addMarker(final IResource file, int line,
			                      final String message, final int severity,
			                      final int beginChar, final int endChar) {

		if (file == null || !file.exists() || message == null) {
			return;
		}

		try {
			// never add a duplicate marker
			for (IMarker mark : file.findMarkers(BUILD_PROBLEM_ID, true, IResource.DEPTH_INFINITE)) {
				int line_number = (Integer) mark.getAttribute(IMarker.LINE_NUMBER);
				String msg = (String) mark.getAttribute(IMarker.MESSAGE);
				if (line == line_number && message.equals(msg)) {
					return;
				}
			}

		} catch (CoreException e1) {
			Activator.logError(e1);
		}

		try {
			IMarker marker = file.createMarker(BUILD_PROBLEM_ID);
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
				resource.deleteMarkers(BUILD_PROBLEM_ID, false, IResource.DEPTH_ZERO);
			}
		} catch (CoreException ce) {
			Activator.logError(ce);
		}
	}

}
