/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.fields;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.launch.AbstractLaunchConfigurationTabExt;
import melnorme.util.swt.components.AbstractFieldExt;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class ArgumentsGroupField extends AbstractFieldExt<String> {
	
	protected Text text;
	protected int textStyle;
	protected Button variablesButton;
	
	public ArgumentsGroupField(String labelText) {
		this(labelText, SWT.BORDER);
	}
	
	public ArgumentsGroupField(String labelText, int textStyle) {
		super(labelText);
		this.textStyle = textStyle;
	}
	
	@Override
	public String getDefaultFieldValue() {
		return "";
	}
	
	@Override
	protected Composite createTopLevelControl(Composite parent) {
		Group topControl = new Group(parent, SWT.NONE);
		topControl.setText(labelText);
		createTopLevelControlLayout(topControl);
		return topControl;
	}
	
	@Override
	protected void createTopLevelControlLayout(Composite topControl) {
		topControl.setLayout(GridLayoutFactory.swtDefaults().numColumns(1).create());
	}
	
	@Override
	protected void createContents_do(Composite topControl) {
		text = createFieldText(this, topControl, textStyle);
		
		variablesButton = AbstractLaunchConfigurationTabExt.
				createVariablesButton(topControl, LangUIMessages.LangArgumentsTab_Variables, text); 
	}
	
	@Override
	protected void createContents_layout() {
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(40, 100).create());
		variablesButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	}
	
	@Override
	public Text getFieldControl() {
		return text;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		text.setText(getFieldValue());
	}
	
}