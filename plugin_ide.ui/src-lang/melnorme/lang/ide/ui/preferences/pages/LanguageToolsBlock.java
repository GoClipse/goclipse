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
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.AbstractCompositePreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.utils.validators.LocationOrSinglePathValidator;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.util.swt.components.AbstractGroupWidget;
import melnorme.util.swt.components.FieldWidget;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.FileTextField;

public class LanguageToolsBlock extends AbstractCompositePreferencesBlock {
	
	public LanguageToolsBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		addChildWidget(init_createEngineToolGroup());
	}
	
	protected EngineToolGroup init_createEngineToolGroup() {
		return new EngineToolGroup();
	}
	
	protected String getEngineToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
	public class EngineToolGroup extends AbstractGroupWidget {
		
		public final ButtonTextField toolLocationField;
		
		protected final FieldWidget<Boolean> startServerAutomatically = new CheckBoxField(
			"Start " + getEngineToolName() + " server automatically");
		
		public EngineToolGroup() {
			super(getEngineToolName() + ": ", 3);
			
			this.toolLocationField = initToolLocationField();
			addChildWidget(toolLocationField);
			if(toolLocationField instanceof DownloadToolTextField) {
				layoutColumns = 4;
			}
			
			PathValidator validator = (new LocationOrSinglePathValidator(getEngineToolName())).setFileOnly(true);
			toolLocationField.addFieldValidator(false, validator);
			
			prefContext.bindToPreference(toolLocationField, ToolchainPreferences.DAEMON_PATH);
			prefContext.bindToPreference(startServerAutomatically, ToolchainPreferences.AUTO_START_DAEMON);
			
			addChildWidget(startServerAutomatically);
		}
		
		protected ButtonTextField initToolLocationField() {
			return new FileTextField(getEngineToolName());
		}
		
	}
	
}