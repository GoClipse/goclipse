/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.pages;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.WidgetSelectedRunner;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.util.swt.components.fields.SetFieldValueOperation;
import melnorme.utilbox.concurrency.OperationCancellation;

public abstract class DownloadToolTextField extends ButtonTextField {
	
	protected String downloadButtonLabel;
	protected Button downloadButton;
	
	public DownloadToolTextField() {
		this("Executable:", "Download...");
	}
	
	public DownloadToolTextField(String label, String downloadButtonLabel) {
		this(label, FileTextField.DEFAULT_BUTTON_LABEL, downloadButtonLabel);
	}
	
	public DownloadToolTextField(String label, String buttonlabel, String downloadButtonLabel) {
		super(label, buttonlabel);
		this.downloadButtonLabel = downloadButtonLabel;
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 4;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		super.createContents_all(topControl);
		
		createContents_DownloadButton(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layoutControls(array(label, text, button, downloadButton), text, text);
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		super.doSetEnabled(enabled);
		SWTUtil.setEnabledIfOk(downloadButton, enabled);
	}
	
	/* ----------------- Button ----------------- */
	
	@Override
	protected BasicUIOperation getButtonHandler() {
		return new SetFieldValueOperation<String>(this, this::getNewValueFromButtonSelection2);
	}
	
	@Override
	protected String getNewValueFromButtonSelection2() throws OperationCancellation {
		return ControlUtils.openFileDialog(getFieldValue(), button.getShell());
	}
	
	/* -----------------  Download Button  ----------------- */
	
	protected void createContents_DownloadButton(Composite topControl) {
		downloadButton = SWTFactoryUtil.createPushButton(topControl, downloadButtonLabel, null);
		downloadButton.addSelectionListener(new WidgetSelectedRunner(getDownloadButtonHandler()));
	}
	
	
	protected abstract BasicUIOperation getDownloadButtonHandler();
	
}