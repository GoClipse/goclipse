package com.googlecode.goclipse.core.tools;

import org.eclipse.core.runtime.IPath;

public interface IGocodePathProvider {
	
	IPath getBestGocodePath();
	
}