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
package melnorme.lang.ide.ui.fields;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.WidgetFieldComponent;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Field for a program path relative to Eclipse project.
 */
public class ProgramPathField extends WidgetFieldComponent<String> {
	
	protected Text fProgramText;
	protected Button fSearchButton;
	
	@Override
	protected Composite createTopLevelControl(Composite parent) {
		Group topControl = new Group(parent, SWT.NONE);
		topControl.setText(LangUIMessages.ProgramPathField_title);
		topControl.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		return topControl;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		fProgramText = createFieldText(topControl, SWT.SINGLE | SWT.BORDER);
		fProgramText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fSearchButton = SWTFactoryUtil.createPushButton(topControl, 
				LangUIMessages.ProgramPathField__searchButton, null);
		fSearchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSearchButtonSelected();
			}
		});
	}
	
	@Override
	public void setFieldValue(String moduleName) {
		fProgramText.setText(moduleName);
	}
	
	@Override
	public String getFieldValue() {
		return fProgramText.getText();
	}
	
	protected void handleSearchButtonSelected() {
	}
	
}