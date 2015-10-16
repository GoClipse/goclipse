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
package melnorme.util.swt.components.fields;


import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IProperty;
import melnorme.utilbox.misc.StringUtil;

public abstract class EnablementButtonTextField extends ButtonTextField {
	
	public static final String LABEL_UseDefault = "Use default:";
	
	protected final CheckBoxField useDefaultField;
	protected final boolean createUseDefaultComponent;
	
	public EnablementButtonTextField(String labelText, String useDefaultCheckboxLabel, String buttonlabel) {
		super(labelText, buttonlabel);
		
		this.useDefaultField = createUseDefaultField(useDefaultCheckboxLabel);
		this.createUseDefaultComponent = useDefaultCheckboxLabel != null;
		
		useDefaultField.setFieldValue(createUseDefaultComponent);
		useDefaultField.addListener(this::updateDefaultFieldValue);
	}
	
	protected CheckBoxField createUseDefaultField(String enablementCheckBoxLabel) {
		return new CheckBoxField(enablementCheckBoxLabel);
	}
	
	public CheckBoxField getUseDefaultField() {
		return useDefaultField;
	}
	
	public boolean isUseDefault() {
		return useDefaultField.getBooleanFieldValue();
	}
	
	public String getEffectiveFieldValue() {
		return isUseDefault() ? null : super.getFieldValue();
	}
	
	public void setEffectiveFieldValue(String effectiveFieldValue) {
		getUseDefaultField().setFieldValue(effectiveFieldValue == null);
		
		if(effectiveFieldValue != null) {
			setFieldValue(effectiveFieldValue);
		}
	}
	
	public void updateDefaultFieldValue() {
		if(isUseDefault()) {
			try {
				setFieldValue(getDefaultFieldValue());
			} catch(CommonException e) {
				// Do nothing;
			}
		}
	}
	
	protected final IProperty<String> effectiveValueProperty = new IProperty<String>() {
		
		@Override
		public String getValue() {
			return StringUtil.nullAsEmpty(getEffectiveFieldValue());
		}
		
		@Override
		public void setValue(String value) {
			setEffectiveFieldValue(StringUtil.emptyAsNull(value));
		}
	};
	
	public IProperty<String> asEffectiveValueProperty() {
		return effectiveValueProperty;
	}
	
	protected abstract String getDefaultFieldValue() throws CommonException;
	
	/* -----------------  ----------------- */
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, getLabelText(), SWT.NONE);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return glSwtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_EnablementCheckBox(topControl);
		super.createContents_all(topControl);
	}
	
	protected void createContents_EnablementCheckBox(Composite topControl) {
		if(createUseDefaultComponent) {
			Composite enablementTopControl = useDefaultField.createComponent(topControl);
			GridDataFactory.swtDefaults().span(getPreferredLayoutColumns(), 1).applyTo(enablementTopControl);
			useDefaultField.addListener(this::updateComponentFromInput);
		}
	}
	
	@Override
	protected void createContents_Label(Composite parent) {
		// Don't create
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled && !isUseDefault());
		useDefaultField.setEnabled(enabled);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void updateComponentFromInput() {
		super.updateComponentFromInput();
		super.setEnabled(!isUseDefault());
	}
	
}