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
package com.googlecode.goclipse.core;

import static com.googlecode.goclipse.core.GoEnvironmentPrefConstants.GO_OS;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

@Deprecated
public class GoWorkspace extends CommonGoLocation {
	
	protected final IProject project;
	
	public GoWorkspace(IProject project) {
		super(project.getLocation().toFile().toString(), GO_OS.get(), GoEnvironmentPrefConstants.GO_ARCH.get());
		this.project = project;
	}
	
	public String getGoPathWorkspaceString() {
		return location; // TODO: multiple locations.
	}
	
	public IPath getBinFolderRelativePath() {
		// This is not a preference ATM, it constant.
		return Path.fromOSString(DEFAULT_BIN_OUTPUT_FOLDER); 
	}
	
	public IPath getPkgFolderRelativePath() throws CoreException {
		return Path.fromOSString(DEFAULT_PKG_OUTPUT_FOLDER).append(getGo_OS_Arch_segment().toString());
	}
	
	public IPath getBinFolderLocation() throws CoreException {
		return project.getFolder(getBinFolderRelativePath()).getLocation();
	}
	
	public IPath getPkgFolderLocation() throws CoreException {
		return project.getFolder(getPkgFolderRelativePath()).getLocation();
	}
	
}