package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.osgi.framework.BundleContext;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	public GoUIPlugin() {
	}
	
	protected static GoUIPlugin pluginInstance;
	
	/** Returns the plugin instance. */
	public static GoUIPlugin getInstance() {
		return pluginInstance;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		pluginInstance = this;
		super.start(context);
		startInitializeAfterLoadJob();
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		pluginInstance = null;
	}
	
}