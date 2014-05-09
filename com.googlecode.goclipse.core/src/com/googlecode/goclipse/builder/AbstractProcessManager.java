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
package com.googlecode.goclipse.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.utils.process.IExternalProcessListener;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.misc.ListenerListHelper;

/**
 * {@link AbstractProcessManager} is basically a factory to create external process tasks
 * that are bound to notify the process listeneners that this manager manages.
 */
public class AbstractProcessManager<T extends IExternalProcessListener> {
	
	protected final ListenerListHelper<T> processListenersHelper = new ListenerListHelper<>();
	
	public void addBuildProcessListener(T processListener) {
		processListenersHelper.addListener(processListener);
	}
	
	public void removeBuildProcessListener(T processListener) {
		processListenersHelper.removeListener(processListener);
	}
	
	/* ----------------- ----------------- */
	
	public RunExternalProcessTask createRunProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor monitor) {
		return new RunExternalProcessTask(pb, project, monitor, processListenersHelper);
	}
	
}