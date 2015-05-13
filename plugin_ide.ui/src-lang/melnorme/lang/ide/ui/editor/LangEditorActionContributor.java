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
import melnorme.lang.ide.ui.EditorSettings_Actual.EditorCommandIds;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorOperation;
import melnorme.lang.ide.ui.editor.actions.GoToMatchingBracketHandler;
import melnorme.lang.ide.ui.editor.actions.OpenQuickOutlineHandler;
import melnorme.lang.ide.ui.editor.actions.ToggleCommentHandler;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public abstract class LangEditorActionContributor extends LangEditorActionContributorHelper {
	
	public static final String SOURCE_MENU_ID = LangUIPlugin.PLUGIN_ID + ".sourceMenu";
	
	protected final ArrayList2<IHandlerActivation> handlerActivations = new ArrayList2<>(); 
	
	public LangEditorActionContributor() {
		super();
	}
	
	protected void activateHandler(String string, AbstractHandler handler) {
		IHandlerActivation handlerActivation = getHandlerService_2().activateHandler(string, handler);
		handlerActivations.add(handlerActivation);
	}
	
	@Override
	public final void dispose() {
		doDispose();
		
		for (IHandlerActivation handlerActivation : handlerActivations) {
			getHandlerService_2().deactivateHandler(handlerActivation);
		}
		
		super.dispose();
	}
	
	protected void doDispose() {
	}
	
	/* ----------------- Register handlers ----------------- */ 
	
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		
		// Register handlers active only when editor is active:
		
		activateHandler(EditorCommandIds.OpenDef_ID, getHandler_OpenDefinition());
		activateHandler(EditorCommandIds.GoToMatchingBracket, getHandler_GoToMatchingBracket());
		activateHandler(EditorCommandIds.ToggleComment, getHandler_ToggleComment());
		
		activateHandler(EditorCommandIds.QuickOutline, getHandler_QuickOutline());
		
		registerOtherEditorHandlers();
	}
	
	protected AbstractHandler getHandler_OpenDefinition() {
		return new AbstractEditorOperationHandler() {
			@Override
			protected String getOperationName() {
				return LangUIMessages.Op_OpenDefinition_Name;
			}
			
			@Override
			public AbstractEditorOperation createOperation(ITextEditor editor) {
				OpenNewEditorMode newEditorMode = OpenNewEditorMode.TRY_REUSING_EXISTING;
				SourceRange selection = EditorUtils.getSelectionSR(editor);
				return createOpenDefinitionOperation(editor, selection, newEditorMode);
			}
		};
	}
	
	protected abstract AbstractEditorOperation createOpenDefinitionOperation(ITextEditor editor, SourceRange range,
			OpenNewEditorMode newEditorMode);
	
	
	protected abstract void registerOtherEditorHandlers();
	
	
	protected AbstractHandler getHandler_GoToMatchingBracket() {
		return new GoToMatchingBracketHandler(getPage());
	}
	
	protected AbstractHandler getHandler_ToggleComment() {
		return new ToggleCommentHandler(getPage());
	}
	
	protected AbstractHandler getHandler_QuickOutline() {
		return new OpenQuickOutlineHandler(getPage());
	}
	
	/* ----------------- Menu / Toolbar contributions ----------------- */
	
	@Override
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
		
		prepareEditMenu(menu);
		
		prepareSourceMenu(menu);
		
		prepareNavigateMenu(menu);
		
	}
	
	protected void prepareNavigateMenu(IMenuManager menu) {
		IMenuManager navigateMenu = menu.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE);
		if(navigateMenu != null) {
			navigateMenu.appendToGroup(IWorkbenchActionConstants.SHOW_EXT, pushItem(EditorCommandIds.QuickOutline));
		}
		
		IMenuManager gotoMenu = menu.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE + "/goTo");
		if(gotoMenu != null) {
			gotoMenu.add(new Separator("additions2"));
			
			gotoMenu.appendToGroup("additions2", pushItem(EditorCommandIds.GoToMatchingBracket));
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