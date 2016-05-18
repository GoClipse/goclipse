/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.FieldValueListener;
import melnorme.utilbox.fields.FieldValueListener.FieldChangeListener;
import melnorme.utilbox.fields.validation.IValidatableField;
import melnorme.utilbox.fields.validation.ValidationField;
import melnorme.utilbox.fields.validation.ValidationSource;
import melnorme.utilbox.fields.validation.Validator;
import melnorme.utilbox.fields.IField;

/**
 * Field component with a field value that can be manipulated (get/set) even if the 
 * componented is not created.
 */
public abstract class FieldWidget<VALUE> extends AbstractDisableableWidget 
	implements IField<VALUE>, IValidatableField<VALUE> {
	
	private final Field<VALUE> field;
	
	protected boolean listenersNeedNotify;
	protected boolean settingValueFromControl;
	
	public FieldWidget() {
		this(new Field<>(null));
	}
	
	public FieldWidget(VALUE defaultFieldValue) {
		this(new Field<>(defaultFieldValue));
	}
	
	public FieldWidget(Field<VALUE> domainField) {
		this.field = domainField;
		this.field.addChangeListener(new FieldChangeListener() {
			@Override
			public void fieldValueChanged() {
				if(!settingValueFromControl) {
					updateWidgetFromInput();
				}
			}
		});
	}
	
	public Field<VALUE> getField() {
		return field;
	}
	
	@Override
	public VALUE getFieldValue() {
		return field.getFieldValue();
	}
	
	@Override
	public void setFieldValue(VALUE value) {
		doSetFieldValue(value);
	}
	
	/** Update the field value from a control modification. */
	public void setFieldValueFromControl(VALUE newValue) {
		settingValueFromControl = true;
		try {
			doSetFieldValue(newValue);
		} finally {
			settingValueFromControl = false;
		}
	}
	
	protected void doSetFieldValue(VALUE newValue) {
		field.setFieldValue(newValue);
	}
	
	public void fireFieldValueChanged() {
		field.fireFieldValueChanged();
	}
	
	@Override
	public void addListener(FieldValueListener<? super VALUE> listener) {
		field.addListener(listener);
	}
	
	@Override
	public void removeListener(FieldValueListener<? super VALUE> listener) {
		field.removeListener(listener);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void doUpdateWidgetFromInput() {
		if(isCreated()) {
			doUpdateWidgetFromValue();
		}
	}
	
	/** do Update component controls for given value.
	 * Precondition: component is created.*/
	protected abstract void doUpdateWidgetFromValue();
	
	@Override
	protected abstract void createContents(Composite topControl);
	
	/* -----------------  ----------------- */
	
	public abstract Control getFieldControl();
	
	public boolean isCreated() {
		return SWTUtil.isOkToUse(getFieldControl());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public ValidationField getValidation() {
		return super.getValidation();
	}
	
	public void addFieldValidator(boolean init, Validator<VALUE, ?> validator) {
		getValidation().addFieldValidator(init, this, validator);
	}
	
	public void addFieldValidation(boolean init, ValidationSource validationSource) {
		getValidation().addFieldValidation(init, this, validationSource);
	}
	
	public void addFieldValidationX(boolean init, ValidationSourceX validationSource) {
		getValidation().addFieldValidation(init, this, validationSource);
	}
	
}