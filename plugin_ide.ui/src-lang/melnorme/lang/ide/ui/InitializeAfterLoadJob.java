/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;

import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;

public class InitializeAfterLoadJob extends UIJob {

	protected final LangUIPlugin langUIPlugin;
	
	public InitializeAfterLoadJob(LangUIPlugin langUIPlugin) {
		super(LangUIMessages.InitializeAfterLoadJob_starter_job_name);
		this.langUIPlugin = langUIPlugin;
		setSystem(true);
		setPriority(Job.SHORT);
	}
	
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			langUIPlugin.doInitializeAfterLoad(monitor);
		} catch(CoreException ce) {
			UIOperationsStatusHandler.handleInternalError("Error during UI initialization.", ce);
		}
		monitor.done();
		return Status.OK_STATUS;
	}
	
}