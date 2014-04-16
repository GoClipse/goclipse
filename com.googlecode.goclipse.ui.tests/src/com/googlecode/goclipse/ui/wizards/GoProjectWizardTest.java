package com.googlecode.goclipse.ui.wizards;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.tests.utils.WizardDialog__Accessor;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.utilbox.core.DevelopmentCodeMarkers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.goclipse.ui.CommonGoUITest;



public class GoProjectWizardTest extends CommonGoUITest {
	
	public final static String NEWPROJNAME = "ProjectWizard.ProjectName";
	public final static String EXISTING_PROJNAME = "ProjectWizard.ExistingProject";
	
	protected GoProjectWizard wizard;
	protected WizardDialog__Accessor wizDialog;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		IProject project = createAndOpenProject(EXISTING_PROJNAME, true);
		setupGoProject(project);
	}
	
	@Before
	public void setUp() throws Exception {
		tearDown();
		wizard = new GoProjectWizard();
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
	
	
	protected void simulatePressCancel() {
		wizDialog.cancelPressed();
	}
	
	protected void simulatePressFinish() {
		wizDialog.finishPressed();
	}
	
	protected boolean checkNoChanges() throws Throwable {
		logErrorListener.checkErrors();
		
		return EclipseUtils.getWorkspaceRoot().getProject(NEWPROJNAME).exists() == false;
	}
	
	protected boolean checkProjectCreated() throws Throwable {
		logErrorListener.checkErrors();
		return EclipseUtils.getWorkspaceRoot().getProject(NEWPROJNAME).exists();
	}
	
	@Test
	public void test_P1Validation() throws Throwable { test_P1Validation$(); }
	public void test_P1Validation$() throws Throwable {
		firstPage_setProjectName(EXISTING_PROJNAME);
		if(DevelopmentCodeMarkers.UNIMPLEMENTED_FUNCTIONALITY) {
			assertTrue(!wizard.canFinish());
		}
		
		simulatePressCancel();
		assertTrue(checkNoChanges());
	}
	
	protected void firstPage_setProjectName(String name) {
		wizard.page.getProjectComposite().getNameField().setText(name);
	}

	@Test
	public void test_P1_Finish() throws Throwable {
		firstPage_setProjectName(NEWPROJNAME);
		assertTrue(wizard.canFinish());
		
		simulatePressFinish();
		assertTrue(checkProjectCreated());
		assertTrue(wizard.page.getControl().isDisposed());
	}
	
	@Test
	public void test_P1_Cancel() throws Throwable {
		firstPage_setProjectName(NEWPROJNAME);
		assertTrue(wizard.canFinish());
		
		
		simulatePressCancel();
		assertTrue(checkNoChanges());
	}
	
	protected void simulateEnterPage2() {
		wizDialog.nextPressed();
	}
	
	protected void simulatePage2GoBack() {
		wizDialog.backPressed();
	}
	
	// There is no page 2
//	@Test
//	public void test_P1_P2_P1_Finish() throws Throwable {
//		ProjectWizardFirstPage__Accessor.access_fNameGroup(wizard.fFirstPage).setName(NEWPROJNAME);
//		assertTrue(wizard.canFinish());
//		simulateEnterPage2();
//		
//		simulatePage2GoBack();
//		
//		simulatePressFinish();
//		assertTrue(checkProjectCreated());
//	}
	
}