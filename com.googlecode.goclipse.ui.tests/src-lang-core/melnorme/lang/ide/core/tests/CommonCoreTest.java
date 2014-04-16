/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
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
import melnorme.lang.ide.core.tests.utils.ErrorLogListener;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.tests.CommonTest;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/** 
 * Base core test class that adds an exception listener to the platform log. 
 * The ErrorLogListener was the only way I found to detect UI exceptions in SafeRunnable's 
 * when running as plugin test. 
 */
public abstract class CommonCoreTest extends CommonTest {
	
	protected static ErrorLogListener logErrorListener;
	
	@BeforeClass
	public static void setUpExceptionListenerStatic() throws Exception {
		logErrorListener = ErrorLogListener.createAndInstall();
	}
	
	@AfterClass
	public static void checkLogErrorListenerStatic() throws Throwable {
		logErrorListener.checkErrorsAndUninstall();
	}
	
	@Before
	public void setUpExceptionListener() throws Throwable {
		logErrorListener.checkErrors();
	}
	
	@After
	public void checkLogErrorListener() throws Throwable {
		logErrorListener.checkErrors();
	}
	
	/* ----------------- utilities ----------------- */
	
	public static IProject createAndOpenProject(String name, boolean overwrite) throws CoreException {
		return ResourceUtils.createAndOpenProject(name, overwrite);
	}
	
	public static void deleteProject(String projectName) {
		ResourceUtils.deleteProject_unchecked(projectName);
	}
	
	public static void setupLangProject(IProject project) throws CoreException {
		assertTrue(project.exists());
		ISchedulingRule currentRule = Job.getJobManager().currentRule();
		assertTrue(currentRule != null && currentRule.contains(project));
		EclipseUtils.addNature(project, LangNature.NATURE_ID);
	}
	
}