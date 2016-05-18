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
package melnorme.lang.utils.validators;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.fields.validation.ValidationException;
import melnorme.utilbox.status.Severity;

public class AbstractValidatorExt extends AbstractValidator {
	
	public AbstractValidatorExt() {
	}
	
	protected ValidationException createException(Severity severity, String message) {
		assertNotNull(severity);
		return new ValidationException(severity, getFullMessage(message), message, null);
	}
	
	protected String getFullMessage(String simpleMessage) {
		return simpleMessage;
	}
	
}