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

import melnorme.util.swt.components.AbstractComponent;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextComponent extends AbstractComponent {
	
	protected String labelText;
	
	protected Label label;
	protected Text text;
	
	public TextComponent(String labelText) {
		this.labelText = labelText;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		GridLayout layoutData = (GridLayout) topControl.getLayout();
		createControls(topControl, layoutData.numColumns);
	}
	
	public void createControls(Composite parent, int numColumns) {
		label = createLabel(parent);
		text = createText(parent);
		
		label.setLayoutData(GridDataFactory.swtDefaults().create());
		numColumns--;
		if(numColumns == 0) {
			numColumns = 1;
		}
		text.setLayoutData(GridDataFactory.fillDefaults().span(numColumns, 1).create());
	}
	
	protected Label createLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		return label;
	}
	
	protected Text createText(Composite parent) {
		return new Text(parent, SWT.BORDER);
	}
	
	public Text getTextControl() {
		return text;
	}
	
	public void setValue(String value) {
		getTextControl().setText(value);
	}
	
	public String getValue() {
		return getTextControl().getText();
	}
	
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		text.setEnabled(enabled);
	}
	
}