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

import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.LayoutUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextField extends AbstractField<String> {
	
	protected String labelText;
	
	protected Label label;
	protected Text text;
	
	public TextField(String labelText) {
		this.labelText = labelText;
	}
	
	@Override
	public String getDefaultFieldValue() {
		return "";
	}
	
	@Override
	protected void createContents(Composite topControl) {
		GridLayout layoutData = (GridLayout) topControl.getLayout();
		createControls(topControl, layoutData.numColumns);
	}
	
	public void createControls(Composite parent, int numColumns) {
		label = createLabel(parent);
		text = createText(parent);
		
		LayoutUtils.layout2Controls(numColumns, label, text);
	}
	
	protected Label createLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		return label;
	}
	
	protected Text createText(Composite parent) {
		return createFieldText(this, parent, SWT.BORDER);
	}
	
	@Override
	public Text getFieldControl() {
		return text;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		text.setText(getFieldValue());
	}
	
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		text.setEnabled(enabled);
	}
	
}