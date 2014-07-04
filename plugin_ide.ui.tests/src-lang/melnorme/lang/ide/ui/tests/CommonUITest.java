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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import melnorme.lang.ide.core.tests.CommonCoreTest_ActualClass;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.util.swt.SWTTestUtils;

public abstract class CommonUITest extends CommonCoreTest_ActualClass {
	
	static {
		IWorkbench workbench = PlatformUI.getWorkbench();
		assertTrue(workbench != null);
		IWorkbenchPage page = WorkbenchUtils.getActivePage();
		page.closeAllEditors(false);
		
		IIntroPart intro = workbench.getIntroManager().getIntro();
		workbench.getIntroManager().closeIntro(intro);
		
		page.setPerspective(workbench.getPerspectiveRegistry().findPerspectiveWithId(UITests_Actual.PERSPECTIVE_ID));
		
		SWTTestUtils.________________flushUIEventQueue________________();
	}
	
	@AfterClass
	public static void staticTestEnd() throws Exception {
		WorkbenchUtils.getActivePage().closeAllEditors(false);
	}
	
	@Before
	public void checkWorbench() throws Exception {
		assertTrue(PlatformUI.getWorkbench().getIntroManager().getIntro() == null);
	}
	
	@After
	public void after_clearEventQueue() throws Throwable {
		SWTTestUtils.clearEventQueue();
	}
	
	@Override
	public void checkLogErrorListener() throws Throwable {
		SWTTestUtils.clearEventQueue();
		super.checkLogErrorListener();
	}
	
}