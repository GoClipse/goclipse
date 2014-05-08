package com.googlecode.goclipse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Util;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoBuilder;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author steel
 */
// BM: TODO: remove UI dependencies, especially all the image stuff
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "goclipse";
	public static final String UI_PLUGIN_ID = "com.googlecode.goclipse.ui";

	public static final String CONTENT_ASSIST_EXTENSION_ID = "com.googlecode.goclipse.contentassistprocessor";
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// Force construction of singleton
		Environment.INSTANCE.toString();
		
		GoBuilder.checkForCompilerUpdates(true);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	@Override
	public IPreferenceStore getPreferenceStore() {
		return super.getPreferenceStore();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Log the given info message to the Eclipse log.
	 */
	public static void logInfo(String message) {
		if (Environment.DEBUG) {
			getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
		}
	}
	
	/**
	 * Log the given info message to the Eclipse log.
	 */
	public static void logInfo(Throwable t) {
		if (Environment.DEBUG) {
			getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, t.getMessage(), t));
		}
	}
	
	/**
	 * Log the given warning message to the Eclipse log.
	 */
	public static void logWarning(String message) {
		getDefault().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message));
	}
	
	/**
	 * Log the given error message to the Eclipse log.
	 */
	public static void logError(String message) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message));
	}
	
	/**
	 * Log the given exception to the Eclipse log.
	 * 
	 * @param t the exception to log
	 */
	public static void logError(Throwable t) {
	  if (getDefault() != null) {
	    getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, t.getMessage(), t));
	  }
	}
	
	public static boolean isMac() {
	  return Util.isMac();
	}

	public static boolean isWindows() {
	  return Util.isWindows();
	}
	
}
