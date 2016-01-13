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

import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask.ProcessStartHelper;
import melnorme.lang.tooling.data.StatusLevel;

public interface ILangOperationsListener_Default {
	
	/** Report a message to the user. */
	void notifyMessage(StatusLevel statusLevel, String title, String message);
	
	void handleStartBuildOperation(OperationInfo opInfo);
	
	void handleMessage(MessageEventInfo messageInfo);
	
	public enum ProcessStartKind {
		BUILD,
		ENGINE_SERVER,
		ENGINE_TOOLS
	}
	
	// TODO: Need to refactor out these two methods into something more generic.
	
	default void engineDaemonStart(ProcessBuilder pb, ProcessStartHelper psh) {
		handleProcessStart(ProcessStartKind.ENGINE_SERVER, pb, psh);
	}
	
	default void engineClientToolStart(ProcessBuilder pb, ProcessStartHelper psh) {
		handleProcessStart(ProcessStartKind.ENGINE_TOOLS, pb, psh);
	}
	
	default void handleProcessStart(ProcessStartKind kind, ProcessBuilder pb, ProcessStartHelper psh) {
		ILangOperationConsoleHandler operationUIHandler = getOperationUIHandler(kind, null);
		operationUIHandler.handleProcessStart(null, pb, psh);
	}
	
	ILangOperationConsoleHandler getOperationUIHandler(ProcessStartKind kind, OperationInfo opInfo);
	
	public interface ILangOperationConsoleHandler {
		
		void handleProcessStart(String prefixText, ProcessBuilder pb, ProcessStartHelper processStartHelper);
		
	}
	
}