/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package lang_project_id.jvmcheck;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class JvmCheck implements IStartup, JvmCheckConstants_Actual {
	
	public static String getJavaVersionProperty() {
		return System.getProperty("java.version");
	}
	
	public static int getJavaVersion() {
	    String versionProperty = getJavaVersionProperty();
	    String[] versionSegments = versionProperty.split("\\.");
	    
	    if(versionSegments.length < 2) {
	    	return -1;
	    }
	    String javaVersionStr = versionSegments[1];
		
	    try {
			return Integer.parseInt(javaVersionStr);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	
	@Override
	public void earlyStartup() {
		final int javaVersion = getJavaVersion();
		
		if(javaVersion >= REQUIRED_JAVA_VERSION)
			return;
		
		// Show error message to the user, because the platform just silently fails. 
		// See: https://bugs.eclipse.org/bugs/show_bug.cgi?id=417336
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Shell activeShell = getActiveWorkbenchShell();
				
				String message = "Could not start " + FEATURE_NAME + " because Java version is: " + javaVersion 
						+ "\nVersion " + REQUIRED_JAVA_VERSION + " is required";
				
				System.err.println(message);
				
				if(activeShell == null) {
					return;
				}
				MessageDialog.openError(activeShell, "Error", message);
			}
		});
	
	}
	
	/** Gets the active workbench window. */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	/** Gets the active workbench shell. */
	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if(window != null) {
			return window.getShell();
		}
		return null;
	}
	
}