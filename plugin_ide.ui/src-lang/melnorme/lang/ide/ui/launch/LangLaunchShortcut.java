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

import static melnorme.lang.ide.core.utils.operation.CoreRunnable.toCoreException;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.ILaunchShortcut;

import melnorme.lang.ide.core.launch.BuildTargetLaunchCreator;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class LangLaunchShortcut extends BaseLaunchShortcut implements ILaunchShortcut {
	
	public String getLaunchNameForSubTarget(String subTargetName) {
		return "[" + subTargetName + "]";
	}
	
	@Override
	protected ILaunchable getLaunchTargetForElement(Object element, IProgressMonitor pm)
			throws CoreException, CommonException, OperationCancellation {
		
		IProject project = getAssociatedProject(element);
		if(project == null) {
			return null;
		}
		
		BuildTargetLaunchCreator btLaunchCreator = new BuildTargetLaunchCreator();
		btLaunchCreator.initFromProject(project);
		
		return getLaunchableForElement(element, project, btLaunchCreator, pm);
	}
	
	@SuppressWarnings("unused")
	protected BuildTargetLaunchable getLaunchableForElement(Object element, IProject project,
			BuildTargetLaunchCreator btLaunchCreator, IProgressMonitor pm) 
		throws CoreException, CommonException, OperationCancellation 
	{
		return new BuildTargetLaunchable(project, btLaunchCreator);
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
		protected final BuildTargetLaunchCreator btLaunchCreator;
		
		public BuildTargetLaunchable(IProject project, BuildTargetLaunchCreator btLaunchCreator) {
			this.project = project;
			this.btLaunchCreator = btLaunchCreator;
			assertNotNull(btLaunchCreator.getTargetName());
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
			return btLaunchCreator.getTargetName();
		}
		
		@Override
		public boolean matchesLaunchConfiguration(ILaunchConfiguration config) throws CoreException {
			BuildTargetLaunchCreator otherLaunchSettings = toCoreException(
				() -> new BuildTargetLaunchCreator(config)); 
			
			BuildTargetData data = btLaunchCreator.data;
			BuildTargetData otherData = otherLaunchSettings.data;
			return 
				areEqual(getProjectName(), otherLaunchSettings.projectName) &&
				areEqual(data.targetName, otherData.targetName) &&
				areEqual(data.buildCommand, otherData.buildCommand) &&
				areEqual(data.executablePath, otherData.executablePath)
				;
		}
		
		@Override
		public ILaunchConfiguration createNewConfiguration() throws CoreException {
			return toCoreException(
				() -> btLaunchCreator.createNewConfiguration(getLaunchConfigType()));
		}
		
	}
	
}