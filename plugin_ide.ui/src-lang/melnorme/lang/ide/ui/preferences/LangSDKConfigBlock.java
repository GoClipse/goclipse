/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.DirectoryTextField;

public abstract class LangSDKConfigBlock extends AbstractCompositePreferencesBlock {
	
	public final LanguageSDKLocationGroup sdkLocationGroup;
	
	public LangSDKConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		this.sdkLocationGroup = init_createSDKLocationGroup();
		subComponents.add(sdkLocationGroup);
	}
	
	protected LanguageSDKLocationGroup init_createSDKLocationGroup() {
		return new LanguageSDKLocationGroup();
	}
	
	public FieldComponent<String> getLocationField() {
		return sdkLocationGroup.sdkLocationField;
	}
	
	protected abstract PathValidator getSDKValidator();
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	public class LanguageSDKLocationGroup extends AbstractCompositeWidget {
		
		public final ButtonTextField sdkLocationField = createSdkLocationField();
		
		public LanguageSDKLocationGroup() {
			initBindings();
			LangSDKConfigBlock.this.validation.addValidatableField(true, validation);
			
			this.subComponents.add(sdkLocationField);
		}
		
		protected void initBindings() {
			prefContext.bindToPreference(sdkLocationField, ToolchainPreferences.SDK_PATH2);
			validation.addFieldValidation(true, sdkLocationField, getSDKValidator());
		}
		
		protected ButtonTextField createSdkLocationField() {
			return new DirectoryTextField(PreferencesMessages.ROOT_SDKGroup_path_Label);
		}
		
		@Override
		protected Composite doCreateTopLevelControl(Composite parent) {
			return SWTFactoryUtil.createGroup(parent, PreferencesMessages.ROOT_SDKGroup_Label);
		}
		
		@Override
		protected GridLayoutFactory createTopLevelLayout() {
			return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 3;
		}
		
	}
	
}