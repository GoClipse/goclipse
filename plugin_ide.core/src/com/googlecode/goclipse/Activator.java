package com.googlecode.goclipse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.googlecode.goclipse.core.GoCore;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author steel
 */
public class Activator extends GoCore {
	
	
	/**
	 * Log the given info message to the Eclipse log.
	 */
	public static void logInfo(String message) {
		if (Environment.DEBUG) {
			GoCore.logStatus(new Status(IStatus.INFO, PLUGIN_ID, message));
		}
	}
	
	/**
	 * Log the given info message to the Eclipse log.
	 */
	public static void logInfo(Throwable t) {
		if (Environment.DEBUG) {
			GoCore.logStatus(new Status(IStatus.INFO, PLUGIN_ID, t.getMessage(), t));
		}
	}
	
}