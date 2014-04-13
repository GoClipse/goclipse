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
package melnorme.lang.ide.ui.preferences.fields;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.preferences.FieldMessages;
import melnorme.lang.jdt.corext.util.Messages;

import org.eclipse.core.runtime.IStatus;

public class NumberConfigField extends TextConfigField {
	
	public NumberConfigField(String label, String prefKey, int textLimit) {
		super(label, prefKey, textLimit);
	}
	
	@Override
	protected void updateFieldValue(String numberText) {
		IStatus status = validatePositiveNumber(numberText);
		if(status.isOK()) {
			super.updateFieldValue(numberText);
		}
		statusChanged(status);
	}
	
	@SuppressWarnings("unused")
	protected void statusChanged(IStatus status) { 
	}
	
	protected IStatus validatePositiveNumber(String number) {
		if (number.length() == 0) {
			return LangCore.createErrorStatus(FieldMessages.JavaEditorPreferencePage_empty_input);
		}
		
		try {
			int value = Integer.parseInt(number);
			if(value >= 0) {
				return LangCore.createOkStatus(null);
			}
		} catch (NumberFormatException e) {
		}
		return LangCore.createErrorStatus(
			Messages.format(FieldMessages.JavaEditorPreferencePage_invalid_input, number));
	}
	
}