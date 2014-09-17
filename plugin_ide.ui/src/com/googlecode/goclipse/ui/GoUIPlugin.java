package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoToolManager;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	private GoOperationsConsoleListener operationsListener;
	
	@Override
	protected void doCustomStart_finalStage() {
		operationsListener = new GoOperationsConsoleListener();
		GoToolManager.getDefault().addListener(operationsListener);
		
		super.doCustomStart_finalStage();
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
		GoToolManager.getDefault().removeListener(operationsListener);
	}
	
}