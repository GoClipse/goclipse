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
package melnorme.lang.ide.ui.preferences.pages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.ide.ui.ContentAssistPreferences;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.tooling.data.IValidatableValue.ValidatableField;
import melnorme.lang.tooling.ops.util.LocationOrSinglePathValidator;
import melnorme.lang.tooling.ops.util.LocationValidator;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.util.swt.components.fields.TextFieldComponent;

public abstract class DaemonToolPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public DaemonToolPreferencePage() {
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected AbstractPreferencesBlock init_createPreferencesBlock() {
		return new ServerToolsBlock();
	}
	
	public static class ServerToolsBlock extends AbstractPreferencesBlock {
		
		protected final FieldComponent<Boolean> startServerAutomatically = new CheckBoxField(
			"Start " + getDaemonToolName() + " server automatically");
		protected final FieldComponent<Boolean> enableLogConsole = new CheckBoxField(
			"Enable " + getDaemonToolName() + " log console (requires restart)");
		
		protected final FieldComponent<Boolean> showErrorsDialog = new CheckBoxField(
			"Show error dialog if " + getDaemonToolName() + " failures occur during Content Assist");
		
		public ServerToolsBlock() {
			super();
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 1;
		}
		
		protected Group toolGroup;
		protected TextFieldComponent daemonPathEditor;
		
		@Override
		protected void createContents(Composite topControl) {
			
			toolGroup = AbstractPreferencesBlock.createOptionsSection(topControl, 
				getDaemonToolName(), 
				3, 
				createDefaultGroupGridData());
			
			bindToPreference(startServerAutomatically, ToolchainPreferences.AUTO_START_DAEMON);
			bindToPreference(enableLogConsole, ToolchainPreferences.DAEMON_CONSOLE_ENABLE);
			bindToPreference(showErrorsDialog, 
				ContentAssistPreferences.ShowDialogIfContentAssistErrors.getGlobalPreference());
			
			daemonPathEditor = createDaemonPathFieldEditor(toolGroup);
			
			startServerAutomatically.createComponentInlined(toolGroup);
			enableLogConsole.createComponentInlined(toolGroup);
			showErrorsDialog.createComponentInlined(toolGroup);
		}
		
		protected GridData createDefaultGroupGridData() {
			return GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create();
		}
		
		protected TextFieldComponent createDaemonPathFieldEditor(Group group) {
			String label = getDaemonToolName() + " path:";
			TextFieldComponent pathField = doCreateDaemonPathFieldEditor(label);
			
			createComponentAndBind(group, label, ToolchainPreferences.DAEMON_PATH, true, pathField);
			return pathField;
		}
		
		protected TextFieldComponent doCreateDaemonPathFieldEditor(String label) {
			return new FileTextField(label);
		}
		
		public void createComponentAndBind(Group group, String label, StringPreference pref,
				boolean allowSinglePath, TextFieldComponent pathField) {
			PathValidator validator = (allowSinglePath ? 
					new LocationOrSinglePathValidator(label) : new LocationValidator(label)).setFileOnly(true);
			
			validation.addFieldValidation(false, pathField, new ValidatableField<>(pathField, validator));
			
			bindToPreference(pathField, pref);
			pathField.createComponentInlined(group);
		}
		
		protected String getDaemonToolName() {
			return LangUIPlugin_Actual.DAEMON_TOOL_Name;
		}
	
	}
	
}