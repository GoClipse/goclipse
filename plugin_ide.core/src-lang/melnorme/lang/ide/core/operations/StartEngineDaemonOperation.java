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

import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

public class StartEngineDaemonOperation extends AbstractRunProcessTask {
	
	protected final AbstractToolsManager abstractToolsManager;
	
	public StartEngineDaemonOperation(AbstractToolsManager abstractToolsManager, ProcessBuilder pb,
			ICancelMonitor cancelMonitor) {
		super(pb, cancelMonitor);
		this.abstractToolsManager = abstractToolsManager;
	}
	
	@Override
	protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		for (ILangOperationsListener listener : abstractToolsManager.getListeners()) {
			listener.engineDaemonStart(pb, ce, processHelper);
		}
	}
	
}