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
package melnorme.lang.ide.ui.actions;


import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractOpenDefinitionHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ITextEditor editor = (ITextEditor) HandlerUtil.getActiveEditorChecked(event);
		if(editor == null) {
			throw new ExecutionException("Internal error: No editor for action");
		}
		
		TextSelection sel = EditorUtils.getSelection(editor);
		SourceRange range = new SourceRange(sel.getOffset(), sel.getLength());
		
		createOperation(editor, range).executeAndHandle();
		
		return null;
	}
	
	public abstract AbstractOpenElementOperation createOperation(ITextEditor editor, SourceRange range);
	
}