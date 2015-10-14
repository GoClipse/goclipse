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

import org.eclipse.core.runtime.IStatus;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.ops.util.NumberValidator;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IProperty;

public class NumberField extends TextField2 {
	
	public NumberField(String label, int textLimit) {
		super(label, textLimit);
	}
	
	@Override
	protected void doSetFieldValue(String newValue) {
		IStatus status = validatePositiveNumber(newValue);
		if(status.isOK()) {
			super.doSetFieldValue(newValue);
		}
		statusChanged(status);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}
	
	@SuppressWarnings("unused")
	protected void statusChanged(IStatus status) { 
	}
	
	protected IStatus validatePositiveNumber(String number) {
		try {
			new NumberValidator().validateNonNegativeInteger(number);
			return LangCore.createOkStatus(null);
		} catch(CommonException ce) {
			return LangCore.createCoreException(ce).getStatus();
		}
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
	
}