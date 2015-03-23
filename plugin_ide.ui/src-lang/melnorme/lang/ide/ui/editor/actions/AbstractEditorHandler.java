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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractEditorHandler extends AbstractHandler {
	
	protected final IWorkbenchPage page;
	
	public AbstractEditorHandler(IWorkbenchPage page) {
		this.page = assertNotNull(page);
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled() && EditorSettings_Actual.editorKlass().isInstance(page.getActiveEditor());
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ITextEditor editor = (ITextEditor) HandlerUtil.getActiveEditorChecked(event);
		if(editor == null) {
			throw new ExecutionException(LangUIMessages.INTERNAL_ERROR_NoEditorForCommand);
		}
		runWithEditor(editor);
		
		return null;
	}
	
	public abstract void runWithEditor(ITextEditor editor);
	
}