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
package LANG_PROJECT_ID.ide.ui.actions;

import melnorme.lang.ide.ui.actions.AbstractOpenDefinitionHandler;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

public class LANGUAGE_OpenDefinitionHandler extends AbstractOpenDefinitionHandler  {
	
	public LANGUAGE_OpenDefinitionHandler(IWorkbenchPage page) {
		super(page);
	}
	
	@Override
	public LANGUAGE_OracleOpenDefinitionOperation createOperation(ITextEditor editor, SourceRange range,
			OpenNewEditorMode newEditorMode) {
		return new LANGUAGE_OracleOpenDefinitionOperation(editor, range, newEditorMode);
	}
	
}