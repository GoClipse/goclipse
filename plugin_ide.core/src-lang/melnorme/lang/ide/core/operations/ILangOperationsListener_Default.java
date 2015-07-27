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
package melnorme.lang.ide.core.operations;

import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

public interface ILangOperationsListener_Default {
	
	/** Report a message to the user. */
	void notifyMessage(StatusLevel statusLevel, String title, String message);
	
	void handleNewOperation(OperationInfo opInfo);
	
	void handleMessage(MessageEventInfo messageInfo);
	
	void handleProcessStart(ProcessStartInfo processStartInfo);
	
	void engineDaemonStart(ProcessBuilder pb, CommonException ce, ExternalProcessNotifyingHelper processHelper);
	
	void engineClientToolStart(ProcessBuilder pb, CommonException ce, ExternalProcessNotifyingHelper processHelper);
	
}