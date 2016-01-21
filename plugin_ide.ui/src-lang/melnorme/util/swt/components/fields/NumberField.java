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

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.IValidatableField;
import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.ops.util.NumberValidator;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IProperty;

public class NumberField extends TextFieldExt implements IValidatableField<String> {
	
	public NumberField(String label, int textLimit) {
		super(label, textLimit);
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		return validatePositiveNumber2(getFieldValue());
	}
	
	public StatusException validatePositiveNumber2(String number) {
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
		public Integer getValue() {
			return new NumberValidator().getIntegerFrom(getFieldValue());
		}
		
		@Override
		public void setValue(Integer value) {
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