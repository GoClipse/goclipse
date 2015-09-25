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

import melnorme.utilbox.misc.StringUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;


public class DialogPageUtils {

	public static int severityToMessageType(IStatus status) {
		switch (status.getSeverity()) {
		case IStatus.ERROR: return IMessageProvider.ERROR;
		case IStatus.WARNING: return IMessageProvider.WARNING;
		case IStatus.INFO: return IMessageProvider.INFORMATION;
		case IStatus.OK: return IMessageProvider.NONE;
		default: return IMessageProvider.NONE;
		}
	}
	
	public static void applyStatusMessageToPage(DialogPage page, IStatus status) {
		String message = StringUtil.emptyAsNull(status.getMessage());
		
		if(status.getSeverity() == IStatus.ERROR) {
			page.setErrorMessage(message);
			page.setMessage(null);
		} else {
			page.setErrorMessage(null);
			page.setMessage(message, severityToMessageType(status));
		}
	}
	
	public static void applyStatusToPreferencePage(IStatus status, PreferencePage preferencePage) {
		preferencePage.setValid(status.isOK());
		applyStatusMessageToPage(preferencePage, status);
	}
	
}