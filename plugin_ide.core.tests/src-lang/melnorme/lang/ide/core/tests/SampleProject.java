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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertEquals;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.operations.ICoreOperation;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;


public class SampleProject implements AutoCloseable {
	
	public final IProject project;
	
	public SampleProject(String name) throws CoreException, CommonException {
		this(name, true);
	}
	
	public SampleProject(String name, boolean create) throws CoreException, CommonException {
		this.project = EclipseUtils.getWorkspaceRoot().getProject(name);
		if(create) {
			create();
		}
	}
	
	public final void create() throws CoreException, CommonException {
		ICoreOperation operation = (pm) -> doCreate();
		try {
			ResourceUtils.runCommonOperationInWorkspace(operation, null, null);
		} catch(OperationCancellation e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	public void doCreate() throws CoreException, CommonException {
		IProject newProject = CommonCoreTest.createAndOpenProject(project.getName(), true);
		assertEquals(project, newProject);
		fillProject();
		CommonCoreTest.setupLangProject(project, false);
		assertTrue(project.getNature(LangNature.NATURE_ID) != null);
		
		LangCore.settings().SDK_LOCATION.getEnableProjectSettingsPref().doSetValue(newProject, true);
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
	
	public IFile getFile(String path) {
		return getProject().getFile(path);
	}
	
	/* ----------------- helpers ----------------- */
	
	public void moveToLocation(Location packageLocation) throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setLocation(ResourceUtils.epath(packageLocation));
		project.move(description, false, null);
	}
	
}