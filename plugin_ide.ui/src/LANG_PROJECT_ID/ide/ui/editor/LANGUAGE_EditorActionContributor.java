/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.editor;

import org.eclipse.ui.texteditor.ITextEditor;

import LANG_PROJECT_ID.ide.ui.actions.LANGUAGE_OpenDefinitionOperation;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.ide.ui.editor.LangEditorActionContributor;

public class LANGUAGE_EditorActionContributor extends LangEditorActionContributor {
	
	@Override
	protected LANGUAGE_OpenDefinitionOperation createOpenDefinitionOperation(ITextEditor editor,
			SourceRange range, OpenNewEditorMode newEditorMode) {
		return new LANGUAGE_OpenDefinitionOperation(editor, range, newEditorMode);
	}
	
	@Override
	protected void registerOtherEditorHandlers() {
	}
	
}