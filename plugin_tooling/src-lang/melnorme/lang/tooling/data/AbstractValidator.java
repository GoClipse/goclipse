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

import java.text.MessageFormat;

public class AbstractValidator {
	
	public AbstractValidator() {
		super();
	}
	
	protected StatusException errorMsg(String messagePattern, Object... arguments) throws StatusException {
		throw error(MessageFormat.format(messagePattern, arguments), null);
	}
	
	protected StatusException error(String message) throws StatusException {
		throw error(message, null);
	}
	
	protected StatusException error(String message, Throwable exception) throws StatusException {
		throw new StatusException(StatusLevel.ERROR, message, exception);
	}
	
}