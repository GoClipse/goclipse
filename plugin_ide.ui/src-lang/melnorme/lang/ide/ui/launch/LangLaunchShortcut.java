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
package melnorme.lang.ide.ui.launch;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.ILaunchShortcut;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetLaunchSettings;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class LangLaunchShortcut extends BaseLaunchShortcut implements ILaunchShortcut {
	
	@Override
	protected ILaunchable getLaunchTargetForElement(Object element, IProgressMonitor pm)
			throws CoreException, CommonException, OperationCancellation {
		
		IProject project = getAssociatedProject(element);
		if(project == null) {
			return null;
		}
		
		BuildTargetLaunchSettings launchSettings = new BuildTargetLaunchSettings();
		launchSettings.initFromProject(project);
		
		/* FIXME: build target with references */
		return new BuildTargetLaunchable(project, launchSettings);
	}
	
	protected IProject getAssociatedProject(Object element) {
		IResource resource;
		if(element instanceof IResource) {
			resource = (IResource) element;
		} else {
			resource = EclipseUtils.getAdapter(element, IResource.class);
		}
		
		return resource == null ? null : resource.getProject();
	}
	
	public class BuildTargetLaunchable implements ILaunchable {
		
		protected final IProject project;
		protected final BuildTargetLaunchSettings buildTargetSettings;
		
		public BuildTargetLaunchable(IProject project, BuildTargetLaunchSettings buildTargetSettings) {
			this.project = project;
			this.buildTargetSettings = buildTargetSettings;
			assertNotNull(buildTargetSettings.buildTargetName);
		}
		
		@Override
		public IProject getProject() {
			return project;
		}
		
		@Override
		public String getLabel() {
			return getBuildTargetName();
		}
		
		public String getBuildTargetName() {
			return buildTargetSettings.buildTargetName;
		}
		
		@Override
		public boolean matchesLaunchConfiguration(ILaunchConfiguration config) throws CoreException {
			BuildTargetLaunchSettings otherLaunchSettings = new BuildTargetLaunchSettings(config);
			
			/* FIXME: match other settings parameters. */
			if(!areEqual(getProjectName(), otherLaunchSettings.projectName)) {
				return false;
			}
			
			if(areEqual(
				LangCore.getBuildManager().getResolvedBuildTargetName(getBuildTargetName()),
				LangCore.getBuildManager().getResolvedBuildTargetName(otherLaunchSettings.buildTargetName)
			)) {
				return true;
			}
			return false;
		}
		
		@Override
		public ILaunchConfiguration createNewConfiguration() throws CoreException {
			return buildTargetSettings.createNewConfiguration(getLaunchConfigType());
		}
		
	}
	
}