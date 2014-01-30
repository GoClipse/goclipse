package com.googlecode.goclipse.debug;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author devoncarew
 */
public class GoDebugPlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.googlecode.goclipse.debug";
	
	private static GoDebugPlugin plugin;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		plugin = this;
		
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static GoDebugPlugin getPlugin() {
		return plugin;
	}
	
}
