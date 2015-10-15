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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.misc.StringUtil;

public class DialogPageUtils {
	
	public static int statusLevelToMessageType(StatusLevel statusLevel) {
		switch (statusLevel) {
		case OK: return IMessageProvider.NONE;
		case INFO: return IMessageProvider.INFORMATION;
		case WARNING: return IMessageProvider.WARNING;
		case ERROR: return IMessageProvider.ERROR;
		}
		throw assertFail();
	}

	public static int severityToMessageType(IStatus status) {
		switch (status.getSeverity()) {
		case IStatus.ERROR: return IMessageProvider.ERROR;
		case IStatus.WARNING: return IMessageProvider.WARNING;
		case IStatus.INFO: return IMessageProvider.INFORMATION;
		case IStatus.OK: return IMessageProvider.NONE;
		default: return IMessageProvider.NONE;
		}
	}
	
	public static void setPrefPageStatus(PreferencePage prefPage, IStatusMessage status) {
		if(!SWTUtil.isOkToUse(prefPage.getControl())) {
			return;
		}
		
		prefPage.setValid(status == null || status.getStatusLevel() != StatusLevel.ERROR);
		
		setDialogPageStatus(prefPage, status);
	}
	
	public static void setDialogPageStatus(PreferencePage prefPage, IStatusMessage status) {
		if(!SWTUtil.isOkToUse(prefPage.getControl())) {
			return;
		}
		
		if(status == null) {
			prefPage.setMessage(null);
		} else {
			prefPage.setMessage(status.getMessage(), statusLevelToMessageType(status.getStatusLevel()));
		}
	}
	
	public static void applyStatusToPage(PreferencePage preferencePage, IStatus status) {
		preferencePage.setValid(status.isOK());
		applyStatusToPage(preferencePage, status);
	}
	
	// Whats the the difference with using setErrorMessage?
	
	public static void applyStatusToPage(DialogPage page, IStatus status) {
		String message = StringUtil.emptyAsNull(status.getMessage());
		
		if(status.getSeverity() == IStatus.ERROR) {
			page.setErrorMessage(message);
			page.setMessage(null);
		} else {
			page.setErrorMessage(null);
			page.setMessage(message, severityToMessageType(status));
		}
	}
	
}