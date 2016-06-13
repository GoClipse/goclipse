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

public enum Severity {
	INFO,
	WARNING,
	ERROR,
	;
	
	public boolean isError() {
		return this == Severity.ERROR;
	}
	
	public boolean isHigherSeverity(Severity other) {
		return ordinal() > other.ordinal();
	}
	
	public boolean isHigherOrEqualSeverity(Severity other) {
		return ordinal() >= other.ordinal();
	}
	
	/* -----------------  ----------------- */
	
	public static Severity fromString(String severityString) throws CommonException {
		if(severityString == null) {
			return null;
		}
		
		switch (severityString.toUpperCase()) {
		case "WARNING": return WARNING;
		case "ERROR": return ERROR;
		case "INFO": return INFO;
		default:
			throw new CommonException("Invalid Severity string `" + severityString + "`.");
		}
	}
	
	public StatusLevel toStatusLevel() {
		switch (this) {
		case INFO: return StatusLevel.INFO;
		case WARNING: return StatusLevel.WARNING;
		case ERROR: return StatusLevel.ERROR;
		}
		throw assertFail();
	}
	
}