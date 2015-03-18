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
import melnorme.lang.ide.ui.utils.UIOperationsHelper;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.utilbox.misc.StringUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.operations.GetAndInstallGocodeOperation;

public class GoToolsPreferencePage extends AbstractDeamonToolPrefPage implements
		IWorkbenchPreferencePage {
	
	protected Group oracleGroup;
	
	@Override
	protected void doCreateContents(Composite block) {
		oracleGroup = createOptionsSection(block, 
			"Go oracle",
			GridDataFactory.fillDefaults().grab(true, false).minSize(200, SWT.DEFAULT).create(),
			3);
		
		createFileComponent(oracleGroup, "Go oracle path:", GoToolPreferences.GO_ORACLE_Path.key, true);
		
		super.doCreateContents(block);
	}
	
	@Override
	protected String getDaemonToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
	@Override
	protected ButtonTextField createDaemonPathFieldEditor(Group group) {
		ButtonTextField result = super.createDaemonPathFieldEditor(group);
		
		final GetAndInstallGocodeOperation op = new GetAndInstallGocodeOperation();
		String buttonLabel = "Download gocode (run: `" + StringUtil.collToString(op.getCmdLine(), " ")  + "`)";
		
		Button getGocodeButton = SWTFactoryUtil.createButton(group, SWT.PUSH, buttonLabel, 
				GridDataFactory.fillDefaults().span(3, 0).create());
		
		getGocodeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean success = UIOperationsHelper.runAndHandle(workbench.getActiveWorkbenchWindow(), op, true, 
					"Download gocode error.");
				
				if(success) {
					daemonPathEditor.setFieldValue(op.getGocodeExeLocation().toPathString());
				}
			}
		});
		
		return result;
	}
	
}