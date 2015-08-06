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
package melnorme.lang.ide.ui.utils;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Shell;

import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.core.CommonException;

public class UIOperationExceptionHandler {
	
	// Normally, handler instance is never changed. Only in tests is it changed.
	public volatile static UIOperationErrorHandlerImpl handler = new UIOperationErrorHandlerImpl();
	
	public static void handleStatusMessage(String title, StatusException se) {
		handler.handleStatusMessage(title, se);
	}
	
	public static void handleStatusMessage(StatusLevel statusLevel, String title, String message) {
		handler.handleStatusMessage(statusLevel, title, message);
	}
	
	/* -----------------  ----------------- */
	
	public static void handleError(String message, Throwable exception) {
		handler.handleError(message, exception);
	}
	
	public static void handleError(boolean logError, String message, Throwable exception) {
		handler.handleError(logError, message, exception);
	}
	
	public static void handleError(boolean logError, String title, String message, Throwable exception) {
		handler.handleError(logError, title, message, exception);
	}
	
	public static void handleError(boolean logError, Shell shell, String title, String message, Throwable exception) {
		handler.handleError(logError, shell, title, message, exception);
	}
	
	/* -----------------  ----------------- */
	
	public static void handleOperationStatus(String dialogTitle, CoreException ce) {
		handler.handleOperationStatus(dialogTitle, ce);
	}
	
	public static void handleOperationStatus(String dialogTitle, CommonException ce) {
		handler.handleOperationStatus(dialogTitle, ce);
	}
	
}