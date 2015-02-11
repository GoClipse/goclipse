/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.dialogs;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.tests.CommonUITest;
import melnorme.lang.ide.ui.tests.utils.WizardDialog__Accessor;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public abstract class LangProjectWizardTest extends CommonUITest {
	
	public static final String EXISTING_PROJ_NAME = LangProjectWizardTest.class + "_ExistingProject";
	public static final String EXISTING_TEMP_PROJ_NAME = LangProjectWizardTest.class + "_ExistingProject";
	public final static String NEWPROJNAME = "WizardCreationProject";
	
	@BeforeClass
	public static void classSetup() throws CoreException {
		ResourceUtils.createAndOpenProject(EXISTING_PROJ_NAME, false);
	}
	
	@AfterClass
	public static void classTeardon() throws CoreException {
		ResourceUtils.tryDeleteProject(EXISTING_PROJ_NAME);
	}
	
	protected WizardDialog__Accessor wizDialog;
	protected LangNewProjectWizard wizard;
	
	public LangProjectWizardTest() {
		super();
	}
	
	@Before
	public void setUp() throws Exception {
		tearDown();
		
		wizard = createNewProjectWizard();
		IWorkbenchWindow window = WorkbenchUtils.getActiveWorkbenchWindow();
		wizard.init(window.getWorkbench(), null);
		
		Shell parent = WorkbenchUtils.getActiveWorkbenchShell();
		wizDialog = new WizardDialog__Accessor(parent, wizard);
		wizDialog.setBlockOnOpen(false);
		wizDialog.open();
	}
	
	@After
	public void tearDown() throws Exception {
		// Should undo all wizard actions
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				IProject project = EclipseUtils.getWorkspaceRoot().getProject(NEWPROJNAME);
				if(project.exists()) {
					project.delete(true, monitor);
				}
			}
		}, null);
	}
	
	protected abstract LangNewProjectWizard createNewProjectWizard();
	
	protected void firstPage_setProjectName(String name) {
		wizard.getFirstPage().getNameGroup().getNameField().setFieldValue(name);
	}
	
	protected void simulatePressCancel() {
		wizDialog.cancelPressed();
	}
	
	protected void simulatePressFinish() {
		wizDialog.finishPressed();
	}
	
	protected void simulateEnterPage2() {
		wizDialog.nextPressed();
	}
	
	protected void simulatePage2GoBack() {
		wizDialog.backPressed();
	}
	
	protected boolean checkLangProjectExists(String projectName) throws Throwable {
		logErrorListener.checkErrors();
		return LangNature.isAccessible(ResourceUtils.getWorkspaceRoot().getProject(projectName));
	}
	
	protected boolean checkProjectExists(String projectName) throws Throwable {
		logErrorListener.checkErrors();
		return ResourceUtils.getWorkspaceRoot().getProject(projectName).exists();
	}
	
	protected boolean checkNoChanges(String projectName) throws Throwable {
		return checkLangProjectExists(projectName) == false;
	}
	
	protected boolean checkProjectCreated() throws Throwable {
		return checkLangProjectExists(NEWPROJNAME);
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void test_P1Validation() throws Throwable { test_P1Validation$(); }
	public void test_P1Validation$() throws Throwable {
		firstPage_setProjectName(EXISTING_PROJ_NAME);
		assertTrue(wizard.canFinish());
		
		simulatePressCancel();
		assertTrue(checkNoChanges(NEWPROJNAME));
	}
	
	@Test
	public void test_P1_Finish() throws Throwable {
		firstPage_setProjectName(NEWPROJNAME);
		assertTrue(wizard.canFinish());
		
		simulatePressFinish();
		assertTrue(checkProjectCreated());
	}
	
	@Test
	public void test_P1_Cancel() throws Throwable {
		firstPage_setProjectName(NEWPROJNAME);
		assertTrue(wizard.canFinish());
		
		
		simulatePressCancel();
		assertTrue(checkNoChanges(NEWPROJNAME));
	}
	
	/* ----------------- Page 2 tests ----------------- */
	
	@Test
	public void testPage2() throws Throwable { testPage2$(); }
	public void testPage2$() throws Throwable {
		
		if(wizard.getSecondPage() == null) {
			return; // Not applicable.
		}
		
		setUp();
		firstPage_setProjectName(NEWPROJNAME);
		simulateEnterPage2();
		simulatePressCancel();
		assertTrue(checkNoChanges(NEWPROJNAME));
		
		
		setUp();
		firstPage_setProjectName(NEWPROJNAME);
		simulateEnterPage2();
		simulatePage2GoBack();
		assertTrue(checkNoChanges(NEWPROJNAME));
		assertTrue(wizard.getProjectCreator().revertActions.isEmpty());
		
		simulatePressFinish();
		assertTrue(checkLangProjectExists(NEWPROJNAME));
		
		test_RevertOnlyTheEnactedChanges$();
	}
	
	protected void test_RevertOnlyTheEnactedChanges$() throws CoreException, Exception, Throwable {
		try {
			ResourceUtils.createAndOpenProject(EXISTING_TEMP_PROJ_NAME, true);
			assertProjectExists(EXISTING_TEMP_PROJ_NAME, false);
			
			setUp();
			firstPage_setProjectName(EXISTING_TEMP_PROJ_NAME);
			simulateEnterPage2();
			assertProjectExists(EXISTING_TEMP_PROJ_NAME, true);
			simulatePage2GoBack();
			assertProjectExists(EXISTING_TEMP_PROJ_NAME, false);
			
			// Test again
			simulateEnterPage2();
			simulatePage2GoBack();
			assertProjectExists(EXISTING_TEMP_PROJ_NAME, false);
			
			
			simulateEnterPage2();
			simulatePressCancel();
			assertProjectExists(EXISTING_TEMP_PROJ_NAME, false);
			
		} finally {
			ResourceUtils.tryDeleteProject(EXISTING_TEMP_PROJ_NAME);
		}
	}
	
	protected void assertProjectExists(String projectName, boolean isLang) throws Throwable {
		assertTrue(checkProjectExists(projectName));
		assertTrue(checkLangProjectExists(projectName) == isLang);
	}
	
}