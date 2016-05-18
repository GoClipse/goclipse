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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.DirectoryTextField;

public abstract class LangSDKConfigBlock extends AbstractCompositePreferencesBlock {
	
	protected LanguageSDKLocationGroup sdkLocationGroup;
	
	public LangSDKConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		this.sdkLocationGroup = init_createSDKLocationGroup();
		addChildWidget(sdkLocationGroup);
	}
	
	protected LanguageSDKLocationGroup init_createSDKLocationGroup() {
		return new LanguageSDKLocationGroup();
	}
	
	protected abstract PathValidator getSDKValidator();
	
	public class LanguageSDKLocationGroup extends CompositeWidget {
		
		public final ButtonTextField sdkLocationField = createSdkLocationField();
		
		public LanguageSDKLocationGroup() {
			super(true);
			this.layoutColumns = 3;
			this.addChildWidget(sdkLocationField);
			
			initBindings();
		}
		
		protected void initBindings() {
			prefContext.bindToPreference(sdkLocationField, LangCore.settings().SDK_LOCATION);
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
		
	}
	
}