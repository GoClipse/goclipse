/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import static melnorme.lang.ide.core.utils.TextMessageUtils.headerBIG;
import static melnorme.lang.ide.core.utils.TextMessageUtils.headerSMALL;

import org.eclipse.core.resources.IProject;

public interface ILangOperationsListener_Default {
	
	/** Report a message to the user. */
	void notifyMessage(StatusLevel statusLevel, String title, String message);
	
	void handleToolOperationStart(OperationInfo opInfo);
	
	void handleProcessStart(ProcessStartInfo processStartInfo, OperationInfo opInfo);
	
	
	default void handleBuildStarted(IProject project, boolean clearConsole) {
		String msg = (project == null) ?
			headerBIG("Building " + LangCore_Actual.LANGUAGE_NAME +" workspace") :
			headerSMALL(" Building " + LangCore_Actual.LANGUAGE_NAME + " project: " + project.getName());
		
		handleToolOperationStart(new OperationInfo(project, clearConsole, msg));
	}
	
	default void handleBuildTerminated(IProject project) {
		handleToolOperationStart(new OperationInfo(project, false, headerBIG("Build terminated.")));
	}
	
	void engineDaemonStart(ProcessBuilder pb, CommonException ce, ExternalProcessNotifyingHelper processHelper);
	
	void engineClientToolStart(ProcessBuilder pb, CommonException ce, ExternalProcessNotifyingHelper processHelper);
	
}