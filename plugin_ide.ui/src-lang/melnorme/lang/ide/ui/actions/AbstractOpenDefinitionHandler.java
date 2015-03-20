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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractOpenDefinitionHandler extends AbstractEditorHandler {
	
	public AbstractOpenDefinitionHandler(IWorkbenchPage page) {
		super(page);
	}
	
	@Override
	public void runOperation(ITextEditor editor) {
		assertNotNull(editor);
		createOperation(editor, OpenNewEditorMode.TRY_REUSING_EXISTING_EDITORS).executeAndHandle();
	}
	
	public AbstractEditorOperation createOperation(ITextEditor editor, OpenNewEditorMode newEditorMode) {
		TextSelection sel = EditorUtils.getSelection(editor);
		SourceRange range = new SourceRange(sel.getOffset(), sel.getLength());
		return createOperation(editor, range, newEditorMode);
	}
	
	public abstract AbstractEditorOperation createOperation(ITextEditor editor, SourceRange range, 
			OpenNewEditorMode newEditorMode);
	
}