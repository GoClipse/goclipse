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

import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

public class StartEngineDaemonOperation {
	
	protected final AbstractToolsManager<?> abstractToolsManager;
	protected final ProcessBuilder pb;
	
	public StartEngineDaemonOperation(AbstractToolsManager<?> abstractToolsManager, ProcessBuilder pb) {
		this.abstractToolsManager = abstractToolsManager;
		this.pb = pb;
	}
	
	public ExternalProcessNotifyingHelper call() throws CoreException {
		
		Process process;
		try {
			process = EclipseExternalProcessHelper.startProcess(pb);
		} catch (CoreException ce) {
			for (ILangOperationsListener listener : abstractToolsManager.getListeners()) {
				listener.engineDaemonFailedToStart(ce);
			}
			throw ce;
		}
		
		ExternalProcessNotifyingHelper processHelper = new ExternalProcessNotifyingHelper(process, true, false);
		
		for (ILangOperationsListener listener : abstractToolsManager.getListeners()) {
			listener.engineDaemonStarted(pb, processHelper);
		}
		
		processHelper.startReaderThreads();
		
		return processHelper;
	}
	
}