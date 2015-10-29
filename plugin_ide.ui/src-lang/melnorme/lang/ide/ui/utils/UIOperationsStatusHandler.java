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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.core.CommonException;

public class UIOperationsStatusHandler {
	
	// Normally, handler instance is never changed. Only in tests is it changed.
	public volatile static UIOperationErrorHandlerImpl handler = new UIOperationErrorHandlerImpl();
	
	/* -----------------  ----------------- */
	
	public static void handleStatus(boolean logError, Shell shell, String title, String message, Throwable exception) {
		handler.handleStatus(logError, shell, title, message, exception);
	}
	
	public static void handleStatus(boolean logError, Shell shell, String title, StatusException status) {
		handler.handleStatus(logError, shell, title, status);
	}
	
	public static void handleStatus(boolean logError, String title, StatusException status) {
		handler.handleStatus(logError, null, title, status);
	}
	
	public static void handleStatus(String title, StatusException status) {
		handler.handleStatus(true, null, title, status);
	}
	
	/* -----------------  ----------------- */
	
	public static void handleStatus(boolean logError, String dialogTitle, CoreException ce) {
		handleStatus(logError, dialogTitle, LangCore.createCommonException(ce));
	}
	
	public static void handleStatus(boolean logError, String dialogTitle, CommonException ce) {
		handleStatus(logError, dialogTitle, ce.toStatusException(StatusLevel.ERROR));
	}
	
	public static void handleStatus(String dialogTitle, CommonException ce) {
		handleStatus(false, dialogTitle, ce);
	}
	
	/* -----------------  ----------------- */
	
	public static void displayStatusMessage(String title, StatusException status) {
		handler.displayStatusMessage(title, status);
	}
	
	public static void displayStatusMessage(String title, StatusLevel statusLevel, String message) {
		handler.displayStatusMessage(title, statusLevel, message);
	}
	
	public static void handleInternalError(String message, Throwable exception) {
		handler.handleInternalError(null, message, exception);
	}
	
	public static void handleInternalError(Shell shell, String message, Throwable exception) {
		handler.handleInternalError(shell, message, exception);
	}
	
	public static void handleOperationStatus(String opName, CoreException ce) {
		handleOperationStatus(opName, LangCore.createCommonException(ce));
	}
	
	public static void handleOperationStatus(String opName, CommonException ce) {
		String dialogTitle = opName + " - " + LangCoreMessages.Msg_Error;
		handleStatus(false, dialogTitle, ce);
	}
	
	/* -----------------  ----------------- */
	
	public static class Null_UIOperationErrorHandlerImpl extends UIOperationErrorHandlerImpl {
		
		@Override
		protected void openMessageDialog(int kind, Shell shell, String title, String message) {
			//super.openMessageDialog(kind, shell, title, message);
		}
		
		@Override
		protected void openErrorDialog(Shell shell, String title, StatusException status) {
			//super.openErrorDialog(shell, title, status);
		}
		
	}
	
}