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

import melnorme.lang.tooling.data.IValidator;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IModelField;
import melnorme.utilbox.fields.IFieldValueListener;

/**
 * Field component with a field value that can be manipulated (get/set) even if the 
 * componented is not created.
 */
public abstract class FieldComponent<VALUE> extends AbstractDisableableWidget 
	implements IModelField<VALUE> {
	
	private final DomainField<VALUE> domainField;
	
	protected boolean listenersNeedNotify;
	protected boolean settingValueFromControl;
	
	public FieldComponent() {
		this(new DomainField<>(null));
	}
	
	public FieldComponent(VALUE defaultFieldValue) {
		this(new DomainField<>(defaultFieldValue));
	}
	
	public FieldComponent(DomainField<VALUE> domainField) {
		this.domainField = domainField;
		this.domainField.addListener(new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				if(!settingValueFromControl) {
					updateComponentFromInput();
				}
			}
		});
	}
	
	@Override
	public VALUE getFieldValue() {
		return domainField.getFieldValue();
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
		domainField.setFieldValue(newValue);
	}
	
	public void fireFieldValueChanged() {
		domainField.fireFieldValueChanged();
	}
	
	@Override
	public void addListener(IFieldValueListener listener) {
		domainField.addListener(listener);
	}
	
	@Override
	public void removeListener(IFieldValueListener listener) {
		domainField.removeListener(listener);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void doUpdateComponentFromInput2() {
		if(isCreated()) {
			doUpdateComponentFromValue();
		}
	}
	
	/** do Update component controls for given value.
	 * Precondition: component is created.*/
	protected abstract void doUpdateComponentFromValue();
	
	@Override
	protected abstract void createContents(Composite topControl);
	
	/* -----------------  ----------------- */
	
	public abstract Control getFieldControl();
	
	public boolean isCreated() {
		return SWTUtil.isOkToUse(getFieldControl());
	}
	
	/* -----------------  ----------------- */
	
	public void addFieldValidator(IValidator<VALUE, ?> validator) {
		validation.addFieldValidation(true, this, validator);
	}
	
}