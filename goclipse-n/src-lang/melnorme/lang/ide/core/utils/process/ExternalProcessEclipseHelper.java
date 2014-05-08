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

import java.io.IOException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.misc.IByteSequence;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ExternalProcessEclipseHelper extends ExternalProcessNotifyingHelper {
	
	protected final IProgressMonitor monitor;
	
	public ExternalProcessEclipseHelper(ProcessBuilder pb, boolean startReaders, IProgressMonitor monitor)
			throws IOException {
		super(pb.start(), true, startReaders);
		this.monitor = assertNotNull(monitor);
	}
	
	@Override
	protected boolean isCanceled() {
		return monitor.isCanceled();
	}
	
	@Override
	protected void handleListenerException(RuntimeException e) {
		LangCore.logError("Internal error notifying listener", e);
	}
	
	public IByteSequence getStdOutBytes_CoreException() throws CoreException {
		try {
			return super.getStdOutBytes();
		} catch (IOException e) {
			throw LangCore.createCoreException("IO Error reading process stdout stream.", e);
		}
	}
	
	public IByteSequence getStdErrBytes_CoreException() throws CoreException {
		try {
			return super.getStdErrBytes();
		} catch (IOException e) {
			throw LangCore.createCoreException("IO Error reading process stderr stream.", e);
		}
	}
	
}