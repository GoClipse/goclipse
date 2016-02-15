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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.core.GoToolPreferences;

import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.AbstractToolLocationGroup;
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
		
		protected final AbstractToolLocationGroup oracleGroup = new AbstractToolLocationGroup("oracle") {
			{
				bindToDerivedPreference(toolLocationField, GoToolPreferences.GO_ORACLE_Path);
			}
			
			@Override
			protected BasicUIOperation do_getDownloadButtonHandler(DownloadToolTextField downloadToolTextField) {
				return new Start_GoInstallJob_Operation("Download oracle", "Downloading oracle...", 
					downloadToolTextField,
					"golang.org/x/tools/cmd/oracle",
					"oracle") {
				};
			}
		};
		protected final AbstractToolLocationGroup godefGroup = new AbstractToolLocationGroup("godef") {
			{
				bindToDerivedPreference(toolLocationField, GoToolPreferences.GODEF_Path);
			}
			
			@Override
			protected BasicUIOperation do_getDownloadButtonHandler(DownloadToolTextField downloadToolTextField) {
				return new Start_GoInstallJob_Operation("Download godef", "Downloading godef...", 
					downloadToolTextField,
					"github.com/rogpeppe/godef",
					"godef") {
				};
			}
		};
		
		@Override
		protected void createContents(Composite topControl) {
			
			validation.addValidatableField(true, oracleGroup.getStatusField());
			validation.addValidatableField(true, godefGroup.getStatusField());
			
			oracleGroup.createComponent(topControl, createDefaultGroupGridData());
			super.createContents(topControl);
			godefGroup.createComponent(topControl, createDefaultGroupGridData());
			
			SWTFactoryUtil.createLabel(topControl, SWT.NONE, "Note: if you've made any changes "
					+ "in the Go SDK preferences, \n"
					+ "make sure you press \"Apply\" in that page before using the `Download...` button.");
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
					return new Start_GoInstallJob_Operation("Download gocode", "Downloading gocode...", this,
						"github.com/nsf/gocode",
						"gocode") {
					};
				}
			};
		}
	
	}
	
}