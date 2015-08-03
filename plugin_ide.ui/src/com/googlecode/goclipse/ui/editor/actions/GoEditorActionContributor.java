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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.ui.actions.GoOracleOpenDefinitionOperation;

import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.LangEditorActionContributor;
import melnorme.lang.tooling.ast.SourceRange;

public class GoEditorActionContributor extends LangEditorActionContributor implements GoCommandConstants {
	
	public GoEditorActionContributor() {
	}
	
	@Override
	protected GoOracleOpenDefinitionOperation createOpenDefinitionOperation(ITextEditor editor, SourceRange range,
			OpenNewEditorMode newEditorMode) {
		return new GoOracleOpenDefinitionOperation(editor, range, newEditorMode);
	}
	
	@Override
	protected void registerOtherEditorHandlers() {
	}
	
	@Override
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		super.contributeSourceMenu(sourceMenu);
		
		activateHandler(COMMAND_RunGoFmt, RunGoFmtOperation.getHandler(getPage()));
	}
	
}