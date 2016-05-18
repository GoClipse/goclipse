/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.ui.ILaunchShortcut;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.launch.BuildTargetLaunchCreator;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.launch.LangLaunchShortcut;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class GoLaunchShortcut extends LangLaunchShortcut implements ILaunchShortcut {
	
	public static final String LAUNCH_CONFIG_ID = "com.googlecode.goclipse.launch.goLaunchType";
	
	@Override
	protected String getLaunchTypeId() {
		return LAUNCH_CONFIG_ID;
	}
	
	@Override
	protected ILaunchable getLaunchTargetForElement(Object element, IOperationMonitor om)
			throws CommonException, OperationCancellation {
		
		IResource resource;
		if(element instanceof IResource) {
			resource = (IResource) element;
		} else {
			resource = EclipseUtils.getAdapter(element, IResource.class);
		}
		
		if(resource instanceof IFile) {
			return getLaunchableForGoPackage(resource.getParent());
		} else if(resource instanceof IFolder) { 
			return getLaunchableForGoPackage(resource);
		}
		return null;
	}
	
	protected ILaunchable getLaunchableForGoPackage(IResource goPackageResource) throws CommonException {
		IProject project = goPackageResource.getProject();
		if(project == null) {
			return null;
		}
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		Location goPackageLocation = ResourceUtils.getResourceLocation(goPackageResource);
		GoPackageName goPackageName = goEnv.findGoPackageForLocation(goPackageLocation);
		if(goPackageName == null) {
			throw CommonException.fromMsgFormat("Resource doesn't have a corresponding Go package.");
		}
		
		BuildTargetLaunchCreator btLaunchCreator = new BuildTargetLaunchCreator().initFromProject(project);
		btLaunchCreator.data.targetName = goPackageName.getFullNameAsString();
		return new BuildTargetLaunchable(project, btLaunchCreator);
	}
	
}