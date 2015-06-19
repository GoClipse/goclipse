/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.process;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.core.operations.ProcessStartInfo;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;

public class RunExternalProcessTask extends AbstractRunProcessTask {
	
	protected final IProject project;
	protected final ListenerListHelper<? extends IStartProcessListener> listenersList;
	
	public RunExternalProcessTask(ProcessBuilder pb, IProject project, ICancelMonitor cancelMonitor,
			ListenerListHelper<? extends IStartProcessListener> listenersList) {
		super(pb, cancelMonitor);
		this.project = project; // can be null
		this.listenersList = assertNotNull(listenersList);
	}
	
	@Override
	protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		for(IStartProcessListener processListener : listenersList.getListeners()) {
			handleProcessStartResult(processHelper, ce, processListener);
		}
	}
	
	protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce,
			IStartProcessListener processListener) {
		processListener.handleProcessStart(newStartProcessInfo(processHelper, ce));
	}
	
	protected ProcessStartInfo newStartProcessInfo(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		return new ProcessStartInfo(pb, project, ">> Running: ", false, processHelper, ce);
	}
	
}