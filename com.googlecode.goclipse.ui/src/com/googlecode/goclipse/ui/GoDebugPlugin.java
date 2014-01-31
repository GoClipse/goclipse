package com.googlecode.goclipse.ui;



// TODO: refactor this out to GoUIPlugin code
/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author devoncarew
 */
@Deprecated
public class GoDebugPlugin {
	
	public static void logError(Throwable exception) {
		GoUIPlugin.log(exception);
	}
	
}