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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;

import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.lang.ide.ui.preferences.pages.DaemonToolPreferencePage;
import melnorme.lang.ide.ui.preferences.pages.DownloadToolTextField;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.TextFieldComponent;

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
		protected DownloadToolTextField oracleField;
		
		@Override
		protected void createContents(Composite topControl) {
			oracleGroup = AbstractPreferencesBlock.createOptionsSection(topControl, 
				"Go oracle",
				4,
				GridDataFactory.fillDefaults().grab(true, false).minSize(200, SWT.DEFAULT).create());
			
			
			oracleField = new DownloadToolTextField("Executable:", "Download...") {
				@Override
				protected BasicUIOperation getDownloadButtonHandler() {
					return new Start_GoInstallJob_Operation("Download oracle", this,
						"golang.org/x/tools/cmd/oracle",
						"oracle") {
					};
				}
			};
			createComponentAndBind(oracleGroup, "Go oracle path:", GoToolPreferences.GO_ORACLE_Path, true, oracleField);
			
			super.createContents(topControl);
			
			SWTFactoryUtil.createLabel(topControl, SWT.NONE, "Note: if you've made any changes "
					+ "in the Go SDK preferences, \n"
					+ "make sure you press \"Apply\" in that page before using `Download...` here.");
		}
		
		@Override
		protected String getDaemonToolName() {
			return LangUIPlugin_Actual.DAEMON_TOOL_Name;
		}
		
		@Override
		protected TextFieldComponent createDaemonPathFieldEditor(Group group) {
			group.setLayout(createDefaultOptionsSectionLayout(4));
			return super.createDaemonPathFieldEditor(group);
		}
		
		@Override
		protected TextFieldComponent doCreateDaemonPathFieldEditor(String label) {
			
			return new DownloadToolTextField("Executable:", "Download...") {
				@Override
				protected BasicUIOperation getDownloadButtonHandler() {
					return new Start_GoInstallJob_Operation("Download gocode", this,
						"github.com/nsf/gocode",
						"gocode") {
					};
				}
			};
		}
	
	}
	
}