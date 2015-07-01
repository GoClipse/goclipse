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


import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.actions.CommandsHelper;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorHandler;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorOperation2;
import melnorme.utilbox.collections.ArrayList2;

public class LangEditorActionContributorHelper extends TextEditorActionContributor {
	
	public static interface IActiveEditorListener {
		
		public abstract void setActiveEditor(IEditorPart part);
		
	}
	
	protected final ArrayList2<IActiveEditorListener> activeEditorListeners = new ArrayList2<>();
	
	public LangEditorActionContributorHelper() {
	}
	
	protected IServiceLocator getServiceLocator() {
		return getPage().getWorkbenchWindow();
	}
	
	protected IHandlerService getHandlerService_2() {
		return (IHandlerService) getServiceLocator().getService(IHandlerService.class);
	}
	
	protected <T extends IActiveEditorListener> T registerActiveEditorListener(T activeEditorListener) {
		activeEditorListeners.add(activeEditorListener);
		return activeEditorListener;
	}
	
	@Override
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		
		for (IActiveEditorListener activeEditorListener : activeEditorListeners) {
			activeEditorListener.setActiveEditor(part);
		}
	}
	
	/* -----------------  ----------------- */
	
	public abstract class AbstractEditorOperationHandler extends AbstractEditorHandler {
		
		public AbstractEditorOperationHandler() {
			super(getPage());
		}
		
		@Override
		protected void doRunWithEditor(AbstractLangEditor editor) {
			createOperation(editor).executeAndHandle();
		}
		
		public abstract AbstractEditorOperation2<?> createOperation(ITextEditor editor);
		
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
	}
	
	protected CommandContributionItem pushItem(String commandId, String contribId) {
		return CommandsHelper.pushItem(getServiceLocator(), commandId, contribId);
	}
	
	protected CommandContributionItem pushItem(String commandId) {
		return CommandsHelper.pushItem(getServiceLocator(), commandId, commandId);
	}
	
}