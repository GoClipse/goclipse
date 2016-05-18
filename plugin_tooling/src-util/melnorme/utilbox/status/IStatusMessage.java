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

public interface IStatusMessage {
	
	Severity getSeverity();
	
	String getMessage();
	
	default boolean isHigherSeverity(IStatusMessage other) {
		return getSeverity().isHigherSeverity(other.getSeverity());
	}
	
	default StatusException toStatusException() {
		return new StatusException(getSeverity(), getMessage());
	}
	
}