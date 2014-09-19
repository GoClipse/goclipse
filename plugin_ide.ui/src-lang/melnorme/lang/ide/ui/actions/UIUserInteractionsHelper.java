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
package melnorme.lang.ide.ui.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


public class UIUserInteractionsHelper {
	
	public static boolean unitTestsMode = false;
	
	public UIUserInteractionsHelper() {
		super();
	}
	
	
	public static void openWarning(Shell shell, String title, String message) {
		if(unitTestsMode)
			return;
		
		MessageDialog.openWarning(shell, title, message);
	}
	
	public static void openInfo(Shell shell, String title, String message) {
		if(unitTestsMode)
			return;
		
		MessageDialog.openInformation(shell, title, message);
	}
	
	public static void openError(Shell shell, String title, String message) {
		if(unitTestsMode)
			return;
		
		MessageDialog.openError(shell, title, message);
	}
	
}