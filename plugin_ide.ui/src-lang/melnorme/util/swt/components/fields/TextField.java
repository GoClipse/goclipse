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

import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.components.AbstractFieldExt2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextField extends AbstractFieldExt2<String> {
	
	protected Label label;
	protected Text text;
	protected int defaultTextStyle;
	
	public TextField(String labelText) {
		this(labelText, SWT.SINGLE | SWT.BORDER);
	}
	
	public TextField(String labelText, int textStyle) {
		super(labelText, "");
		this.defaultTextStyle = textStyle;
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_do(Composite topControl) {
		createLabel(topControl);
		createText(topControl);
	}
	
	protected void createLabel(Composite parent) {
		label = SWTFactory.createLabel(parent, SWT.NONE, labelText);
	}
	
	protected void createText(Composite parent) {
		text = createFieldText(this, parent, defaultTextStyle);
	}
	
	@Override
	protected void createContents_layout() {
		layout2Controls_expandLast(label, text);
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