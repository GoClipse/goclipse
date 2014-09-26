package com.googlecode.goclipse.ui.editor;

import org.eclipse.core.runtime.IPath;

public interface IGocodePathProvider {
	
	IPath getBestGocodePath();
	
}