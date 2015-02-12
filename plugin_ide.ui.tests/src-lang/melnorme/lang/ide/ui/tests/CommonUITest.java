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
package melnorme.lang.ide.ui.tests;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.tests.CommonCoreTest_ActualClass;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.util.swt.SWTTestUtils;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;
import org.junit.AfterClass;
import org.junit.Before;

public abstract class CommonUITest extends CommonCoreTest_ActualClass {
	
	static {
		assertTrue(PlatformUI.isWorkbenchRunning());
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		assertTrue(workbench != null);
		assertTrue(workbench.isStarting() == false);
		
		IWorkbenchPage page = WorkbenchUtils.getActivePage();
		page.closeAllEditors(false);
		
		IIntroPart intro = workbench.getIntroManager().getIntro();
		workbench.getIntroManager().closeIntro(intro);
		
		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(
			UITests_Actual.PERSPECTIVE_ID);
		assertNotNull(perspective);
		page.setPerspective(perspective);
		
		SWTTestUtils.________________flushUIEventQueue________________();
		try {
			checkLogErrors_();
		} catch (Throwable e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	@AfterClass
	public static void staticTestEnd() throws Exception {
		WorkbenchUtils.getActivePage().closeAllEditors(false);
	}
	
	@Before
	public void checkWorbench() throws Exception {
		assertTrue(PlatformUI.getWorkbench().getIntroManager().getIntro() == null);
	}
	
	@Override
	protected void doCheckLogErrors() throws Throwable {
		SWTTestUtils.clearEventQueue();
		super.doCheckLogErrors();
	}
	
}