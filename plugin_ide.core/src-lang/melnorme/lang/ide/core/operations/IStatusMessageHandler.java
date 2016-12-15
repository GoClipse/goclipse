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
package melnorme.lang.ide.core.operations;

import melnorme.utilbox.status.StatusLevel;

public interface IStatusMessageHandler {
	
	/** Report a message to the user. */
	void notifyMessage(String msgId, StatusLevel statusLevel, String title, String message);
	
	default void notifyMessage(StatusLevel statusLevel, String title, String message) {
		notifyMessage(null, statusLevel, title, message);
	}
	
}