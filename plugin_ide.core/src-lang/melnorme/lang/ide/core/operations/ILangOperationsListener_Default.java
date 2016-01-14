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
	
	/* -----------------  ----------------- */
	
	public enum ProcessStartKind {
		BUILD,
		ENGINE_SERVER,
		ENGINE_TOOLS
	}
	
	default IOperationConsoleHandler beginBuildOperation(boolean explicitConsoleNotify) {
		return beginOperation(ProcessStartKind.BUILD, true, explicitConsoleNotify);
	}
	
	default IOperationConsoleHandler beginOperation(ProcessStartKind kind) {
		return beginOperation(kind, false, false);
	}
	
	IOperationConsoleHandler beginOperation(ProcessStartKind kind, boolean clearConsole, boolean activateConsole);
	
	public interface IOperationConsoleHandler {
		
		void handleProcessStart(String prefixText, ProcessBuilder pb, ProcessStartHelper processStartHelper);
		
		void writeInfoMessage(String operationMessage);
		
		void activate();
		
	}
	
	/* -----------------  ----------------- */
	
	default void engineDaemonStart(ProcessBuilder pb, ProcessStartHelper psh) {
		beginOperation(ProcessStartKind.ENGINE_SERVER).handleProcessStart(null, pb, psh);
	}
	
	default void engineClientToolStart(ProcessBuilder pb, ProcessStartHelper psh) {
		beginOperation(ProcessStartKind.ENGINE_TOOLS).handleProcessStart(null, pb, psh);
	}
	
}