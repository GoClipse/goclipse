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

/**
 * {@link ValidationException}'s full message is expected to reference the field that was being validated,
 * as well as contain the simple message (a message strictly about what is went wrong in the validation).
 */
@SuppressWarnings("serial")
public class ValidationException extends StatusException {
	
	protected final String simpleMessage;
	
	public ValidationException(StatusLevel statusLevel, String message, String simpleMessage, Throwable cause) {
		super(statusLevel, message, cause);
		this.simpleMessage = assertNotNull(simpleMessage);
	}
	
	public String getSimpleMessage() {
		return simpleMessage;
	}
	
	public String getContextualizedMessage() {
		return getMessage();
	}
	
}