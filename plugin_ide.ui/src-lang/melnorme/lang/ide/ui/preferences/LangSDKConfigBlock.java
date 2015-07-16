/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
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

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.AbstractFieldComponent;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.DirectoryTextField;

public class LangSDKConfigBlock extends AbstractComponent {
	
	protected final LangSDKConfigBlock.LanguageSDKLocationGroup sdkLocationGroup = createSDKLocationGroup();
	
	public LangSDKConfigBlock() {
	}
	
	protected LanguageSDKLocationGroup createSDKLocationGroup() {
		return new LanguageSDKLocationGroup();
	}
	
	public AbstractFieldComponent<String> getLocationField() {
		return sdkLocationGroup.sdkLocation;
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		sdkLocationGroup.createComponent(topControl, gdFillDefaults().grab(true, false).create());
	}
	

	public static class LanguageSDKLocationGroup extends AbstractComponent {
		
		protected final ButtonTextField sdkLocation = createSdkLocationField();
		
		protected ButtonTextField createSdkLocationField() {
			return new DirectoryTextField(PreferencesMessages.ROOT_SDKGroup_path_Label);
		}
		
		@Override
		protected Composite doCreateTopLevelControl(Composite parent) {
			return SWTFactoryUtil.createGroup(parent, 
				PreferencesMessages.ROOT_SDKGroup_Label);
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
		protected void createContents(Composite topControl) {
			sdkLocation.createComponentInlined(topControl);
		}
		
		@Override
		public void updateComponentFromInput() {
		}
	}
	
	@Override
	public void updateComponentFromInput() {
	}
	
}