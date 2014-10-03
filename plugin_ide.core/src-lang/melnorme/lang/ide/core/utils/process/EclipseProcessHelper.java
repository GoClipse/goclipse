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

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * An adapter to {@link ExternalProcessNotifyingHelper} that is customized to run in Eclipse. 
 * In particular:
 *  Allows using an {@link IProgressMonitor} as a cancel monitor.
 *  Log errors in Eclipse log.
 */
public class EclipseProcessHelper extends ExternalProcessNotifyingHelper {
	
	protected final IProgressMonitor monitor;
	
	public EclipseProcessHelper(Process process, boolean startReaders, IProgressMonitor monitor) {
		this(process, true, startReaders, monitor);
	}
	
	public EclipseProcessHelper(Process process, boolean readStdErr, boolean startReaders, IProgressMonitor monitor) {
		super(process, readStdErr, startReaders);
		this.monitor = monitor;
	}
	
	@Override
	protected boolean isCanceled() {
		return monitor.isCanceled();
	}
	
	@Override
	protected void handleListenerException(RuntimeException e) {
		LangCore.logError("Internal error notifying listener", e);
	}
	
}