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
package melnorme.lang.ide.ui.editor.actions;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.actions.UIUserInteractionsHelper;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This is a an editor handler that manages its enablement state so as to be restricted to a certaint type
 * of editors only. It is meant to be used by {@link TextEditorActionContributor}'s, for which there is 
 * a single instance per multiple editor ids. 
 */
public abstract class AbstractEditorHandler extends AbstractHandler {
	
	protected final IWorkbenchPage page;
	
	public AbstractEditorHandler(IWorkbenchPage page) {
		this.page = assertNotNull(page);
	}
	
	protected abstract String getOperationName();
	
	protected Shell getShell() {
		return page.getWorkbenchWindow().getShell();
	}
	
	protected ITextEditor getTextEditor() {
		IEditorPart activeEditor = page.getActiveEditor();
		if(activeEditor instanceof ITextEditor) {
			return (ITextEditor) activeEditor;
		}
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled() && EditorSettings_Actual.editorKlass().isInstance(page.getActiveEditor());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ITextEditor editor = (ITextEditor) HandlerUtil.getActiveEditorChecked(event);
		
		new LangEditorRunner(getShell(), editor) {
			@Override
			protected void runWithLangEditor(AbstractLangEditor editor) {
				try {
					doRunWithEditor(editor);
				} catch(CoreException ce) {
					UIOperationExceptionHandler.handleOperationStatus(getOperationName(), ce);
				} catch(CommonException ce) {
					UIOperationExceptionHandler.handleOperationStatus(getOperationName(), ce);
				}
			}
			
			@Override
			protected void handleInternalError(String message) {
				AbstractEditorHandler.this.handleInternalError(message);
			};
		}.run();
		
		return null;
	}
	
	protected void handleInternalError(String message) {
		handleError(LangUIMessages.InternalError + ": " + message);
	}
	
	protected void handleError(String message) {
		UIUserInteractionsHelper.openError(getShell(), getOperationName(), message);
	}
	
	/** editor: not null */
	protected abstract void doRunWithEditor(AbstractLangEditor editor) throws CoreException, CommonException;
	
}