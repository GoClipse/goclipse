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
package melnorme.lang.ide.core.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.misc.Location;


public class SampleProject implements AutoCloseable {
	
	public final IProject project;
	
	public SampleProject(String name) throws CoreException {
		project = CommonCoreTest.createAndOpenProject(name, true);
		fillProject();
		CommonCoreTest.setupLangProject(project, false);
		assertTrue(project.getNature(LangNature.NATURE_ID) != null);
	}
	
	public void cleanUp() throws CoreException {
		project.delete(true, null);
	}
	
	protected void fillProject() throws CoreException {
	}
	
	@Override
	public void close() throws CoreException {
		cleanUp();
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getName() {
		return getProject().getName();
	}
	
	/* ----------------- helpers ----------------- */
	
	public void moveToLocation(Location packageLocation) throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setLocation(ResourceUtils.epath(packageLocation));
		project.move(description, false, null);
	}
	
}