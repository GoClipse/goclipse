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
	
	default void notifyMessage(StatusLevel statusLevel, String title, String message) {
		notifyMessage(null, statusLevel, title, message);
	}
	
	/** Report a message to the user. */
	void notifyMessage(String msgId, StatusLevel statusLevel, String title, String message);
	
	/* -----------------  ----------------- */
	
	public enum ProcessStartKind {
		BUILD,
		CHECK_BUILD,
		ENGINE_SERVER,
		ENGINE_TOOLS
	}
	
	default IOperationMonitor beginBuildOperation(boolean explicitConsoleNotify) {
		return beginOperation(ProcessStartKind.BUILD, true, explicitConsoleNotify);
	}
	
	default IOperationMonitor beginOperation(ProcessStartKind kind) {
		return beginOperation(kind, false, false);
	}
	
	IOperationMonitor beginOperation(ProcessStartKind kind, boolean clearConsole, boolean activateConsole);
	
	default IOperationMonitor beginOperation(StartOperationOptions options) {
		return beginOperation(options.kind, options.clearConsole, options.activateConsole);
	}
	
	public class StartOperationOptions {
		
		public ProcessStartKind kind;
		public boolean clearConsole;
		public boolean activateConsole;
		
		public StartOperationOptions(ProcessStartKind kind, boolean clearConsole, boolean activateConsole) {
			this.kind = kind;
			this.clearConsole = clearConsole;
			this.activateConsole = activateConsole;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * A UI monitor handling the view of an IDE operation. (normally it will be a console).
	 */
	public interface IOperationMonitor {
		
		void handleProcessStart(String prefixText, ProcessBuilder pb, ProcessStartHelper processStartHelper);
		
		void writeInfoMessage(String operationMessage);
		
		void activate();
		
	}
	
	public class NullOperationMonitor implements IOperationMonitor {
		@Override
		public void handleProcessStart(String prefixText, ProcessBuilder pb, ProcessStartHelper psh) {
		}
		
		@Override
		public void writeInfoMessage(String operationMessage) {
		}
		
		@Override
		public void activate() {
		}
	}
	
}