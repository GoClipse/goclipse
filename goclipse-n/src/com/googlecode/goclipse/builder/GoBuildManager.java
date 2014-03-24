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

import melnorme.lang.ide.core.utils.process.IExternalProcessListener;
import melnorme.utilbox.misc.ListenerListHelper;

public class GoBuildManager {
	
	protected static GoBuildManager instance = new GoBuildManager();
	
	public static GoBuildManager getDefault() {
		return instance;
	}
	
	public interface GoBuildListener extends IExternalProcessListener {
		
		public void handleBuildStarted(IProject project);
		
		public void handleBuildTerminated(IProject project);
		
	}
	
	/* ----------------- listeners ----------------- */
	
	protected final ListenerListHelper<GoBuildListener> processListenersHelper = new ListenerListHelper<>();
	
	public void addBuildProcessListener(GoBuildListener processListener) {
		processListenersHelper.addListener(processListener);
	}
	
	public void removeBuildProcessListener(GoBuildListener processListener) {
		processListenersHelper.removeListener(processListener);
	}

	protected void notifyBuildStarting(IProject project) {
		for (GoBuildListener processListener : processListenersHelper.getListeners()) {
			processListener.handleBuildStarted(project);
		}
	}
	
	protected void notifyBuildTerminated(IProject project) {
		for (GoBuildListener processListener : processListenersHelper.getListeners()) {
			processListener.handleBuildTerminated(project);
		}
	}
	
}