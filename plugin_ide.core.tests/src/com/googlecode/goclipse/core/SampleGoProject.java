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

import melnorme.lang.ide.core.tests.AbstractSampleProject;
import melnorme.lang.ide.core.utils.ResourceUtils;

import org.eclipse.core.runtime.CoreException;

public class SampleGoProject extends AbstractSampleProject {
	
	public SampleGoProject(String name) throws CoreException {
		super(name);
	}
	
	@Override
	protected void fillProject() throws CoreException {
		ResourceUtils.createFolder(project.getFolder("src"), true, null);
	}
	
}