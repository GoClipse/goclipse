/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/

package melnorme.util.swt.components.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.IStatusMessage.StatusMessage;
import melnorme.utilbox.status.Severity;

public class StatusMessageWidget extends AbstractWidget {
	
	protected Composite topControl;
	
	protected Label icon;
	protected Link hintText;
	
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	public void createComponentInlined(Composite parent) {
		this.topControl = parent;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		this.topControl = topControl;
		
		icon = SWTFactoryUtil.createIconLabel(topControl, 
			null, 
			GridDataFactory.swtDefaults().create());
		
		hintText = SWTFactoryUtil.createLink(topControl, SWT.LEFT, "", 
			GridDataFactory.fillDefaults().grab(true, false).create());
	}
	
	@Override
	public void updateWidgetFromInput() {
		updateWidget(null);
	}
	
	public void updateWidget(IStatusMessage statusMessage) {
		if(!SWTUtil.isOkToUse(icon)) {
			return;
		}
		
		topControl.setVisible(statusMessage != null);
		((GridData) topControl.getLayoutData()).exclude = statusMessage == null;
		
		icon.setImage(statusMessage == null ? null : getImageForSeverity(statusMessage.getSeverity()));
		hintText.setText(statusMessage == null ? "" : statusMessage.getMessage());
		
		updateLayout();
	}
	
	protected void updateLayout() {
		topControl.getParent().layout(true, true);
	}
	
	public void setStatusMessage(Severity severity, String message) {
		assertNotNull(message);
		setStatusMessage(new StatusMessage(severity, message));
	}
	
	public void setStatusMessage(IStatusMessage statusMessage) {
		updateWidget(statusMessage);
	}
	
	public static Image getImageForSeverity(Severity severity) {
		switch (severity) {
		case INFO: return Dialog.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
		case WARNING: return Dialog.getImage(Dialog.DLG_IMG_MESSAGE_WARNING);
		case ERROR: return Dialog.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
		}
		throw assertFail();
	}
	
}