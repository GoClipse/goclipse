/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.DefaultProjectResourceListener;

public abstract class BundleManifestResourceListener extends DefaultProjectResourceListener {
	
	public BundleManifestResourceListener() {
	}
	
	public boolean isValidLangProject(IProject project) {
		return LangNature.isAccessible(project, true);
	}
	
	@Override
	protected void processProjectDelta(IResourceDelta projectDelta) {
		IProject project = (IProject) projectDelta.getResource();
		
		Object existingProjectInfo = getProjectInfo(project);
		
		if(projectDelta.getKind() == IResourceDelta.REMOVED || !isValidLangProject(project)) {
			// New bundle model status = removed. 
			
			if(existingProjectInfo == null) {
				return; // Nothing to update, wasn't a bundle model project to start with.
			}
			bundleProjectRemoved(project);
			return;
		}
		
		
		if(projectDelta.getKind() == IResourceDelta.ADDED) {
			tentativeAddBundleProject(project);
		} else if (projectDelta.getKind() == IResourceDelta.CHANGED) {
			
			// It might be the case that project wasn't eligible to have an info, but now is eligible,
			// purely due to a change in DESCRIPTION (such as a nature add)
			if(existingProjectInfo == null) {
				// Then it's true, project has become bundle model project.
				tentativeAddBundleProject(project);
				return;
			}
			
			IResourceDelta[] resourceDeltas = projectDelta.getAffectedChildren();
			if(resourceDeltas == null)
				return;
			for (IResourceDelta resourceDelta : resourceDeltas) {
				if(resourceDeltaIsBundleManifestChange(resourceDelta)) {
					bundleManifestChanged(project);
				}
			}
		}
	}
	
	protected void tentativeAddBundleProject(IProject project) {
		if(isValidBundleModelProject(project)) {
			bundleProjectAdded(project);
		}
	}
	
	@SuppressWarnings("unused") 
	public boolean isValidBundleModelProject(IProject project) {
		return true;
	}
	
	public boolean resourceDeltaIsBundleManifestChange(IResourceDelta resourceDelta) {
		return resourceIsManifest(resourceDelta.getResource());
	}
	
	public abstract Object getProjectInfo(IProject project);
	
	public abstract void bundleProjectAdded(IProject project);
	
	public abstract void bundleProjectRemoved(IProject project);
	
	public abstract void bundleManifestChanged(IProject project);
	
	public abstract boolean resourceIsManifest(IResource resource);
	
}