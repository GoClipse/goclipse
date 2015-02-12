/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components.fields;

import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public abstract class ButtonTextField extends TextField {
	
	protected final String buttonLabel;
	protected Button button;
	
	public ButtonTextField(String label, String buttonlabel) {
		super(label);
		buttonLabel = buttonlabel;
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 3;
	}
	
	@Override
	protected void createContents_do(Composite topControl) {
		super.createContents_do(topControl);
		
		button = SWTFactoryUtil.createPushButton(topControl, getButtonLabel(), null);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonSelected();
			}
		});
	}
	
	protected String getButtonLabel() {
		return buttonLabel;
	}
	
	@Override
	protected void createContents_layout() {
		layoutControls(array(label, text, button), text, text);
	}
	
	protected void handleButtonSelected() {
		String result = getNewValueFromButtonSelection();
		
		if(result != null) {
			setFieldValue(result);
		}
	}
	
	protected abstract String getNewValueFromButtonSelection();
	
}