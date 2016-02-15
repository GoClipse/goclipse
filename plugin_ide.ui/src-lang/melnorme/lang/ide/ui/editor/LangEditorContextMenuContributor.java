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
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.EditorSettings_Actual.EditorCommandIds;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.actions.CommandsHelper;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public abstract class LangEditorContextMenuContributor extends CommandsHelper {
	
	protected final IServiceLocator svcLocator;
	
	public LangEditorContextMenuContributor(IServiceLocator svcLocator) {
		this.svcLocator = assertNotNull(svcLocator);
	}
	
	public void editorContextMenuAboutToShow(IMenuManager ctxMenu) {
		ctxMenu.prependToGroup(ICommonMenuConstants.GROUP_OPEN, new CommandContributionItem(
			createCommandContrib_OpenDefinitionContrib()));
		
		ctxMenu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, new CommandContributionItem(
			createCommandContrib_QuickOutline()));
		
		prepareSourceMenu(ctxMenu);
	}
	
	protected CommandContributionItemParameter createCommandContrib_OpenDefinitionContrib() {
		return contribItemParameter2(svcLocator, EditorCommandIds.OpenDef_ID, 
			LangImages.ACTIONS_OPEN_DEF.getDescriptor());
	}
	
	protected CommandContributionItemParameter createCommandContrib_QuickOutline() {
		return contribItemParameter(svcLocator, EditorCommandIds.QuickOutline);
	}
	
	/* ----------------- Source menu ----------------- */
	
	public static final String SOURCE_MENU_GroupComment = "comment";
	public static final String SOURCE_MENU_GroupFormat = "format";
	public static final String SOURCE_MENU_GroupAdditions = "additions";
	
	public static MenuManager createSourceMenuSkeleton() {
		MenuManager sourceMenu = new MenuManager("Source", LangEditorActionContributor.SOURCE_MENU_ID);
		sourceMenu.add(new Separator(SOURCE_MENU_GroupComment));
		sourceMenu.add(new Separator(SOURCE_MENU_GroupFormat));
		sourceMenu.add(new Separator(SOURCE_MENU_GroupAdditions));
		return sourceMenu;
	}
	
	protected void prepareSourceMenu(IMenuManager ctxMenu) {
		ctxMenu.remove(ITextEditorActionConstants.SHIFT_RIGHT);
		ctxMenu.remove(ITextEditorActionConstants.SHIFT_LEFT);
		
		
		// I wonder if it's ok to reuse the same menu id as the workbench menu
		IMenuManager sourceMenu = createSourceMenuSkeleton();
		
		contributeSourceMenu(sourceMenu);
		ctxMenu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT, sourceMenu);
	}
	
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		sourceMenu.appendToGroup(SOURCE_MENU_GroupComment, 
			pushItem(svcLocator, EditorCommandIds.ToggleComment));
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, 
			pushItem(svcLocator, ITextEditorActionDefinitionIds.SHIFT_RIGHT));
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, 
			pushItem(svcLocator, ITextEditorActionDefinitionIds.SHIFT_LEFT));
		
		sourceMenu.appendToGroup(SOURCE_MENU_GroupFormat, 
			pushItem(svcLocator, EditorCommandIds.Format));
	}
	
}