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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import melnorme.util.swt.SWTFactory;
import melnorme.utilbox.status.StatusException;

public abstract class StatusMessageDialogWithIgnore extends StatusMessageDialog2 {
	
	protected final String ignoreButtonLabel;
	
	protected Button ignore;
	
	public StatusMessageDialogWithIgnore(
		Shell parentShell, String title, StatusException statusException, String ignoreButtonLabel
	) {
		super(parentShell, title, statusException);
		this.ignoreButtonLabel = ignoreButtonLabel;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogParent = (Composite) super.createDialogArea(parent);
		createIgnoreArea(dialogParent);
		return dialogParent;
	}
	
	protected void createIgnoreArea(Composite parent) {
		ignore = SWTFactory.createButton(
			parent, 
			SWT.CHECK, 
			ignoreButtonLabel,
			GridDataFactory.swtDefaults().span(2, 1).create()
		);
		ignore.setSelection(true);
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