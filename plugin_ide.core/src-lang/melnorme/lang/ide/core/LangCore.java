/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ILogHandler;

public class LangCore extends LangCore_Actual {
	
	public LangCore() {
	}
	
	protected void shutdown() {
		buildManager.dispose();
		bundleManager.shutdownManager();
		sourceModelManager.dispose();
		toolManager.shutdownNow();
	}
	
	/** 
	 * Start core agents, and do other initizaliation after UI is started.
	 */
	public void startAgentsAfterUIStart() {
		bundleManager.startManager();
	}
	
	/* ----------------- logging stuff ----------------- */
	
	protected static Plugin getInstance() {
		return LangCorePlugin.getInstance();
	}
	
	protected static ILog getLog() {
		return getInstance().getLog();
	}
	
	/* -----------------  ----------------- */
	
	/** Logs status of given StatusException. */
	public static void logStatusException(StatusException se) {
		int severity = EclipseUtils.toEclipseSeverity(se);
		getLog().log(createStatus(severity, se.getMessage(), se.getCause()));
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		getLog().log(createErrorStatus(message, null));
	}
	
	/** Logs an error status with given message and given throwable. */
	public static void logError(String message, Throwable throwable) {
		getLog().log(createErrorStatus(message, throwable));
	}
	
	/** Logs a warning status with given message */
	public static void logWarning(String message) {
		getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message, null));
	}
	
	/** Logs a warning status with given message and given throwable */
	public static void logWarning(String message, Throwable throwable) {
		getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message, throwable));
	}
	
	/** Logs an info status with given message */
	public static void logInfo(String message) {
		getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message, null));
	}
	
	public static void logInternalError(Throwable throwable) {
		logError("Internal Error!", throwable);
	}
	
	public static final ILogHandler LOG_HANDLER = new ILogHandler() {
		@Override
		public void logStatus(StatusException statusException) {
			logStatusException(statusException);
		}
	};
	
	/* ----------------- ----------------- */
	
	/** Creates an OK status with given message. */
	public static StatusExt createOkStatus(String message) {
		return createStatus(IStatus.OK, message, null);
	}
	
	/** Creates an Info status with given message. */
	public static StatusExt createInfoStatus(String message) {
		return createStatus(IStatus.INFO, message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message. */
	public static StatusExt createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static StatusExt createErrorStatus(String message, Throwable throwable) {
		return createStatus(IStatus.ERROR, message, throwable);
	}
	
	/** Creates a Status with given status code and message. */
	public static StatusExt createStatus(int severity, String message, Throwable throwable) {
		return new StatusExt(severity, getInstance(), message, throwable);
	}
	
	public static final class StatusExt extends Status {
		
		protected final Plugin plugin;
		
		public StatusExt(int severity, Plugin plugin, String message, Throwable exception) {
			super(severity, plugin.getBundle().getSymbolicName(), message, exception);
			this.plugin = plugin;
		}
		
		public void logInPlugin() {
			plugin.getLog().log(this);
		}
	}
	
	/** Creates a CoreException describing an error in this plugin. */
	public static CoreException createCoreException(String message, Throwable throwable) {
		return new CoreException(createErrorStatus(message, throwable));
	}
	
	/** Creates a CoreException describing an error in this plugin, from given {@link CommonException} */
	public static CoreException createCoreException(CommonException ce) {
		return createCoreException(ce.getMessage(), ce.getCause());
	}
	
	public static CommonException createCommonException(CoreException ce) {
		return new CommonException(ce.getMessage(), ce.getCause());
	}
	
	/* ----------------- Logging ----------------- */
	
	/** Logs given status. */
	public static void logStatus(IStatus status) {
		getInstance().getLog().log(status);
	}
	
	/** Logs status of given CoreException. */
	public static void logStatus(CoreException ce) {
		getInstance().getLog().log(ce.getStatus());
	}
	
}