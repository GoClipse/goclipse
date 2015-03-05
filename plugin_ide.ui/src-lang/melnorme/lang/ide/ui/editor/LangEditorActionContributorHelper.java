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
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.tryCast;
import melnorme.lang.ide.ui.actions.AbstractEditorHandler;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.texteditor.ITextEditor;

public class LangEditorActionContributorHelper extends TextEditorActionContributor {
	
	public static interface IActiveEditorListener {
		
		public abstract void setActiveEditor(IEditorPart part);
		
	}
	
	protected final ArrayList2<IActiveEditorListener> activeEditorListeners = new ArrayList2<>();
	
	public LangEditorActionContributorHelper() {
	}
	
	@Override
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
	}
	
	protected IServiceLocator getServiceLocator() {
		return getPage().getWorkbenchWindow();
	}
	
	protected IHandlerService getHandlerService() {
		return (IHandlerService) getServiceLocator().getService(IHandlerService.class);
	}
	
	protected CommandContributionItem createEditorActionContribution(String commandId, String editorActionId) {
		final EditorActionContribution editorActionHelper = new EditorActionContribution(
			getHandlerService(), commandId, editorActionId);
		registerContribution(editorActionHelper);
		return editorActionHelper.createContributionItem(this);
	}
	
	protected <T extends IActiveEditorListener> T registerContribution(T editorContribution) {
		activeEditorListeners.add(editorContribution);
		return editorContribution;
	}
	
	protected CommandContributionItem createCommandContribution(String commandId) {
		return new CommandContributionItem(
			new CommandContributionItemParameter(getServiceLocator(), 
				null, commandId, CommandContributionItem.STYLE_PUSH));
	}
	
	protected CommandContributionItem registerEditorHandler(String commandId, AbstractEditorHandler handler) {
		HandlerContribution hc = new HandlerContribution(getHandlerService(), commandId, handler);
		return registerContribution(hc).createContributionItem();
	}
	
	@Override
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		
		for (IActiveEditorListener activeEditorListener : activeEditorListeners) {
			activeEditorListener.setActiveEditor(part);
		}
	}
	
	/* -----------------  ----------------- */
	
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
	
	public class HandlerContribution extends AbstractEditorCommandHelper implements IActiveEditorListener {
		
		protected final IHandler handler;
		
		public HandlerContribution(IHandlerService handlerService, String commandId, IHandler action) {
			super(handlerService, commandId);
			this.handler = assertNotNull(action);
		}
		
		@Override
		protected IHandler getHandler(IEditorPart editorPart) {
			return handler;
		}
		
		public CommandContributionItem createContributionItem() {
			return createCommandContribution(commandId);
		}
		
	}
	
	/**
	 * Helper to contribute programmatically menu contributions based on an {@link Action} as a handler.
	 */
	public static class ActionContribution extends AbstractEditorCommandHelper implements IActiveEditorListener {
		
		protected final IAction action;
		
		public ActionContribution(IHandlerService handlerService, String commandId, IAction action) {
			super(handlerService, commandId);
			this.action = assertNotNull(action);
		}
		
		@Override
		public void setActiveEditor(IEditorPart part) {
			if(action instanceof IEditorActionDelegate) {
				IEditorActionDelegate editorActionDelegate = (IEditorActionDelegate) action;
				editorActionDelegate.setActiveEditor(action, part);
			}
			super.setActiveEditor(part);
		}
		
		@Override
		protected ActionHandler getHandler(IEditorPart editorPart) {
			return new ActionHandler(action);
		}
		
	}
	
	/**
	 * Helper to contribute programmatically menu contributions based on an editor {@link Action} as a handler.
	 */
	public static class EditorActionContribution extends AbstractEditorCommandHelper implements IActiveEditorListener {

		protected final String editorActionId;

		public EditorActionContribution(IHandlerService handlerService, String commandId, String editorActionId) {
			super(handlerService, commandId);
			this.editorActionId = assertNotNull(editorActionId);
		}
		
		public CommandContributionItem createContributionItem(LangEditorActionContributorHelper actionContributor) {
			return actionContributor.createCommandContribution(commandId);
		}
		
		@Override
		protected ActionHandler getHandler(IEditorPart editorPart) {
			ITextEditor textEditor = tryCast(editorPart, ITextEditor.class);
			if(textEditor == null) 
				return null;
			return new ActionHandler(textEditor.getAction(editorActionId));
		}
		
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * Helper to contribute programmatically menu contributions that use an {@link IEditorActionDelegate}
	 */
	public class ReusableAction extends Action implements IActiveEditorListener {
		
		protected final IEditorActionDelegate actionDelegate;
		
		public ReusableAction(String actionDefinitionid, String text, IEditorActionDelegate ad) {
			super(text);
			this.actionDelegate = ad;
			setActionDefinitionId(actionDefinitionid);
		}
		
		@Override
		public void setActiveEditor(IEditorPart targetEditor) {
			actionDelegate.setActiveEditor(this, targetEditor);
		}
		
		@Override
		public void run() {
			actionDelegate.run(this);
		}
	}
	
}