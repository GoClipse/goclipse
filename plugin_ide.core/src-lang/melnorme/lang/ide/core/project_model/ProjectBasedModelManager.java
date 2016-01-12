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

import static melnorme.lang.ide.core.utils.ResourceUtils.getWorkspaceRoot;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.BundlePath;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.LifecycleObject;

/**
 * Abstract class for a manager for a project-based model.
 */
public abstract class ProjectBasedModelManager extends LifecycleObject {
	
	protected final BundleManifestResourceListener listener = init_createResourceListener();
	
	public ProjectBasedModelManager() {
	}
	
	protected void initializeModelManager() {
		ResourceUtils.connectResourceListener(listener, this::initializeProjectsInfo, getWorkspaceRoot(), owned);
	}
	
	public final void shutdownManager() {
		dispose();
	}
	
	/* -----------------  ----------------- */
	
	protected void initializeProjectsInfo() {
		
		IProject[] projects = EclipseUtils.getWorkspaceRoot().getProjects();
		for (IProject project : projects) {
			if(listener.isEligibleForBundleManifestWatch(project) && listener.projectHasBundleManifest(project)) {
				bundleProjectAdded(project);
			}
		}
	}
	
	protected abstract BundleManifestResourceListener init_createResourceListener();
	
	protected class ManagerResourceListener extends BundleManifestResourceListener {
		
		public ManagerResourceListener() {
			super();
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
		
		@Override
		public boolean resourceIsManifest(IResource resource) {
			return ProjectBasedModelManager.this.resourceIsManifest(resource);
		}
		
	}
	
	protected abstract Object getProjectInfo(IProject project);
	
	protected abstract void bundleProjectAdded(IProject project);
	
	protected abstract void bundleProjectRemoved(IProject project);
	
	protected abstract void bundleManifestFileChanged(IProject project);
	
	public boolean resourceIsManifest(IResource resource) {
		if(resource == null || resource.getType() != IResource.FILE) {
			return false;
		}
		try {
			Location projectLoc = ResourceUtils.getLocation(resource.getProject());
			Location resourceLoc = ResourceUtils.getLocation(resource);
			return areEqual(new BundlePath(projectLoc).getManifestLocation(true), resourceLoc);
		} catch(CommonException e) {
			return false;
		}
	}
	
}