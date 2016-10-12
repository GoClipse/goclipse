/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
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

public class StatusMessage implements IStatusMessage {
	
	protected final Severity severity;
	protected final String message;
	
	public StatusMessage(String message) {
		this(Severity.ERROR, message);
	}
	
	public StatusMessage(Severity severity, String message) {
		this.severity = assertNotNull(severity);
		this.message = assertNotNull(message);
	}
	
	@Override
	public Severity getSeverity() {
		return severity;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}