package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoBuildManager;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	@Override
	protected Class<?> doCustomStart_getImagesClass() {
		return GoPluginImages.class;
	}
	
	private GoBuilderConsoleListener dubProcessListener;
	
	@Override
	protected void doCustomStart(BundleContext context) {
		dubProcessListener = new GoBuilderConsoleListener();
		GoBuildManager.getDefault().addBuildProcessListener(dubProcessListener);
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
		GoBuildManager.getDefault().removeBuildProcessListener(dubProcessListener);
	}
	
}