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
package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangPerspective;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.IPageLayout;

public class GoPerspective extends LangPerspective {
	
	public static final String ID = "com.googlecode.goclipse.perspectives.GoPerspective";
	
	@Override
	protected void addActionSets(IPageLayout layout) {
		super.addActionSets(layout);
		
		layout.addActionSet(IDebugUIConstants.DEBUG_ACTION_SET);
	}
	
	@Override
	protected void addNewWizardShortcuts(IPageLayout layout) {
		super.addNewWizardShortcuts(layout);
		
		layout.addNewWizardShortcut("com.googlecode.goclipse.wizards.NewGoFileWizard");
	}
	
}