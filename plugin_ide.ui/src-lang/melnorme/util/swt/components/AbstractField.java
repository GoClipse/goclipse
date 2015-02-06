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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.util.swt.SWTUtil;

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

/**
 * Field component with a field value that can be manipulated (get/set) even if the 
 * componented is not created.
 */
public abstract class AbstractField<VALUE> extends CommonFieldComponent<VALUE> {
	
	private VALUE value; // private to prevent direct modifications.
	protected boolean listenersNeedNotify;
	
	public AbstractField() {
		this.value = assertNotNull(getDefaultFieldValue());
	}
	
	public AbstractField(VALUE defaultFieldValue) {
		this.value = assertNotNull(defaultFieldValue);
	}
	
	public abstract VALUE getDefaultFieldValue();
	
	@Override
	public VALUE getFieldValue() {
		return assertNotNull(value);
	}
	
	@Override
	public void setFieldValue(VALUE value) {
		if(value == null) {
			value = getDefaultFieldValue();
		}
		doSetFieldValue(value, true);
	}
	
	/** Update the field value from a control modification. */
	protected void setFieldValueFromControl(VALUE newValue) {
		doSetFieldValue(newValue, false);
	}
	
	protected void doSetFieldValue(VALUE newValue, boolean needsUpdateControls) {
		this.value = newValue;
		this.listenersNeedNotify = true;
		
		if(needsUpdateControls) {
			updateComponentFromInput();
		}
		if(listenersNeedNotify) {
			fireFieldValueChanged();
		}
		listenersNeedNotify = false;
	}
	
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
	
	protected static Text createFieldText(final AbstractField<String> field, Composite parent, int style) {
		final Text text = new Text(parent, style);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				field.setFieldValueFromControl(text.getText());
			}
		});
		return text;
	}
	
	protected static Button createFieldCheckbox(final AbstractField<Boolean> field, Composite parent, 
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
	
	protected static Spinner createFieldSpinner(final AbstractField<Integer> field, Composite parent, 
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
	
	protected static Combo createFieldCombo(final AbstractField<Integer> field, Composite topControl, int style) {
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