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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.swt.widgets.Shell;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

/**
 * Utility for handling exceptions during UI operations, by presenting an information dialog to the user.
 */
public class UIOperationErrorHandlerImpl {
	
	public void handleStatus(boolean logError, Shell shell, String title, String message, Throwable exception) {
		if(message == null) {
			message = "Error: ";
		}
		
		StatusException status = new StatusException(Severity.ERROR, message, exception);
		
		handleStatus(logError, shell, title, status);
	}
	
	public void handleStatus(boolean logError, Shell shell, String title, StatusException status) {
		if(logError) {
			LangCore.logStatusException(status);
		}
		
		if(shell == null) {
			shell = WorkbenchUtils.getActiveWorkbenchShell();
		}
		assertNotNull(shell);
		
		openMessageDialog(shell, title, status);
	}
	
	protected void openMessageDialog(Shell shell, String title, StatusException status) {
		new StatusMessageDialogExt(shell, title, status).open();
	}
	
	/* -----------------  ----------------- */
	
	public final void displayStatusMessage(String title, StatusException status) {
		handleStatus(false, null, title, status);
	}
	
	public void displayStatusMessage(String title, Severity severity, String message) {
		handleStatus(false, null, title, new StatusException(severity, message));
	}
	
	
	
	/* -----------------  ----------------- */
	
	protected static String getExceptionText(Throwable exception) {
		if(exception == null)
			return null;
		
		String exceptionText = exception.getClass().getName();
		if(exception.getMessage() != null) {
			exceptionText += ": " + exception.getMessage();
		}
		return exceptionText;
	}
	
}