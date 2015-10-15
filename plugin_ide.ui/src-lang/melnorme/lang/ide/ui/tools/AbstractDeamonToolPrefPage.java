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
package melnorme.lang.ide.ui.tools;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.tooling.data.IValidatedField.ValidatedField;
import melnorme.lang.tooling.ops.util.LocationOrSinglePathValidator;
import melnorme.lang.tooling.ops.util.LocationValidator;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.FileTextField;

/* FIXME: test */
public abstract class AbstractDeamonToolPrefPage extends AbstractPreferencesBlockPrefPage {
	
	public AbstractDeamonToolPrefPage() {
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
		
		public ServerToolsBlock() {
			super();
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 1;
		}
		
		
		
	protected Group toolGroup;
	protected ButtonTextField daemonPathEditor;
	
	@Override
	protected void createContents(Composite topControl) {
		
		toolGroup = AbstractPreferencesBlock.createOptionsSection(topControl, 
			getDaemonToolName(), 
			3, 
			GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create());
		
		FieldComponent<Boolean> startServerAutomatically = new CheckBoxField(
			"Start " + getDaemonToolName() + " server automatically");
		bindToPreference(startServerAutomatically, ToolchainPreferences.AUTO_START_DAEMON);
		
		FieldComponent<Boolean> enableLogConsole = new CheckBoxField(
			"Enable " + getDaemonToolName() + " log console (requires restart)");
		bindToPreference(enableLogConsole, ToolchainPreferences.DAEMON_CONSOLE_ENABLE);
		
		startServerAutomatically.createComponentInlined(toolGroup);
		enableLogConsole.createComponentInlined(toolGroup);
		
		daemonPathEditor = createDaemonPathFieldEditor(toolGroup);
	}
	
	protected ButtonTextField createDaemonPathFieldEditor(Group group) {
		return createFileComponent(group, getDaemonToolName() + " path:", 
			ToolchainPreferences.DAEMON_PATH, true);
	}
	
	public FileTextField createFileComponent(Group group, String label, StringPreference pref, 
			boolean allowSinglePath) {
		FileTextField pathField = new FileTextField(label);
		
		PathValidator validator = (allowSinglePath ? 
				new LocationOrSinglePathValidator(label) : new LocationValidator(label)).setFileOnly(true);
		
		validation.addFieldValidation(false, pathField, new ValidatedField(pathField, validator));
		
		bindToPreference(pathField, pref);
		pathField.createComponentInlined(group);
		return pathField;
	}
	
	protected String getDaemonToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
	}
	
}