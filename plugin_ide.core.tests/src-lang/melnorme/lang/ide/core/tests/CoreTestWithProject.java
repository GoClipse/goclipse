/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.tests;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.tests.CommonCoreTest_ActualClass;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.IDisposable;

public abstract class CoreTestWithProject extends CommonCoreTest_ActualClass {
	
	public CoreTestWithProject() {
		super();
	}
	
	protected SampleProject sampleProject;
	protected IProject project;
	
	protected SampleProject initSampleProject() throws CoreException, CommonException {
		this.sampleProject = new SampleProject(getClass().getSimpleName());
		return setSampleProject(sampleProject);
	}
	
	protected SampleProject setSampleProject(SampleProject sampleProject) {
		this.project = sampleProject.getProject();
		
		owned.add(new IDisposable() {
			@Override
			public void dispose() {
				try {
					sampleProject.cleanUp();
				} catch(CoreException e) {
					throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
				}
			}
		});
		return sampleProject;
	}
	
}