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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IDomainField;
import melnorme.utilbox.fields.IFieldValueListener;

/**
 * Field component with a field value that can be manipulated (get/set) even if the 
 * componented is not created.
 */
public abstract class AbstractFieldComponent<VALUE> extends AbstractComponentExt 
	implements IDomainField<VALUE>, IWidgetComponent {
	
	private final DomainField<VALUE> domainField = new DomainField<>();
	
	protected boolean listenersNeedNotify;
	protected boolean settingValueFromControl;
	
	public AbstractFieldComponent(VALUE defaultFieldValue) {
		domainField.setFieldValue(defaultFieldValue);
		domainField.addValueChangedListener(new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				if(!settingValueFromControl) {
					updateComponentFromInput();
				}
			}
		});
	}
	
	public abstract VALUE getDefaultFieldValue();
	
	@Override
	public VALUE getFieldValue() {
		return domainField.getFieldValue();
	}
	
	@Override
	public void setFieldValue(VALUE value) {
		if(value == null) {
			value = getDefaultFieldValue();
		}
		doSetFieldValue(value);
	}
	
	/** Update the field value from a control modification. */
	protected void setFieldValueFromControl(VALUE newValue) {
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
	
	protected static Text createFieldText(final AbstractFieldComponent<String> field, Composite parent, int style) {
		final Text text = new Text(parent, style);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				field.setFieldValueFromControl(text.getText());
			}
		});
		return text;
	}
	
	protected static Button createFieldCheckbox(final AbstractFieldComponent<Boolean> field, Composite parent, 
			int style) {
		final Button checkBox = new Button(parent, SWT.CHECK | style);
		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				field.setFieldValueFromControl(checkBox.getSelection());
			}
		});
		return checkBox;
	}
	
	protected static Spinner createFieldSpinner(final AbstractFieldComponent<Integer> field, Composite parent, 
			int style) {
		final Spinner spinner = new Spinner(parent, style);
		spinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				field.setFieldValueFromControl(spinner.getSelection());
			}
		});
		return spinner;
	}
	
	protected static Combo createFieldCombo(final AbstractFieldComponent<Integer> field, Composite topControl, int style) {
		final Combo combo = new Combo(topControl, style);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = combo.getSelectionIndex();
				field.setFieldValueFromControl(selectionIndex);
			}
		});
		return combo;
	}
	
}