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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.tryCast;
import melnorme.lang.ide.ui.actions.AbstractEditorOperation;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.LangEditorActionContributor;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.ui.actions.GoOracleOpenDefinitionOperation;
import com.googlecode.goclipse.ui.actions.GotoMatchingBracketHandler;

public class GoEditorActionContributor extends LangEditorActionContributor implements GoCommandConstants {
	
	public static final String SOURCE_MENU_ID = "com.googlecode.goclipse.ui.sourceMenu";
	
	public GoEditorActionContributor() {
	}
	
	@Override
	protected AbstractEditorOperation createOpenDefinitionOperation(ITextEditor editor, SourceRange range,
			OpenNewEditorMode newEditorMode) {
		return new GoOracleOpenDefinitionOperation(editor, range, newEditorMode);
	}
	
	@Override
	protected void registerOtherEditorHandlers() {
		getHandlerService().activateHandler(COMMAND_GoToMatchingBracket, new GotoMatchingBracketHandler(getPage()));
	}
	
	@Override
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		super.contributeSourceMenu(sourceMenu);
		
		// TODO: Lang Toggle Line Comment
		createEditorActionContribution(COMMAND_ToggleLineComment, "ToggleComment");
//		getHandlerService().activateHandler(COMMAND_ToggleLineComment, ToggleCommentAction.getHandler(getPage()));
		
		getHandlerService().activateHandler(COMMAND_RunGoFix, RunGoFixOperation.getHandler(getPage()));
		getHandlerService().activateHandler(COMMAND_RunGoFmt, RunGoFmtOperation.getHandler(getPage()));
	}
	
	/* -----------------  ----------------- */
	
	@Deprecated
	protected void createEditorActionContribution(String commandId, String editorActionId) {
		final EditorActionContribution editorActionHelper = new EditorActionContribution(
			getHandlerService(), commandId, editorActionId);
		registerActiveEditorListener(editorActionHelper);
	}
	
	public static abstract class AbstractEditorCommandHelper implements IActiveEditorListener {
		
		protected final IHandlerService handlerService;
		protected final String commandId;
		
		protected IHandlerActivation handlerActivation;
		
		public AbstractEditorCommandHelper(IHandlerService handlerService, String commandId) {
			this.handlerService = assertNotNull(handlerService);
			this.commandId = assertNotNull(commandId);
		}
		
		public String getCommandId() {
			return commandId;
		}
		
		@Override
		public void setActiveEditor(IEditorPart part) {
			
			if(handlerActivation != null) {
				handlerService.deactivateHandler(handlerActivation);
				handlerActivation = null;
			}
			
			IHandler handler = getHandler(part);
			if(handler != null) {
				handlerActivation = handlerService.activateHandler(commandId, handler);
			}
		}
		
		protected abstract IHandler getHandler(IEditorPart editorPart);
		
	}
	
	/**
	 * Helper to contribute programmatically menu contributions based on an editor {@link Action} as a handler.
	 */
	@Deprecated
	public class EditorActionContribution extends AbstractEditorCommandHelper implements IActiveEditorListener {

		protected final String editorActionId;

		public EditorActionContribution(IHandlerService handlerService, String commandId, String editorActionId) {
			super(handlerService, commandId);
			this.editorActionId = assertNotNull(editorActionId);
		}
		
		public CommandContributionItem createContributionItem() {
			return createCommandContribution(commandId);
		}
		
		@Override
		protected ActionHandler getHandler(IEditorPart editorPart) {
			ITextEditor textEditor = tryCast(editorPart, ITextEditor.class);
			if(textEditor == null) 
				return null;
			return new ActionHandler(textEditor.getAction(editorActionId));
		}
		
	}
	
	public CommandContributionItem createCommandContribution(String commandId) {
		return new CommandContributionItem(
			new CommandContributionItemParameter(getServiceLocator(), 
				null, commandId, CommandContributionItem.STYLE_PUSH));
	}
	
}