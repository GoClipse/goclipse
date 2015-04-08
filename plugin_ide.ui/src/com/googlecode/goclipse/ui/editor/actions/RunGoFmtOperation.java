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
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorHandler;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class RunGoFmtOperation extends AbstractEditorGoToolOperation {
	
	protected static final String RUN_GOFMT_OpName = "Run 'gofmt'";
	
	public static AbstractEditorHandler getHandler(IWorkbenchPage page) {
		return new AbstractEditorHandler(page) {
			@Override
			protected String getOperationName() {
				return RUN_GOFMT_OpName;
			}
			
			@Override
			protected void doRunWithEditor(AbstractLangEditor editor) {
				new RunGoFmtOperation(editor).executeAndHandle();
			}
		};
	}
	
	public RunGoFmtOperation(ITextEditor editor) {
		super(RUN_GOFMT_OpName, editor);
	}
	
	@Override
	protected void prepareProcessBuilder(GoEnvironment goEnv) throws CoreException, CommonException {
		toolPath = GoEnvironmentPrefs.FORMATTER_PATH.get();
		pb = goEnv.createProcessBuilder(listFrom(toolPath), null, true);
	}
	
}