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
package com.googlecode.goclipse.ui.properties;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextField {
	
	protected String labelText;
	
	protected Label label;
	protected Text text;
	
	public TextField(String labelText) {
		this.labelText = labelText;
	}
	
	public void createControls(Composite parent, int numColumns) {
		assertTrue(numColumns >= 2);
		
		createLabel(parent);
		createText(parent);
		
		label.setLayoutData(GridDataFactory.swtDefaults().create());
		text.setLayoutData(GridDataFactory.fillDefaults().span(numColumns - 1, 1).create());
	}
	
	protected void createText(Composite parent) {
		text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData());
	}
	
	protected void createLabel(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText(labelText);
	}
	
	public void setValue(String value) {
		text.setText(value);
	}
	
	public String getValue() {
		return text.getText();
	}
	
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		text.setEnabled(enabled);
	}
	
}