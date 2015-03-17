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

import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.tools.AbstractDeamonToolPrefPage;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;

public class GoToolsPreferencePage extends AbstractDeamonToolPrefPage implements
		IWorkbenchPreferencePage {
	
	protected Group oracleGroup;
	
	@Override
	protected void doCreateContents(Composite block) {
		oracleGroup = SWTFactoryUtil.createGroup(block,
			"Go oracle",
			GridDataFactory.fillDefaults().grab(true, false).minSize(200, SWT.DEFAULT).create());
		GridLayoutFactory.fillDefaults().numColumns(3).margins(6, 4).applyTo(oracleGroup);
		
		createFileComponent(oracleGroup, "Go oracle path:", GoToolPreferences.GO_ORACLE_Path.key, true);
		
		super.doCreateContents(block);
	}
	
	@Override
	protected String getDaemonToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
	@Override
	protected void createDaemonPathFieldEditor(Group group) {
		super.createDaemonPathFieldEditor(group);
	}
	
}