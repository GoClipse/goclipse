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

import java.text.MessageFormat;

import melnorme.lang.ide.core.LangCore;

import org.eclipse.core.runtime.IStatus;

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
	
	@SuppressWarnings("unused")
	protected void statusChanged(IStatus status) { 
	}
	
	protected IStatus validatePositiveNumber(String number) {
		if (number.length() == 0) {
			return LangCore.createErrorStatus(FieldMessages.NumberField_empty_input);
		}
		
		try {
			int value = Integer.parseInt(number);
			if(value >= 0) {
				return LangCore.createOkStatus(null);
			}
		} catch (NumberFormatException e) {
		}
		return LangCore.createErrorStatus(
			MessageFormat.format(FieldMessages.NumberField_invalid_input, number));
	}
	
}