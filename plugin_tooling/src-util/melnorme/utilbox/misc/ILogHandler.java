/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;


public interface ILogHandler {
	
	void logStatus(StatusException statusException);
	
	/** Logs an error status with given message. */
	default void logError(String message) {
		logError(message, null);
	}
	/** Logs an error status with given message and given throwable. */
	default void logError(String message, Throwable throwable) {
		logStatus(new StatusException(Severity.ERROR, message, throwable));
	}
	
	/** Logs a warning status with given message. */
	default void logWarning(String message) {
		logWarning(message, null);
	}
	/** Logs a warning status with given message and given throwable. */
	default void logWarning(String message, Throwable throwable) {
		logStatus(new StatusException(Severity.WARNING, message, throwable));
	}
	
	/** Logs an info status with given message. */
	default void logInfo(String message) {
		logStatus(new StatusException(Severity.INFO, message, null));
	}
	
}