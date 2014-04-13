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
package melnorme.lang.ide.ui.launch;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.launching.LaunchConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.WorkingDirectoryBlock;

public class LangWorkingDirectoryBlock extends WorkingDirectoryBlock {
	
	public LangWorkingDirectoryBlock() {
		super(LaunchConstants.ATTR_WORKING_DIRECTORY);
	}
	
	@Override
	protected IProject getProject(ILaunchConfiguration configuration) throws CoreException {
		String projectName = getProjectAttribute(configuration);
		if (projectName != null) {
			projectName = projectName.trim();
			if (projectName.length() > 0) {
				return LangCore.getWorkspaceRoot().getProject(projectName);
			}
		}
		return null;
	}
	
	protected String getProjectAttribute(ILaunchConfiguration config) throws CoreException {
		return config.getAttribute(LaunchConstants.ATTR_PROJECT_NAME, (String) null);
	}
	
}