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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.WorkingDirectoryBlock;

import melnorme.lang.ide.core.launch.ProjectLaunchSettings;
import melnorme.lang.ide.launching.LaunchConstants;

public class LangWorkingDirectoryBlock extends WorkingDirectoryBlock {
	
	public LangWorkingDirectoryBlock() {
		super(LaunchConstants.ATTR_WORKING_DIRECTORY);
	}
	
	@Override
	protected IProject getProject(ILaunchConfiguration configuration) throws CoreException {
		return new ProjectLaunchSettings(configuration).getProject();
	}
	
}