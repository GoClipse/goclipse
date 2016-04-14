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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IProperty;

/**
 * Second version of EnablementButtonTextField2, 
 * using AbstractCompositeWidget as base instead
 */
public abstract class EnablementButtonTextField2 extends AbstractCompositeWidget {
	
	public static final String LABEL_UseDefault = "Use default value.";
	
	protected final String label;
	protected final String useDefaultCheckboxLabel;
	
	protected final CheckBoxField useDefaultField;
	protected final ButtonTextField buttonTextField;
	
	public EnablementButtonTextField2(String label) {
		this(label, LABEL_UseDefault);
	}
	
	public EnablementButtonTextField2(String label, String useDefaultCheckboxLabel) {
		super(true);
		
		this.label = assertNotNull(label);
		this.useDefaultCheckboxLabel = assertNotNull(useDefaultCheckboxLabel);
		
		this.useDefaultField = createUseDefaultField(useDefaultCheckboxLabel);
		useDefaultField.setFieldValue(true);
		useDefaultField.addListener(this::updateWidgetFromInput);
		addSubComponent(useDefaultField);
		
		this.buttonTextField = init_createButtonTextField();
		addSubComponent(buttonTextField);
	}
	
	protected CheckBoxField createUseDefaultField(String enablementCheckBoxLabel) {
		return new CheckBoxField(enablementCheckBoxLabel);
	}
	
	protected abstract ButtonTextField init_createButtonTextField();
	
	public ButtonTextField getButtonTextField() {
		return buttonTextField;
	}
	
	public CheckBoxField getUseDefaultField() {
		return useDefaultField;
	}
	
	public boolean isUseDefault() {
		return useDefaultField.getBooleanFieldValue();
	}
	
	public String getEffectiveFieldValue() {
		return isUseDefault() ? null : buttonTextField.getFieldValue();
	}
	
	public void setEffectiveFieldValue(String effectiveFieldValue) {
		getUseDefaultField().setFieldValue(effectiveFieldValue == null);
		
		if(effectiveFieldValue != null) {
			buttonTextField.setFieldValue(effectiveFieldValue);
		}
	}
	
	@Override
	protected void doUpdateWidgetFromInput() {
		if(isUseDefault()) {
			String defaultFieldValue;
			try {
				defaultFieldValue = getDefaultFieldValue();
			} catch(CommonException e) {
				defaultFieldValue = "";
			}
			buttonTextField.setFieldValue(defaultFieldValue);
		}
		buttonTextField.setEnabled(!isUseDefault());
	}
	
	protected final IProperty<String> effectiveValueProperty = new IProperty<String>() {
		@Override
		public String get() {
			return getEffectiveFieldValue();
		}
		
		@Override
		public void set(String value) {
			setEffectiveFieldValue(value);
		}
	};
	
	public IProperty<String> asEffectiveValueProperty2() {
		return effectiveValueProperty;
	}
	
	protected abstract String getDefaultFieldValue() throws CommonException;
	
	/* -----------------  ----------------- */
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, label, SWT.NONE);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return glSwtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 3;
	}
	
}