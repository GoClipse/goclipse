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
package com.googlecode.goclipse.ui.launch;

import melnorme.lang.ide.core.LaunchConstants_Actual;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.launch.AbstractLaunchShortcut2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.ILaunchShortcut;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoPath;

public class GoLaunchShortcut extends AbstractLaunchShortcut2 implements ILaunchShortcut {
	
	@Override
	protected String getLaunchTypeId() {
		return LaunchConstants_Actual.LAUNCH_CONFIG_ID;
	}
	
	@Override
	protected ResourceLaunchTarget getLaunchTargetForResource(IResource resource)
			throws CommonException, OperationCancellation {
		if(resource instanceof IFile) {
			IFile file = (IFile) resource;
			return getLaunchTargetForResource(file.getParent());
		} else if(resource instanceof IContainer) {
			IContainer container = (IContainer) resource;
			return super.getLaunchTargetForResource(container);
		}
		return super.getLaunchTargetForResource(resource);
	}
	
	@Override
	protected ILaunchConfiguration createConfiguration(ILaunchTarget launchable) throws CoreException {
		Location packageLocation = ResourceUtils.getResourceLocation(launchable.getAssociatedResource());
		IProject project = launchable.getProject();
		
		GoPath goPath = GoProjectEnvironment.getEffectiveGoPath(project);
		GoPackageName goPackage = goPath.findGoPackageForSourceFile(packageLocation.resolve_valid("dummy.go"));
		String suggestedName = project.getName() + " - " + goPackage.getFullNameAsString();
		return super.createConfiguration(launchable, suggestedName);
	}
	
}