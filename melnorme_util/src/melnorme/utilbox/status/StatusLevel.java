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
package melnorme.utilbox.status;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import melnorme.utilbox.core.CommonException;

public enum StatusLevel {
	OK,
	INFO,
	WARNING,
	ERROR,
	;
	
	public boolean isOkStatus() {
		return this == OK;
	}
	
	public Severity toSeverity() {
		switch (this) {
		case OK:
		case INFO: return Severity.INFO;
		case WARNING: return Severity.WARNING;
		case ERROR: return Severity.ERROR;
		}
		throw assertFail();
	}
	
	/* -----------------  ----------------- */
	
	public static StatusLevel fromString(String messageTypeString) throws CommonException {
		if(messageTypeString == null) {
			return null;
		}
		
		switch (messageTypeString.toLowerCase()) {
		case "warning": return WARNING;
		case "error": return ERROR;
		case "info": return INFO;
		case "ok": return INFO;
		default:
			throw new CommonException("Invalid Status: " + messageTypeString);
		}
	}
}