package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoToolManager;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	private GoBuilderConsoleListener toolchainProcessListener;
	
	@Override
	protected void doCustomStart_finalStage() {
		toolchainProcessListener = new GoBuilderConsoleListener();
		GoToolManager.getDefault().addBuildProcessListener(toolchainProcessListener);
		
		super.doCustomStart_finalStage();
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
		GoToolManager.getDefault().removeBuildProcessListener(toolchainProcessListener);
	}
	
}