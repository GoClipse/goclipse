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

import static org.eclipse.jface.layout.GridDataFactory.fillDefaults;
import melnorme.lang.ide.ui.tools.AbstractDeamonToolPrefPage;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.tools.GocodeServerManager;

public class GoToolsPreferencePage extends AbstractDeamonToolPrefPage implements
		IWorkbenchPreferencePage {
	
	protected Group oracleGroup;
	protected Label gocodeLabel;
	
	@Override
	protected void doCreateContents(Composite block) {
		oracleGroup = SWTFactoryUtil.createGroup(block,
			"Go oracle",
			GridDataFactory.fillDefaults().grab(true, false).minSize(200, SWT.DEFAULT).create());
		GridLayoutFactory.fillDefaults().numColumns(3).margins(6, 4).applyTo(oracleGroup);
		
		createFileTextField(oracleGroup, "Go oracle path:", GoToolPreferences.GO_ORACLE_Path.key);
		
		super.doCreateContents(block);
	}
	
	@Override
	protected String getDaemonToolName() {
		return "Gocode";
	}
	
	@Override
	protected void createDaemonPathFieldEditor(Group group) {
		super.createDaemonPathFieldEditor(group);
		IPath gocodePath = GocodeServerManager.getBestGocodePath();
		String labelText = 
				gocodePath == null? "" : 
				"If path is empty, the built-in gocode will be used:\n" + gocodePath.toOSString();
		
		gocodeLabel = SWTFactoryUtil.createLabel(group, SWT.WRAP, 
			labelText,
			fillDefaults().span(3, 1).grab(true, false).indent(10, 0).minSize(200, SWT.DEFAULT).create());
	}
	
}