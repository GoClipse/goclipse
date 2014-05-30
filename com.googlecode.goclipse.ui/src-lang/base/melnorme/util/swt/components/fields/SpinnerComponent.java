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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class SpinnerComponent extends AbstractComponent {
	
	protected String labelText;
	
	protected Label label;
	protected Spinner spinner;
	
	public SpinnerComponent(String labelText) {
		this.labelText = labelText;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		GridLayout layoutData = (GridLayout) topControl.getLayout();
		createContents(topControl, layoutData.numColumns);
	}
	
	public void createContents(Composite parent, int numColumns) {
		createLabel(parent);
		createSpinner(parent);
		
		assertTrue(numColumns >= 2);
		label.setLayoutData(GridDataFactory.swtDefaults().create());
		spinner.setLayoutData(GridDataFactory.swtDefaults().span(numColumns - 1, 1).create());
	}
	
	protected void createLabel(Composite parent) {
		label = SWTFactoryUtil.createLabel(parent, SWT.NONE, labelText, null);
	}
	
	protected void createSpinner(Composite parent) {
		spinner = new Spinner(parent, SWT.BORDER);
	}
	
	public Spinner getSpinner() {
		return spinner;
	}
	
	public void setValue(int value) {
		spinner.setSelection(value);
	}
	
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		spinner.setEnabled(enabled);
	}
	
	public int getValue() {
		return spinner.getDigits();
	}
	
	public SpinnerComponent setValueMinimum(int minimum) {
		spinner.setMinimum(minimum);
		return this;
	}
	
	public SpinnerComponent setValueMaximum(int maximum) {
		spinner.setMaximum(maximum);
		return this;
	}
	
	public SpinnerComponent setValueIncrement(int increment) {
		spinner.setIncrement(increment);
		return this;
	}
	
}