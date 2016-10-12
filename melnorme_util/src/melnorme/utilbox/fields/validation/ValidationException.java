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
package melnorme.utilbox.fields.validation;

import static melnorme.utilbox.core.CoreUtil.option;

import java.util.Optional;

import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

/**
 * {@link ValidationException}'s full message is expected to reference the field that was being validated,
 * as well as contain the simple message (a message strictly about what is went wrong in the validation).
 */
@SuppressWarnings("serial")
public class ValidationException extends StatusException implements IDetailsMessage {
	
	protected final String detailsMessage;
	
	public ValidationException(Severity severity, String message) {
		this(severity, message, null);
	}
	
	public ValidationException(Severity severity, String message, String detailsMessage) {
		super(severity, message, null);
		this.detailsMessage = detailsMessage;
	}
	
	@Override
	public Optional<String> getDetailsMessage2() {
		return option(detailsMessage);
	}
	
}