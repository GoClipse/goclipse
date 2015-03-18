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
import melnorme.utilbox.misc.Location;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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
	
	protected void launch(IFile file, String mode) {
		super.launch((IResource) file, mode);
	}
	
	@Override
	protected ResourceLaunchTarget resourceToLaunchTarget(IResource resource) {
		if(resource instanceof IFile) {
			IFile file = (IFile) resource;
			return resourceToLaunchTarget(file.getParent());
		} else if(resource instanceof IContainer) {
			IContainer container = (IContainer) resource;
			return super.resourceToLaunchTarget(container);
		}
		return super.resourceToLaunchTarget(resource);
	}
	
	@Override
	protected ILaunchConfiguration createConfiguration(ILaunchTarget launchable) {
		Location packageLocation = ResourceUtils.getResourceLocation(launchable.getAssociatedResource());
		IProject project = launchable.getProject();
		
		GoPath goPath = GoProjectEnvironment.getEffectiveGoPath(project);
		GoPackageName goPackage = goPath.findGoPackageForSourceFile(packageLocation.resolve_valid("dummy.go"));
		String suggestedName = project.getName() + " - " + goPackage.getFullNameAsString();
		return super.createConfiguration(launchable, suggestedName);
	}
	
}