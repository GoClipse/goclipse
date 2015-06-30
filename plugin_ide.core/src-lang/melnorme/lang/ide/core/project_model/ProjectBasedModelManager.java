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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.EclipseUtils;

/**
 * Abstract class for a manager for a project-based model.
 */
public abstract class ProjectBasedModelManager {
	
	protected final BundleManifestResourceListener listener = init_createResourceListener();
	
	public ProjectBasedModelManager() {
		super();
	}
	
	public void startManager() {
		initializeModelManager();
	}
	
	public void shutdownManager() {
		doShutdown();
	}
	
	protected void initializeModelManager() {
		try {
			EclipseUtils.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					EclipseUtils.getWorkspace().addResourceChangeListener(
						listener, IResourceChangeEvent.POST_CHANGE);
					initializeProjectsInfo(monitor);
				}
			}, null);
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
			// This really should not happen, but still try to recover by registering listener.
			EclipseUtils.getWorkspace().addResourceChangeListener(
				listener, IResourceChangeEvent.POST_CHANGE);
		}
	}
	
	
	protected void doShutdown() {
		// It is possible to shutdown the manager without having it started.
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
	}
	
	/* -----------------  ----------------- */
	
	protected void initializeProjectsInfo(@SuppressWarnings("unused") IProgressMonitor monitor) {
		
		IProject[] projects = EclipseUtils.getWorkspaceRoot().getProjects();
		for (IProject project : projects) {
			if(listener.isEligibleForBundleManifestWatch(project) && listener.projectHasBundleManifest(project)) {
				bundleProjectAdded(project);
			}
		}
	}
	
	protected abstract BundleManifestResourceListener init_createResourceListener();
	
	protected class ManagerResourceListener extends BundleManifestResourceListener {
		
		public ManagerResourceListener(Path manifestFile) {
			super(manifestFile);
		}
		
		@Override
		public boolean isEligibleForBundleManifestWatch(IProject project) {
			return LangNature.isAccessible(project, true);
		}
		
		@Override
		public Object getProjectInfo(IProject project) {
			return ProjectBasedModelManager.this.getProjectInfo(project);
		}
		
		@Override
		public void bundleProjectAdded(IProject project) {
			ProjectBasedModelManager.this.bundleProjectAdded(project);
		}
		
		@Override
		public void bundleProjectRemoved(IProject project) {
			ProjectBasedModelManager.this.bundleProjectRemoved(project);
		}
		
		@Override
		public void bundleManifestChanged(IProject project) {
			ProjectBasedModelManager.this.bundleManifestFileChanged(project);
		}
		
	}
	
	protected abstract Object getProjectInfo(IProject project);
	
	protected abstract void bundleProjectAdded(IProject project);
	
	protected abstract void bundleProjectRemoved(IProject project);
	
	protected abstract void bundleManifestFileChanged(IProject project);
	
}