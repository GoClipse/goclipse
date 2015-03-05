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
package com.googlecode.goclipse.ui.editor.actions;

import melnorme.lang.ide.ui.editor.LangEditorActionContributor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;

import com.googlecode.goclipse.editors.PairMatcher;

public class GoEditorActionContributor extends LangEditorActionContributor implements GoCommandConstants {
	
	public static final String SOURCE_MENU_ID = "com.googlecode.goclipse.ui.sourceMenu";
	
	public GoEditorActionContributor() {
	}
	
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		
		registerContribution(new ActionContribution(getHandlerService(), COMMAND_GoToMatchingBracket, 
			new PairMatcher()));
	}
	
	@Override
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		super.contributeSourceMenu(sourceMenu);
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupComment, createEditorActionContribution(
			COMMAND_ToggleLineComment, "ToggleComment"));
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupAdditions, 
			registerEditorHandler(COMMAND_RunGoFix, RunGoFixOperation.handler));
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, 
			registerEditorHandler(COMMAND_RunGoFmt, RunGoFmtOperation.handler));
	}
	
}