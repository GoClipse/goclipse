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
package melnorme.util.swt.components.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.lang.tooling.ops.util.NumberValidator;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IProperty;
import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

public class NumberField extends TextFieldExt {
	
	public NumberField(String label, int textLimit) {
		super(label, textLimit);
		
		getValidation().addFieldValidation(true, this, this::validatePositiveNumber);
	}
	
	public IStatusMessage validatePositiveNumber() {
		return validatePositiveNumber(getFieldValue());
	}
	
	public StatusException validatePositiveNumber(String number) {
		try {
			doValidatePositiveNumber(number);
			return null;
		} catch(CommonException ce) {
			return ce.toStatusException(Severity.ERROR);
		}
	}
	
	public void doValidatePositiveNumber(String number) throws CommonException {
		new NumberValidator().validateNonNegativeInteger(number);
	}
	
	protected final IProperty<Integer> intProperty = new IProperty<Integer>() {
		@Override
		public Integer get() {
			return new NumberValidator().getIntegerFrom(getFieldValue());
		}
		
		@Override
		public void set(Integer value) {
			assertTrue(value != null);
			setFieldValue(value.toString());
		}
	};
	
	public IProperty<Integer> asIntProperty() {
		return intProperty;
	}
	
	/** Override is required for reflection check of {@link IDisableableWidget} */
	@Override
	protected void doSetEnabled(boolean enabled) {
		super.doSetEnabled(enabled);
	}
	
}