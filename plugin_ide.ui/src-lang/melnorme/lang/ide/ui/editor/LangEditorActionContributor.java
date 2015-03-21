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
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.actions.AbstractEditorOperation;
import melnorme.lang.ide.ui.actions.AbstractEditorOperationHandler;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public abstract class LangEditorActionContributor extends LangEditorActionContributorHelper {
	
	public static final String SOURCE_MENU_ID = LangUIPlugin.PLUGIN_ID + ".sourceMenu";
	
	public LangEditorActionContributor() {
		super();
	}
	
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		
		// Register some handlers active only when editor is active:
		
		getHandlerService().activateHandler(EditorSettings_Actual.EditorCommandIds.OpenDef_ID, 
			getOpenDefinition_Handler());
		
		registerOtherEditorHandlers();
	}
	
	protected AbstractHandler getOpenDefinition_Handler() {
		return new AbstractEditorOperationHandler(getPage()) {
			
			@Override
			public AbstractEditorOperation createOperation(ITextEditor editor) {
				OpenNewEditorMode newEditorMode = OpenNewEditorMode.TRY_REUSING_EXISTING_EDITORS;
				return createOpenDefinitionOperation(editor, EditorUtils.getSelectionSR(editor), newEditorMode);
			}
		};
	}
	
	protected void registerOtherEditorHandlers() {
	}
	
	protected abstract AbstractEditorOperation createOpenDefinitionOperation(ITextEditor editor, SourceRange range,
			OpenNewEditorMode newEditorMode);
	
	
	@Override
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
		
		prepareEditMenu(menu);
		
		prepareSourceMenu(menu);
		
		IMenuManager navigateMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE);
		if (navigateMenu != null) {
			
		}
		
	}
	
	protected void prepareEditMenu(IMenuManager menu) {
		IMenuManager editMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if(editMenu != null) {
			editMenu.appendToGroup(ITextEditorActionConstants.GROUP_ASSIST, pushItem(
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, 
				ITextEditorActionConstants.CONTENT_ASSIST));
			
			editMenu.appendToGroup(ITextEditorActionConstants.GROUP_ASSIST, pushItem(
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION, 
				ITextEditorActionConstants.CONTENT_ASSIST_CONTEXT_INFORMATION));
		}
		
	}
	
	protected void prepareSourceMenu(IMenuManager menu) {
		IMenuManager sourceMenu = menu.findMenuUsingPath(SOURCE_MENU_ID);
		if(sourceMenu == null) {
			// This structure should have been created declaratively by plugin.xml, 
			LangCore.logError("Source menu " + SOURCE_MENU_ID + " not created by plugin.xml!");

			// We can create it programmatically, by other plugin.xml declarative menu contribution will
			// fail to find the menu because it will be created too late.
			sourceMenu = LangEditorContextMenuContributor.createSourceMenuSkeleton();
			menu.insertAfter(IWorkbenchActionConstants.M_EDIT, sourceMenu);
		}
		
		contributeSourceMenu(sourceMenu);
	}
	
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		EditorSettings_Actual.createCommandsContribHelper(getServiceLocator()).contributeSourceMenu(sourceMenu);
	}
	
}