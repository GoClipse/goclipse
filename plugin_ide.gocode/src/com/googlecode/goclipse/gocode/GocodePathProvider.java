package com.googlecode.goclipse.gocode;

import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.ui.editor.IGocodePathProvider;

public class GocodePathProvider implements IGocodePathProvider {
	
	public GocodePathProvider() {
	}
	
	@Override
	public IPath getBestGocodePath() {
		return GocodePlugin.getPlugin().getBestGocodeInstance();
	}
	
}