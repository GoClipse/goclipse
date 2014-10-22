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

import static melnorme.utilbox.core.CoreUtil.listFrom;
import melnorme.lang.ide.ui.actions.AbstractEditorHandler;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class RunGoFmtOperation extends AbstractEditorGoToolOperation {
	
	public static final AbstractEditorHandler handler = new AbstractEditorHandler() {
		@Override
		public void runOperation(ITextEditor editor) {
			new RunGoFmtOperation(editor).executeAndHandle();
		}
	};
	
	public RunGoFmtOperation(ITextEditor editor) {
		super("Run 'gofmt'", editor);
	}
	
	@Override
	protected void prepareProcessBuilder(GoEnvironment goEnv) throws CoreException {
		toolPath = GoEnvironmentPrefs.FORMATTER_PATH.get();
		pb = goEnv.createProcessBuilder(listFrom(toolPath));
	}
	
}