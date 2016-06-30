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

import org.eclipse.core.runtime.ILog;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.misc.ILogHandler;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

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
	
	/* ----------------- Logging ----------------- */
	
	public static final ILogHandler LOG_HANDLER = new ILogHandler() {
		@Override
		public void logStatus(StatusException se) {
			int severity = EclipseUtils.toEclipseSeverity(se);
			ILog log = LangCorePlugin.getInstance().getLog();
			log.log(EclipseCore.createStatus(severity, se.getMessage(), se.getCause()));
		}
	};
	
	/** Logs status of given StatusException. */
	public static void logStatusException(StatusException se) {
		LOG_HANDLER.logStatus(se);
	}
	
	public static void logInternalError(Throwable throwable) {
		logError("Internal Error!", throwable);
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		logError(message, null);
	}
	/** Logs an error status with given message and given throwable. */
	public static void logError(String message, Throwable throwable) {
		logStatusException(new StatusException(Severity.ERROR, message, throwable));
	}
	
	/** Logs a warning status with given message. */
	public static void logWarning(String message) {
		logWarning(message, null);
	}
	/** Logs a warning status with given message and given throwable. */
	public static void logWarning(String message, Throwable throwable) {
		logStatusException(new StatusException(Severity.WARNING, message, throwable));
	}
	
	/** Logs an info status with given message. */
	public static void logInfo(String message) {
		logStatusException(new StatusException(Severity.INFO, message, null));
	}
	
}