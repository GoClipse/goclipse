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
package melnorme.lang.ide.core.utils.process;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.List;

import melnorme.utilbox.misc.ListenerListHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

public class RunExternalProcessTask extends AbstractRunExternalProcessTask {
	
	protected final ListenerListHelper<? extends IExternalProcessListener> listenersList;
	
	public RunExternalProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor,
			ListenerListHelper<? extends IExternalProcessListener> listenersList) {
		super(pb, project, cancelMonitor);
		this.listenersList = assertNotNull(listenersList);
	}
	
	@Override
	protected List<? extends IExternalProcessListener> getListeners() {
		return listenersList.getListeners();
	}
	
}