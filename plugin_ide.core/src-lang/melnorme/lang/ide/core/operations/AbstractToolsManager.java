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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

/**
 * Abstract class for running external tools and notifying interested listeners (normally the UI only).
 */
public abstract class AbstractToolsManager<LISTENER extends ILangOperationsListener> 
		extends ListenerListHelper<LISTENER> {
	
	public ProcessBuilder createDefaultProcessBuilder(List<String> commandLine) {
		return createDefaultProcessBuilder(commandLine, (File) null);
	}
	
	public ProcessBuilder createDefaultProcessBuilder(List<String> commandLine, File workingDir) {
		assertTrue(commandLine.size() > 0);
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		setupDefaultEnvironment(pb);
		if(workingDir != null) {
			pb.directory(workingDir);
		}
		return pb;
	}
	
	@SuppressWarnings("unused")
	protected void setupDefaultEnvironment(ProcessBuilder pb) {
	}
	
	public ExternalProcessResult runEngineClientTool(String clientPath, List<String> arguments,
			String clientInput, IProgressMonitor pm) throws CoreException {
		return runEngineClientTool(new ArrayList2<>(clientPath).addElements(arguments), clientInput, pm);
	}
	
	public ExternalProcessResult runEngineClientTool(List<String> commandLine, String clientInput,
			IProgressMonitor pm) throws CoreException {
		ProcessBuilder pb = createDefaultProcessBuilder(commandLine);
		return new RunEngineClientOperation(this, pb).runProcess(clientInput, pm);
	}
	
}