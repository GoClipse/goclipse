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

import static melnorme.utilbox.core.CoreUtil.list;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.utilbox.collections.Indexable;

public abstract class LangSDKConfigBlock extends AbstractPreferencesBlockExt {
	
	public final LanguageSDKLocationGroup sdkLocationGroup;
	
	public LangSDKConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		this.sdkLocationGroup = init_createSDKLocationGroup();
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
	
	@Override
	protected void createContents(Composite topControl) {
		sdkLocationGroup.createComponent(topControl, gdFillDefaults().grab(true, false).create());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		sdkLocationGroup.setEnabled(enabled);
	}
	
	public class LanguageSDKLocationGroup extends AbstractCompositeWidget {
		
		public final ButtonTextField sdkLocationField = createSdkLocationField();
		
		public LanguageSDKLocationGroup() {
			initBindings();
		}
		
		protected void initBindings() {
			bindToPreference(sdkLocationField, ToolchainPreferences.SDK_PATH2);
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
		
		@Override
		protected Indexable<IDisableableWidget> getSubWidgets() {
			return list(sdkLocationField);
		}
		
	}
	
}