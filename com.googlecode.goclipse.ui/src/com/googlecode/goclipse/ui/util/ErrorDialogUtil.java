package com.googlecode.goclipse.ui.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.googlecode.goclipse.Activator;

/**
 * 
 * @author steel
 * 
 */
public class ErrorDialogUtil {

	public static void displayError(String title, String message) {
		displayError(null, title, message);
	}

	public static void displayError(String title, Throwable exception) {
		displayError(null, title, exception);
	}

	public static void displayError(Shell shell, String title, Throwable exception) {
		if (shell == null) {
			shell = Display.getDefault().getActiveShell();
		}

		ErrorDialog.openError(shell, title, null, new Status(IStatus.ERROR,
				Activator.PLUGIN_ID, exception.toString(), exception));
	}

	public static void displayError(Shell shell, String title, String message) {
		if (shell == null) {
			shell = Display.getDefault().getActiveShell();
		}

		ErrorDialog.openError(shell, title, null, new Status(IStatus.ERROR,
				Activator.PLUGIN_ID, message));
	}

}
