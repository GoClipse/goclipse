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
package melnorme.lang.ide.core.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;


public class AbstractSampleProject implements AutoCloseable {
	
	public final IProject project;
	
	public AbstractSampleProject(String name) throws CoreException {
		project = CommonCoreTest.createAndOpenProject(name, true);
		fillProject();
		CommonCoreTest.setupLangProject(project, false);
		assertTrue(project.getNature(LangNature.NATURE_ID) != null);
	}
	
	protected void fillProject() throws CoreException {
	}
	
	public IProject getProject() {
		return project;
	}
	
	public void cleanUp() throws CoreException {
		project.delete(true, null);
	}
	
	@Override
	public void close() throws CoreException {
		cleanUp();
	}
	
	/* ----------------- helpers ----------------- */
	
	public void moveToLocation(Location packageLocation) throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setLocation(EclipseUtils.epath(packageLocation));
		project.move(description, false, null);
	}
	
}