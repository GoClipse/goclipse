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
package melnorme.util.swt.components.fields;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.IProperty;

public abstract class EnablementCompositeWidget<VALUE> extends CompositeWidget {
	
	public static final String LABEL_UseDefault = "Use default value.";
	
	protected final Field<VALUE> valueField = init_valueField();
	
	protected String label;
	
	protected final CheckBoxField enablementField;
	
	public EnablementCompositeWidget(String label, String useDefaultCheckboxLabel) {
		super(true);
		
		this.label = assertNotNull(label);
		assertNotNull(useDefaultCheckboxLabel);
		
		this.enablementField = init_createUseDefaultField(useDefaultCheckboxLabel);
		enablementField.setFieldValue(true);
		enablementField.addChangeListener(this::handleEnablementChanged);
		addChildWidget(enablementField);
		
		valueField.addChangeListener(this::updateWidgetFromInput);
	}
	
	protected Field<VALUE> init_valueField() {
		return new Field<>();
	} 
	
	protected CheckBoxField init_createUseDefaultField(String enablementCheckBoxLabel) {
		return new CheckBoxField(enablementCheckBoxLabel);
	}
	
	public CheckBoxField getEnablementField() {
		return enablementField;
	}
	
	public boolean isUseDefault() {
		return enablementField.getBooleanFieldValue();
	}
	
	protected Field<VALUE> getField() {
		return valueField;
	}
	
	/* -----------------  ----------------- */
	
	protected void handleEnablementChanged() {
		getValidation().setExplicitStatus(null);
		if(isUseDefault()) {
			VALUE defaultFieldValue;
			try {
				defaultFieldValue = getDefaultFieldValue();
			} catch(CommonException e) {
				defaultFieldValue = null;
				getValidation().setExplicitStatus(e.toStatusException());
			}
			getField().setFieldValue(defaultFieldValue);
		}
		updateChildrenEnablement();
	}
	
	protected void updateChildrenEnablement() {
		for (IDisableableWidget childWidget : getChildWidgets()) {
			if(childWidget == enablementField) {
				continue;
			}
			childWidget.setEnabled(!isUseDefault());
		}
	}
	
	protected abstract VALUE getDefaultFieldValue() throws CommonException;
	
	/* ----------------- effective value ----------------- */
	
	public VALUE getEffectiveFieldValue() {
		return isUseDefault() ? null : getField().getFieldValue();
	}
	
	public void setEffectiveFieldValue(VALUE effectiveFieldValue) {
		getEnablementField().setFieldValue(effectiveFieldValue == null);
		
		if(effectiveFieldValue != null) {
			getField().setFieldValue(effectiveFieldValue);
		}
	}
	
	protected final IProperty<VALUE> effectiveValueProperty = new IProperty<VALUE>() {
		@Override
		public VALUE get() {
			return getEffectiveFieldValue();
		}
		
		@Override
		public void set(VALUE value) {
			setEffectiveFieldValue(value);
		}
	};
	
	public IProperty<VALUE> asEffectiveValueProperty() {
		return effectiveValueProperty;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, label, SWT.NONE);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
}