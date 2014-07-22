/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.launching;

import melnorme.lang.ide.core.LaunchingCore_Actual;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Core class for launching functionality.
 * It's similar to a plugin class, but not the same since launching code is still part of the main ide.core plugin
 */
public class LaunchingCore {
	
	public static final String PLUGIN_ID = LaunchingCore_Actual.PLUGIN_ID;
	
	public static final int LAUNCHING_CONFIG_ERROR = LaunchingCore_Actual.LAUNCHING_CONFIG_ERROR;
	
	public static CoreException createCoreException(Throwable exception, int code) {
		String message = exception.getMessage();
		return createCoreException(exception, code, message);
	}
	
	public static CoreException createCoreException(Throwable exception, int code, String message) {
		return new CoreException(new Status(IStatus.ERROR, LaunchingCore.PLUGIN_ID, code, message, exception));
	}
	
}