/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.misc.SimpleLogger;

public abstract class ProjectBasedModel<INFO, LISTENER extends IProjectModelListener<INFO>> {
	
	protected final HashMap<String, INFO> projectInfos = new HashMap<>();
	protected final ListenerListHelper<LISTENER> listeners = new ListenerListHelper<>();
	
	public ProjectBasedModel() {
		super();
	}
	
	protected abstract SimpleLogger getLog();
	
	public void addListener(LISTENER listener) {
		listeners.addListener(listener);
	}
	public void removeListener(LISTENER listener) {
		listeners.removeListener(listener);
	}
	
	public synchronized INFO getProjectInfo(IProject project) {
		return projectInfos.get(project.getName());
	}
	
	public synchronized INFO addProjectInfo(IProject project, INFO newProjectInfo) {
		String projectName = project.getName();
		projectInfos.put(projectName, newProjectInfo);
		notifyProjectAdded(project, newProjectInfo);
		return newProjectInfo;
	}
	
	public synchronized INFO removeProjectInfo(IProject project) {
		INFO oldProjectInfo = projectInfos.remove(project.getName());
		assertNotNull(oldProjectInfo);
		notifyProjectRemoved(project, oldProjectInfo);
		return oldProjectInfo;
	}
	
	protected void notifyProjectAdded(IProject project, INFO newProjectInfo) {
		getLog().println(getClass().getSimpleName() + " info added: " + project.getName());
		fireUpdateEvent(new UpdateEvent<INFO>(project, newProjectInfo));
	}
	
	protected void notifyProjectRemoved(IProject project, INFO oldProjectInfo) {
		getLog().println(getClass().getSimpleName() + " info removed: " + project.getName());
		fireUpdateEvent(new UpdateEvent<INFO>(project, oldProjectInfo));
	}
	
	protected void fireUpdateEvent(UpdateEvent<INFO> updateEvent) {
		for (IProjectModelListener<INFO> listener : listeners.getListeners()) {
			listener.notifyUpdateEvent(updateEvent);
		}
	}
	
}