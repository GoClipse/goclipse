package com.googlecode.goclipse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Util;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoBuilder;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author steel
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.googlecode.goclipse.core";

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
	
	private ScopedPreferenceStore preferenceStore;

	public IPreferenceStore getPreferenceStore() {
        // Create the preference store lazily.
        if (preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, getBundle().getSymbolicName());

        }
        return preferenceStore;
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
