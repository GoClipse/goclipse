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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import melnorme.utilbox.core.CommonException;

public enum Severity {
	INFO,
	WARNING,
	ERROR,
	;
	
	public static Severity fromString(String messageTypeString) throws CommonException {
		if(messageTypeString == null) {
			return null;
		}
		
		switch (messageTypeString.toUpperCase()) {
		case "WARNING": return WARNING;
		case "ERROR": return ERROR;
		case "INFO": return INFO;
		default:
			throw new CommonException("Invalid Status: " + messageTypeString);
		}
	}
	
	public StatusLevel toStatusLevel() {
		switch (this) {
		case INFO: return StatusLevel.INFO;
		case WARNING: return StatusLevel.WARNING;
		case ERROR: return StatusLevel.INFO;
		}
		throw assertFail();
	}
	
}