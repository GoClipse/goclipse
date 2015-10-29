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
package melnorme.utilbox.core;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.text.MessageFormat;

import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;

/**
 * A generic status exception.
 * Has an associated message, and optionally an associated Exception cause.
 */
public class CommonException extends Exception {
	
	public static CommonException fromMsgFormat(String pattern, Object... arguments) {
		return new CommonException(MessageFormat.format(pattern, arguments));
	}
	
	/* -----------------  ----------------- */
	
	private static final long serialVersionUID = -7324639626503261646L;
	
	public CommonException(String message) {
		this(assertNotNull(message), null);
	}
	
	public CommonException(String message, Throwable cause) {
		super(assertNotNull(message), cause);
	}
	
	public StatusException toStatusException(StatusLevel statusLevel) {
		if(this instanceof StatusException) {
			return (StatusException) this;
		} else {
			return new StatusException(statusLevel, getMessage(), getCause());
		}
	}
	
}