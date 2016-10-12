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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.utilbox.core.CommonException;

@SuppressWarnings("serial")
public class StatusException extends CommonException implements IStatusMessage {
	
	protected final Severity severity;
	
	public StatusException(String message) {
		this(Severity.ERROR, message, null);
	}
	
	public StatusException(Severity severity, String message) {
		this(severity, message, null);
	}
	
	public StatusException(Severity severity, String message, Throwable cause) {
		super(message, cause);
		this.severity = assertNotNull(severity);
	}
	
	@Override
	public Severity getSeverity() {
		return severity;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	@Override
	public StatusException toStatusException() {
		return this;
	}
	
	@Override
	public StatusException toStatusException(Severity severity) {
		return this;
	}
	
	public static StatusException toStatusException(IStatusMessage status) {
		if(status instanceof StatusException) {
			return (StatusException) status;
		}
		return new StatusException(status.getSeverity(), status.getMessage());
	}
	
}