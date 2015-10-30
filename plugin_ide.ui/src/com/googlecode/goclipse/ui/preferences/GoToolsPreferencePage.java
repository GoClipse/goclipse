/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.preferences;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.operations.GetAndInstallGoPackageOperation;

import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.lang.ide.ui.preferences.pages.DaemonToolPreferencePage;
import melnorme.lang.ide.ui.utils.UIOperationsHelper;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.utilbox.misc.StringUtil;

public class GoToolsPreferencePage extends DaemonToolPreferencePage implements
		IWorkbenchPreferencePage {
	
	@Override
	protected AbstractPreferencesBlock init_createPreferencesBlock() {
		return new GoServerToolsBlock();
	}
	
	public static class GoServerToolsBlock extends ServerToolsBlock {
		
		public GoServerToolsBlock() {
			super();
		}
		
	protected Group oracleGroup;
	
	@Override
	protected void createContents(Composite topControl) {
		oracleGroup = AbstractPreferencesBlock.createOptionsSection(topControl, 
			"Go oracle",
			3,
			GridDataFactory.fillDefaults().grab(true, false).minSize(200, SWT.DEFAULT).create());
		
		FileTextField goOracleFileEditor = 
				createFileComponent(oracleGroup, "Go oracle path:", GoToolPreferences.GO_ORACLE_Path, true);
		
		createInstallPackageButton(oracleGroup, "Download Go oracle", "golang.org/x/tools/cmd/oracle", "oracle",
			goOracleFileEditor);
		
		super.createContents(topControl);
	}
	
	@Override
	protected String getDaemonToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
	@Override
	protected ButtonTextField createDaemonPathFieldEditor(Group daemonGroup) {
		daemonPathEditor = super.createDaemonPathFieldEditor(daemonGroup);
		
		createInstallPackageButton(daemonGroup, "Download gocode", "github.com/nsf/gocode", "gocode", 
			daemonPathEditor);
		
		return daemonPathEditor;
	}
	
	protected void createInstallPackageButton(Group group, final String baseButtonLabel, String goPackage, 
			String exeName, final ButtonTextField textFieldEditor) {
		assertNotNull(textFieldEditor);
		final GetAndInstallGoPackageOperation op = new GetAndInstallGoPackageOperation(goPackage, exeName);
		String buttonLabel = baseButtonLabel + " (run: `" + StringUtil.collToString(op.getCmdLine(), " ")  + "`)";
		
		Button getGocodeButton = SWTFactoryUtil.createButton(group, SWT.PUSH, buttonLabel, 
				GridDataFactory.fillDefaults().span(3, 0).create());
		
		getGocodeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = group.getShell();
				boolean success = UIOperationsHelper.runAndHandle(new ProgressMonitorDialog(shell), op, true, 
					baseButtonLabel + " error.");
				
				if(success) {
					textFieldEditor.setFieldValue(op.getGocodeExeLocation().toPathString());
				}
			}
		});
	}
	
	}
	
}