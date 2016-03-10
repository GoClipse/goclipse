/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.pages;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.ContentAssistPreferences;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.AbstractCompositePreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.tooling.ops.util.LocationOrSinglePathValidator;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.util.swt.components.AbstractGroupWidget;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.FileTextField;

public class LanguageToolsBlock extends AbstractCompositePreferencesBlock {
	
	public LanguageToolsBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		addSubComponent(init_createEngineToolGroup());
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	protected EngineToolGroup init_createEngineToolGroup() {
		return new EngineToolGroup();
	}
	
	protected String getEngineToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
	public class EngineToolGroup extends AbstractGroupWidget {
		
		public final ButtonTextField toolLocationField;
		
		protected final FieldComponent<Boolean> startServerAutomatically = new CheckBoxField(
			"Start " + getEngineToolName() + " server automatically");
		protected final FieldComponent<Boolean> showErrorsDialog = new CheckBoxField(
			"Show error dialog if " + getEngineToolName() + " failures occur during Content Assist");
		
		public EngineToolGroup() {
			super(getEngineToolName() + ": ", 3);
			
			this.toolLocationField = initToolLocationField();
			addSubComponent(toolLocationField);
			if(toolLocationField instanceof DownloadToolTextField) {
				layoutColumns = 4;
			}
			
			PathValidator validator = (new LocationOrSinglePathValidator(getEngineToolName())).setFileOnly(true);
			validation.addFieldValidation(false, toolLocationField, validator);
			
			prefContext.bindToPreference(toolLocationField, ToolchainPreferences.DAEMON_PATH);
			prefContext.bindToPreference(startServerAutomatically, ToolchainPreferences.AUTO_START_DAEMON);
			prefContext.bindToPreference(showErrorsDialog, ContentAssistPreferences.ShowDialogIfContentAssistErrors
				.getGlobalPreference());
			
			addSubComponent(startServerAutomatically);
			addSubComponent(showErrorsDialog);
		}
		
		protected ButtonTextField initToolLocationField() {
			return new FileTextField(getEngineToolName());
		}
		
	}
	
}