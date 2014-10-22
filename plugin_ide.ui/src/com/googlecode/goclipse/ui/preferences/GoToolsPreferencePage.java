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
package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.tools.AbstractDeamonToolPrefPage;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.jface.preference.FileFieldEditorExt;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.tools.GocodeServerManager;

public class GoToolsPreferencePage extends AbstractDeamonToolPrefPage implements
		IWorkbenchPreferencePage {
	
	protected Group oracleGroup;
	protected Label gocodeLabel;

	@Override
	public void init(IWorkbench workbench) {
	}
	
	@Override
	protected void createFieldEditors() {
		oracleGroup = SWTFactoryUtil.createGroup(getFieldEditorParent(),
			"Go oracle",
			GridDataFactory.fillDefaults().grab(true, false).minSize(200, SWT.DEFAULT).create());
		
		addField(new FileFieldEditorExt(GoToolPreferences.GO_ORACLE_Path.key, "Go oracle path:", oracleGroup));
		GridLayoutFactory.fillDefaults().margins(6, 4).applyTo(oracleGroup);
		
		super.createFieldEditors();
	}
	
	@Override
	protected String getDaemonToolName() {
		return "Gocode";
	}
	
	@Override
	protected void createDaemonPathFieldEditor(Group group) {
		super.createDaemonPathFieldEditor(group);
		gocodeLabel = SWTFactoryUtil.createLabel(group, SWT.WRAP, 
			"If path is empty, the built-in gocode will be used:\n" + GocodeServerManager.getBestGocodePath().toOSString(),
			GridDataFactory.fillDefaults().grab(true, false).indent(10, 0).minSize(200, SWT.DEFAULT).create());
	}
	
	@Override
	protected void adjustGridLayoutForFieldsNumberofColumns(int numColumns) {
		super.adjustGridLayoutForFieldsNumberofColumns(numColumns);
		((GridData) gocodeLabel.getLayoutData()).horizontalSpan = numColumns;
		((GridLayout) oracleGroup.getLayout()).numColumns = numColumns;
	}
	
}