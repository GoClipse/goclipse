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
package melnorme.lang.ide.ui.editor;


import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public abstract class LangEditorActionContributor extends LangEditorActionContributorHelper {
	
	public static final String SOURCE_MENU_ID = LangUIPlugin.PLUGIN_ID + ".sourceMenu";
	
	public static final String SOURCE_MENU_GroupComment = "comment";
	public static final String SOURCE_MENU_GroupFormat = "format";
	public static final String SOURCE_MENU_GroupAdditions = "additions";
	
	public LangEditorActionContributor() {
		super();
	}
	
	public static MenuManager createSourceMenuSkeleton() {
		MenuManager sourceMenu = new MenuManager("Source", SOURCE_MENU_ID);
		sourceMenu.add(new Separator(SOURCE_MENU_GroupComment));
		sourceMenu.add(new Separator(SOURCE_MENU_GroupFormat));
		sourceMenu.add(new Separator(SOURCE_MENU_GroupAdditions));
		return sourceMenu;
	}
	
	@Override
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
		
		IMenuManager sourceMenu = menu.findMenuUsingPath(SOURCE_MENU_ID);
		if(sourceMenu == null) {
			// This structure should have been created declaratively by plugin.xml, 
			LangCore.logError("Source menu " + SOURCE_MENU_ID + " not created by plugin.xml!");

			// We can create it programmatically, by other plugin.xml declarative menu contribution will
			// fail to find the menu because it will be created too late.
			sourceMenu = createSourceMenuSkeleton();
			menu.insertAfter(IWorkbenchActionConstants.M_EDIT, sourceMenu);
		}
		
		contributeSourceMenu(sourceMenu);
		
		IMenuManager editMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.appendToGroup(ITextEditorActionConstants.GROUP_ASSIST, createEditorActionContribution(
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, 
				ITextEditorActionConstants.CONTENT_ASSIST));
			
			editMenu.appendToGroup(ITextEditorActionConstants.GROUP_ASSIST, createEditorActionContribution(
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION, 
				ITextEditorActionConstants.CONTENT_ASSIST_CONTEXT_INFORMATION));
		}
		
		IMenuManager navigateMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE);
		if (navigateMenu != null) {
			
		}
		
	}
	
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		// TODO: use pure command contributions?
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, createEditorActionContribution(
			ITextEditorActionDefinitionIds.SHIFT_LEFT, ITextEditorActionConstants.SHIFT_LEFT));
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, createEditorActionContribution(
			ITextEditorActionDefinitionIds.SHIFT_RIGHT, ITextEditorActionConstants.SHIFT_RIGHT));
	}
	
}