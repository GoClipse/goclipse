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
package com.googlecode.goclipse.ui.actions;

import melnorme.lang.ide.ui.actions.AbstractEditorHandler;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.editors.PairMatcher;

public class GotoMatchingBracketHandler extends AbstractEditorHandler  {
	
	public GotoMatchingBracketHandler(IWorkbenchPage page) {
		super(page);
	}
	
	@Override
	public void runOperation(ITextEditor editor) {
		PairMatcher.goToMatchingBracket(editor);
	}
	
}