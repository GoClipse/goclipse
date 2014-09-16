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
package melnorme.lang.ide.ui.utils;


import melnorme.lang.ide.core.LangCore;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility for handling exceptions during UI operations, by presenting an information dialog to the user.
 */
public class UIOperationExceptionHandler {
	
	public static void handle(CoreException e, String title, String message) {
		LangCore.logError(e);
		Shell shell = WorkbenchUtils.getActiveWorkbenchShell();
		ErrorDialog.openError(shell, title, message, e.getStatus());
	}
	
	/* -----------------  ----------------- */
	
	public static void handleError(String errorMessage, Throwable exception) {
		handleException(null, errorMessage, exception);
	}
	
	public static void handleException(String errorMessage, Throwable exception) {
		handleException(null, errorMessage, exception);
	}
	
	public static void handleException(String operation, String errorMessage, Throwable exception) {
		String title = (operation == null) ? "Error" : "Error during " + operation; 
		doHandleException(title, errorMessage, exception);
	}
	
	public static void doHandleException(String title, String errorMessage, Throwable exception) {
		LangCore.logError(errorMessage, exception);
		
		Shell shell = WorkbenchUtils.getActiveWorkbenchShell();
		
		String reasonMessage = exception.getMessage();
		if(reasonMessage == null) {
			reasonMessage = exception.getClass().getSimpleName();
		}
		Status status = LangCore.createErrorStatus(reasonMessage, exception);
		ErrorDialog.openError(shell, title, errorMessage, status);
	}
	
}