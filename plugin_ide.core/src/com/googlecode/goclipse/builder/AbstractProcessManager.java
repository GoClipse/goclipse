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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.util.List;

import melnorme.lang.ide.core.utils.process.IExternalProcessListener;
import melnorme.utilbox.misc.ListenerListHelper;

/**
 * {@link AbstractProcessManager} is basically a factory to create external process tasks
 * that are bound to notify the process listeneners that this manager manages.
 */
public abstract class AbstractProcessManager<T extends IExternalProcessListener> {
	
	protected final ListenerListHelper<T> processListenersHelper = new ListenerListHelper<>();
	
	public void addBuildProcessListener(T processListener) {
		processListenersHelper.addListener(processListener);
	}
	
	public void removeBuildProcessListener(T processListener) {
		processListenersHelper.removeListener(processListener);
	}
	
	/* -----------------  ----------------- */
	
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
	
	protected abstract void setupDefaultEnvironment(ProcessBuilder pb);
	
}