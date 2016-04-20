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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.CoreSettings.SettingsField;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tests.ToolingTests_Actual;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.IDisposable;

public abstract class CoreTestWithProject extends CommonCoreTest_ActualClass {
	
	public static class TestsProject extends SampleProject {
		public TestsProject(String name, boolean create) throws CoreException, CommonException {
			super(name, create);
		}
		
		@Override
		public void doCreate() throws CoreException, CommonException {
			super.doCreate();
			
			setTestsSDKPath(project);
		}
		
		public static void setTestsSDKPath(IProject project) {
			String SDK_PATH = ToolingTests_Actual.SAMPLE_SDK_PATH.toString();
			SDK_LOCATION.doSetRawValue(project, SDK_PATH);
		}
	}
	
	public CoreTestWithProject() {
		super();
	}
	
	protected static final SettingsField<Path> SDK_LOCATION = LangCore.settings().SDK_LOCATION;
	protected SampleProject sampleProject;
	protected IProject project;
	
	protected SampleProject initSampleProject() throws CoreException, CommonException {
		return setSampleProject(new TestsProject(getClass().getSimpleName(), false) {
			@Override
			protected void customizeAfterCreate() {
				customizeProjectAfterCreate();
			}
		});
	}
	
	protected void customizeProjectAfterCreate() {
	}
	
	protected SampleProject setSampleProject(SampleProject sampleProject) throws CoreException, CommonException {
		if(this.sampleProject == sampleProject) {
			return sampleProject;
		}
		
		if(this.sampleProject != null) {
			this.sampleProject.cleanUp();
		}
		
		this.sampleProject = sampleProject;
		this.project = sampleProject.getProject();
		
		sampleProject.create();
		assertTrue(sampleProject.getProject().exists());
		
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
	
	protected Path getSDKToolPath() throws StatusException {
		assertNotNull(project);
		return SDK_LOCATION.getValue(project);
	}
	
	protected String strSDKTool() throws StatusException {
		return getSDKToolPath().toString();
	}
	
}