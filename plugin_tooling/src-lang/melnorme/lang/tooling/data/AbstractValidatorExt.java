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
package melnorme.lang.tooling.data;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

public class AbstractValidatorExt extends AbstractValidator2 {
	
	public AbstractValidatorExt() {
	}
	
	protected ValidationException createException(StatusLevel statusLevel, String message) {
		assertNotNull(statusLevel);
		return new ValidationException(statusLevel, getFullMessage(message), message, null);
	}
	
	protected String getFullMessage(String simpleMessage) {
		return simpleMessage;
	}
	
}