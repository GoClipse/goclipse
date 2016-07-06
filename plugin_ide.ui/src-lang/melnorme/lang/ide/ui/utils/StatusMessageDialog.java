/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.status.StatusLevel;

public abstract class StatusMessageDialog extends IconAndMessageDialog {
	
	protected final String ignoreButtonLabel;
	protected final String title;
	protected final StatusLevel statusLevel;
	
	protected Button ignore;
	
	public StatusMessageDialog(Shell parentShell, String title, StatusLevel statusLevel, String message) {
		this(parentShell, title, statusLevel, message,
			"Ignore similar errors for this operation, during this session.");
	}
	
	public StatusMessageDialog(Shell parentShell, String title, StatusLevel statusLevel, String message,
			String ignoreButtonLabel) {
		super(parentShell);
		this.title = title;
		this.statusLevel = statusLevel;
		this.message = message;
		this.ignoreButtonLabel = ignoreButtonLabel;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}
	
	@Override
	protected Image getImage() {
		return SWTUtil.getSWTImageForStatusLevel(statusLevel);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createMessageArea(composite);
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING) * 2;
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		ignore = SWTFactory.createButton(parent, SWT.CHECK, ignoreButtonLabel,
			GridDataFactory.swtDefaults().span(2, 1).create());
		ignore.setSelection(true);
		
		return composite;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}
	
	@Override
	public void okPressed() {
		if(ignore.getSelection()) {
			setIgnoreFutureMessages();
		}
		super.okPressed();
	}
	
	protected abstract void setIgnoreFutureMessages();
	
}