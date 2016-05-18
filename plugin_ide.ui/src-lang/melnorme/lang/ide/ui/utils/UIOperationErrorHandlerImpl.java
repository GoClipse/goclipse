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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore.StatusExt;
import melnorme.lang.ide.core.utils.EclipseUtils;
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
		
		if(status.getCause() == null) {
			// No point in using ErrorDialog, use simpler dialog
			openMessageDialog(MessageDialog.ERROR, shell, title, status.getMessage());
			return;
		}
		
		openErrorDialog(shell, title, status);
	}
	
	protected void openMessageDialog(int kind, Shell shell, String title, String message) {
		MessageDialog.open(kind, shell, title, message, SWT.SHEET);
	}
	
	protected void openErrorDialog(Shell shell, String title, StatusException status) {
		new ErrorDialogExt(shell, title, status).open();
	}
	
	/* -----------------  ----------------- */
	
	public final void displayStatusMessage(String title, StatusException status) {
		handleStatus(false, null, title, status);
	}
	
	public void displayStatusMessage(String title, Severity severity, String message) {
		handleStatus(false, null, title, new StatusException(severity, message));
	}
	
	
	
	/* -----------------  ----------------- */
	
	public static class ErrorDialogExt extends ErrorDialog {
		
		public ErrorDialogExt(Shell parentShell, String dialogTitle, StatusException se) {
			super(parentShell, dialogTitle, "XXX", createDialogStatus(se), 
				IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
			
			this.message = se.getMessage();
		}
		
		protected static StatusExt createDialogStatus(StatusException se) {
			return LangCore.createStatus(
				EclipseUtils.toEclipseSeverity(se), 
				null, 
				new Exception(getExceptionText(se.getCause()))
			);
		}
		
	}
	
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