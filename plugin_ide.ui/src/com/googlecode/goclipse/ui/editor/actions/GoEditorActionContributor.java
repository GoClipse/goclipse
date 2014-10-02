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

import melnorme.lang.ide.ui.editor.AbstractLangEditorActionContributor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import com.googlecode.goclipse.editors.GoGetActionDelegate;
import com.googlecode.goclipse.editors.GofixAction;
import com.googlecode.goclipse.editors.GofmtActionDelegate;

public class GoEditorActionContributor extends AbstractLangEditorActionContributor {
	
	public static final String SOURCE_MENU_ID = "com.googlecode.goclipse.ui.sourceMenu";
	
	public static final String SOURCE_MENU__ADDITIONS = "additions";
	public static final String SOURCE_MENU__FORMAT = "format";
	public static final String SOURCE_MENU__COMMENT = "comment";

	public GoEditorActionContributor() {
	}
	
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
	}
	
	@Override
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
		
		IMenuManager sourceMenu = menu.findMenuUsingPath(SOURCE_MENU_ID);
		
		sourceMenu.appendToGroup(SOURCE_MENU__COMMENT, createEditorContribution(
			"com.googlecode.goclipse.editors.ToggleComment.run", "ToggleComment"));

		sourceMenu.appendToGroup(SOURCE_MENU__FORMAT, createEditorContribution(
			"org.eclipse.ui.edit.text.shiftLeft", ITextEditorActionConstants.SHIFT_LEFT));
		sourceMenu.appendToGroup(SOURCE_MENU__FORMAT, createEditorContribution(
			"org.eclipse.ui.edit.text.shiftRight", ITextEditorActionConstants.SHIFT_RIGHT));
		
		
		ReusableAction goFormat = registerContribution(new ReusableAction(
			"com.googlecode.goclipse.editors.GofmtShortcut.run", 
			"&Format (go fmt)",
			new GofmtActionDelegate()
		));
		
		ReusableAction goFix = registerContribution(new ReusableAction(
			"com.googlecode.goclipse.editors.GofixShortcut.run", 
			"Fix (go fix)",
			new GofixAction()
		));
		
		ReusableAction goGet = registerContribution(new ReusableAction(
			"com.googlecode.goclipse.editors.GoGetShortcut.run",
			"&Get (go get -fix -u)",
			new GoGetActionDelegate()
		) {{setToolTipText("Run the 'go get' command for the current file.");}});
		
		sourceMenu.appendToGroup(SOURCE_MENU__FORMAT, goFormat);
		
		sourceMenu.appendToGroup(SOURCE_MENU__ADDITIONS, goFix);
		sourceMenu.appendToGroup(SOURCE_MENU__ADDITIONS, goGet);
	}
	
}