/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.ops.util;

import melnorme.utilbox.core.CommonException;

public class NumberValidator {

	public int validateNonNegativeInteger(String numberString) throws CommonException {
		int intValue = validateInteger(numberString);
		if(intValue < 0) {
			throw CommonException.fromMsgFormat(ValidationMessages.Number_IsNegative, intValue);
		}
		return intValue;
	}
	
	public int validateInteger(String numberString) throws CommonException {
		if(numberString.length() == 0) {
			throw new CommonException(ValidationMessages.NumberField_empty_input);
		}
		
		try {
			return Integer.parseInt(numberString);
		} catch(NumberFormatException e) {
			throw CommonException.fromMsgFormat(ValidationMessages.NumberField_invalid_input2, numberString);
		}
	}
	
	public int getIntegerFrom(String numberString) {
		try {
			return validateInteger(numberString);
		} catch(CommonException e) {
			return 0;
		}
	}
	
}