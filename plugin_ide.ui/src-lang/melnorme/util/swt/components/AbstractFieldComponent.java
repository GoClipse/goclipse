/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IDomainField;
import melnorme.utilbox.fields.IFieldValueListener;

/**
 * Field component with a field value that can be manipulated (get/set) even if the 
 * componented is not created.
 */
public abstract class AbstractFieldComponent<VALUE> extends AbstractComponent 
	implements IDomainField<VALUE> {
	
	private final DomainField<VALUE> domainField;
	
	protected boolean listenersNeedNotify;
	protected boolean settingValueFromControl;
	
	public AbstractFieldComponent() {
		this(new DomainField<>(null));
	}
	
	public AbstractFieldComponent(VALUE defaultFieldValue) {
		this(new DomainField<>(defaultFieldValue));
	}
	
	public AbstractFieldComponent(DomainField<VALUE> domainField) {
		this.domainField = domainField;
		this.domainField.addValueChangedListener(new IFieldValueListener() {
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
	
	@Override
	public void addValueChangedListener(IFieldValueListener listener) {
		domainField.addValueChangedListener(listener);
	}
	
	@Override
	public void removeValueChangedListener(IFieldValueListener listener) {
		domainField.removeValueChangedListener(listener);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void updateComponentFromInput() {
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
	
	protected abstract Control getFieldControl();
	
	public boolean isCreated() {
		return SWTUtil.isOkToUse(getFieldControl());
	}
	
	/* ----------------- create helpers ----------------- */
	
	public static Text createFieldText(AbstractFieldComponent<String> field, Composite parent, int style) {
		final Text text = new Text(parent, style);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				field.setFieldValueFromControl(text.getText());
			}
		});
		return text;
	}
	
}