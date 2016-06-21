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
package melnorme.util.swt.components;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import melnorme.utilbox.tests.CommonTest;

public abstract class AbstractWidgetTest extends CommonTest {
	
	public AbstractWidgetTest() {
		super();
	}
	
	@Test
	public void runTest() throws Exception {
		runTestWithShell();
	}
	
	protected void runTestWithShell() {
		Shell shell = new Shell(Display.getDefault());
		try {

			doRunTest(shell);
		
		} finally {
			shell.dispose();
		}
	}
	
	protected abstract void doRunTest(Shell shell);
	
}