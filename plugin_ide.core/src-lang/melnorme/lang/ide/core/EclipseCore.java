/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.core.CommonException;

public class EclipseCore extends EclipseUtils {
	
	private static LangCorePlugin getPluginInstance() {
		return LangCorePlugin.getInstance();
	}
	
	/** Creates a Status with given status code and message. */
	public static Status createStatus(int severity, String message, Throwable throwable) {
		return new Status(severity, getPluginInstance().getBundle().getSymbolicName(), message, throwable);
	}
	
	/* -----------------  ----------------- */
	
	/** Creates an OK status with given message. */
	public static Status createOkStatus(String message) {
		return createStatus(IStatus.OK, message, null);
	}
	
	/** Creates an Info status with given message. */
	public static Status createInfoStatus(String message) {
		return createStatus(IStatus.INFO, message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message. */
	public static Status createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static Status createErrorStatus(String message, Throwable throwable) {
		return createStatus(IStatus.ERROR, message, throwable);
	}
	
	/** Creates a CoreException describing an error in this plugin. */
	public static CoreException createCoreException(String message, Throwable throwable) {
		return new CoreException(createErrorStatus(message, throwable));
	}
	
	/** Creates a CoreException describing an error in this plugin, from given {@link CommonException} */
	public static CoreException createCoreException(CommonException ce) {
		return createCoreException(ce.getMessage(), ce.getCause());
	}
	
	/* -----------------  ----------------- */
	
	/** Logs status of given CoreException. */
	public static void logStatus(CoreException ce) {
		getPluginInstance().getLog().log(ce.getStatus());
	}
	
}