/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor.actions;

import melnorme.lang.ide.ui.editor.LangEditorContextMenuContributor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.services.IServiceLocator;

public class GoEditorContextMenuContributor extends LangEditorContextMenuContributor implements GoCommandConstants {
	
	public GoEditorContextMenuContributor(IServiceLocator svcLocator) {
		super(svcLocator);
	}
	
	@Override
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		super.contributeSourceMenu(sourceMenu);
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, pushItem(svcLocator, COMMAND_RunGoFmt));
	}
	
}