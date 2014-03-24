package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.osgi.framework.BundleContext;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	@Override
	protected Class<?> doCustomStart_getImagesClass() {
		return GoPluginImages.class;
	}
	
	@Override
	protected void doCustomStart(BundleContext context) {
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
	}
	
}