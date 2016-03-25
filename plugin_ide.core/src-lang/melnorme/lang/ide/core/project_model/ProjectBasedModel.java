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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.fields.ListenerListHelper;
import melnorme.utilbox.misc.SimpleLogger;

public abstract class ProjectBasedModel<INFO> {
	
	protected final HashMap<String, INFO> projectInfos = new HashMap<>();
	protected final ListenerListHelper<IProjectModelListener<INFO>> listeners = new ListenerListHelper<>();
	
	public ProjectBasedModel() {
		super();
	}
	
	protected abstract SimpleLogger getLog();
	
	public void addListener(IProjectModelListener<? super INFO> listener) {
		listeners.addListener(listener.castTypeParam());
	}
	public void removeListener(IProjectModelListener<? super INFO> listener) {
		listeners.removeListener(listener.<INFO>castTypeParam());
	}
	
	@SuppressWarnings("unchecked")
	public synchronized HashMap<String, INFO> connectListener(IProjectModelListener<? super INFO> listener) {
		addListener(listener);
		return (HashMap<String, INFO>) projectInfos.clone();
	}
	
	protected synchronized INFO getProjectInfo(IProject project) {
		return projectInfos.get(project.getName());
	}
	
	protected synchronized INFO setProjectInfo(IProject project, INFO newProjectInfo) {
		String projectName = project.getName();
		assertNotNull(newProjectInfo);
		projectInfos.put(projectName, newProjectInfo);
		notifyProjectInfoAdded(project, newProjectInfo);
		return newProjectInfo;
	}
	
	/**
	 * Set a new project info, but only if the current info is the same as given oldProjectInfo
	 */
	public synchronized INFO updateProjectInfo(IProject project, INFO oldProjectInfo, INFO newProjectInfo) {
		String projectName = project.getName();
		assertNotNull(newProjectInfo);
		if(projectInfos.get(projectName) == oldProjectInfo) {
			return setProjectInfo(project, newProjectInfo);
		}
		return null;
	}
	
	public synchronized INFO removeProjectInfo(IProject project) {
		INFO oldProjectInfo = projectInfos.remove(project.getName());
		if(oldProjectInfo != null) {
			notifyProjectRemoved(project, oldProjectInfo);
		}
		return oldProjectInfo;
	}
	
	public synchronized Set<String> getModelProjects() {
		return new HashSet<>(projectInfos.keySet());
	}
	
	protected void notifyProjectInfoAdded(IProject project, INFO newProjectInfo) {
		getLog().println(getClass().getSimpleName() + " info set: " + project.getName());
		fireUpdateEvent(new UpdateEvent<INFO>(project, newProjectInfo));
	}
	
	@SuppressWarnings("unused")
	protected void notifyProjectRemoved(IProject project, INFO oldProjectInfo) {
		getLog().println(getClass().getSimpleName() + " info removed: " + project.getName());
		fireUpdateEvent(new UpdateEvent<INFO>(project, null));
	}
	
	protected void fireUpdateEvent(UpdateEvent<INFO> updateEvent) {
		for (IProjectModelListener<INFO> listener : listeners.getListeners()) {
			listener.notifyUpdateEvent(updateEvent);
		}
	}
	
}