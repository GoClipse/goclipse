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
package com.googlecode.goclipse.ui.launch;

import melnorme.util.swt.components.FieldComponent;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BuildConfigField extends FieldComponent<BuildConfiguration> {
	
	protected Combo buildCfgCombo;
	
	@Override
	public Composite createComponent(Composite parent, Object layoutData) {
		
		Composite topControl = new Composite(parent, SWT.NONE);
		topControl.setLayoutData(layoutData);
		topControl.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		
		Label label = new Label(topControl, SWT.NONE);
		label.setText("Build configuration:");
		label.setLayoutData(GridDataFactory.fillDefaults().grab(false, false).create());
		
		buildCfgCombo = new Combo(topControl, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		buildCfgCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fieldValueChanged(getFieldValue());
			}
		});
		
		for (BuildConfiguration buildConfiguration : BuildConfiguration.values()) {
			buildCfgCombo.add(buildConfiguration.toString());
		}
		buildCfgCombo.select(0);
		
		buildCfgCombo.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		return topControl;
	}
	
	@Override
	public void setFieldValue(BuildConfiguration buildCfg) {
		if(buildCfg == null) {
			buildCfgCombo.deselectAll();
		}
		buildCfgCombo.select(buildCfg.ordinal());
	}
	
	@Override
	public BuildConfiguration getFieldValue() {
		int selectionIndex = buildCfgCombo.getSelectionIndex();
		if(selectionIndex == -1) {
			return null;
		}
		return BuildConfiguration.values()[selectionIndex];
	}
	
	public String getFieldValueAsString() {
		BuildConfiguration fieldValue = getFieldValue();
		if(fieldValue != null) {
			return fieldValue.toString();
		}
		return null;
	}
	
}