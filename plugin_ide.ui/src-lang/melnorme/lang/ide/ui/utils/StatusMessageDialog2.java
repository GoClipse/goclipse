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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.utilbox.status.StatusException;

public class StatusMessageDialog2 extends Dialog {
	
	protected final String title;
	
	protected final IconAndMessageWidget iconAndMessageWidget;
	
	public StatusMessageDialog2(Shell shell, String title, StatusException statusMessage) {
		super(shell);
		assertNotNull(statusMessage);
		this.title = title;
		this.iconAndMessageWidget = createIconAndMessageWidget();
		this.iconAndMessageWidget.setStatusMessage(statusMessage);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		return super.createContents(parent);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite topControl = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) topControl.getLayout();
		gridLayout.numColumns = 2;
//		gridLayout.marginHeight = 0;
//		gridLayout.marginWidth = 0;
		iconAndMessageWidget.createComponentInlined(topControl);
		iconAndMessageWidget.messageControlLayoutData().widthHint = 
				convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		
		return topControl;
	}
	
	protected IconAndMessageWidget createIconAndMessageWidget() {
		return new IconAndMessageWidget();
	}
	
	public static class IconAndMessageWidget extends AbstractWidget {
		
		protected StatusException statusMessage;
		
		protected Label imageControl;
		protected Label messageControl;
		
		public IconAndMessageWidget() {
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 2;
		}
		
		public void setStatusMessage(StatusException statusMessage) {
			this.statusMessage = assertNotNull(statusMessage);
		}
		
		@Override
		protected void createContents(Composite topControl) {
			imageControl = createImageControl(topControl);
			createMessageControl(topControl);
		}
		
		protected Label createImageControl(Composite topControl) {
			return SWTFactoryUtil.createIconLabel(topControl, getImage(), 
				GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING).create()
			);
		}
		
		protected Image getImage() {
			return SWTUtil.getImageForSeverity(statusMessage.getSeverity());
		}
		
		protected void createMessageControl(Composite topControl) {
			String message = statusMessage.getMultiLineRender();
			messageControl = SWTFactoryUtil.createLabel(topControl, SWT.WRAP, message, gdGrabAll());
		}
		
		public GridData messageControlLayoutData() {
			assertNotNull(messageControl);
			return (GridData) messageControl.getLayoutData();
		}
		
		@Override
		protected void updateWidgetFromInput() {
			// immutable widget
		}
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}
	
	// public access
	@Override
	public void okPressed() {
		super.okPressed();
	}
	
}