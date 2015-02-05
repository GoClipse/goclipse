/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.bundlemodel;

import melnorme.lang.ide.core.utils.DefaultProjectResourceListener;
import melnorme.lang.tooling.IBundleInfo;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

public abstract class BundleManifestResourceListener extends DefaultProjectResourceListener {
	
	@Override
	protected void processProjectDelta(IResourceDelta projectDelta) {
		IProject project = (IProject) projectDelta.getResource();
		
		IBundleInfo existingProjectModel = getBundleInfo(project);
		
		if(projectDelta.getKind() == IResourceDelta.REMOVED || !isEligibleForBundleModelWatch(project)) {
			// New bundle model status = removed. 
			
			if(existingProjectModel == null) {
				return; // Nothing to update, wasn't a bundle model project to start with.
			}
			bundleProjectRemoved(project);
			return;
		}
		
		
		if(projectDelta.getKind() == IResourceDelta.ADDED) {
			if(projectHasBundleManifest(project)) {
				bundleProjectAdded(project);
			}
		} else if (projectDelta.getKind() == IResourceDelta.CHANGED) {
			if((projectDelta.getFlags() & IResourceDelta.DESCRIPTION) != 0) {
				// It might be the case that project wasn't a bundle model project before, and now is eligible,
				// purely due to a change in DESCRIPTION (such as a nature add)
				if(existingProjectModel == null) {
					// Then it's true, project has become bundle model project.
					if(projectHasBundleManifest(project)) {
						bundleProjectAdded(project);
					}
					return;
				}
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
	
	public abstract boolean isEligibleForBundleModelWatch(IProject project);
	
	public abstract IBundleInfo getBundleInfo(IProject project);
	
	public abstract boolean projectHasBundleManifest(IProject project);
	
	public abstract boolean resourceDeltaIsBundleManifestChange(IResourceDelta resourceDelta);
	
	public abstract void bundleProjectAdded(IProject project);
	
	public abstract void bundleProjectRemoved(IProject project);
	
	public abstract void bundleManifestChanged(IProject project);
	
}