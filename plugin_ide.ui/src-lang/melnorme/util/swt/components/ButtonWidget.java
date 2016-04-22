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
package melnorme.util.swt.components;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.WidgetSelectedRunner;

public class ButtonWidget extends AbstractDisableableWidget {

	protected String buttonLabel;
	protected Runnable buttonHandler;
	
	protected Button button;
	
	public ButtonWidget(String buttonlabel, Runnable buttonHandler) {
		this.buttonLabel = buttonlabel;
		this.buttonHandler = assertNotNull(buttonHandler);
	}
	
	protected String getButtonLabel() {
		return buttonLabel;
	}
	
	public Button getButton() {
		return button;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		button = SWTFactory.createButton(topControl, SWT.PUSH, buttonLabel);
		/* FIXME: button width */
//		button = SWTFactoryUtil.createPushButton(topControl, buttonLabel, null);
		button.addSelectionListener(new WidgetSelectedRunner(buttonHandler));
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		SWTUtil.setEnabledIfOk(button, enabled);
	}
	
}