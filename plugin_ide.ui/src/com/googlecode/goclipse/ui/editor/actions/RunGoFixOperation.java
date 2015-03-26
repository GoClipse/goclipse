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

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class RunGoFixOperation extends AbstractEditorGoToolOperation {
	
	protected static final String RUN_GO_FIX_OpName = "Run 'go fix'";
	
	public static AbstractEditorHandler getHandler(IWorkbenchPage page) {
		return new AbstractEditorHandler(page) {
			
			@Override
			protected String getOperationName() {
				return RUN_GO_FIX_OpName;
			}
			
			@Override
			protected void doRunWithEditor(AbstractLangEditor editor) {
				new RunGoFixOperation(editor).executeAndHandle();
			}
		};
	}
	
	public RunGoFixOperation(ITextEditor editor) {
		super(RUN_GO_FIX_OpName, editor);
	}
	
	@Override
	protected void prepareProcessBuilder(GoEnvironment goEnv) throws CoreException {
		try {
			toolPath = goEnv.getGoRootToolsDir().resolve("fix").toString();
		} catch (CommonException se) {
			throw GoCore.createCoreException(se.getMessage(), se.getCause());
		}
		pb = goEnv.createProcessBuilder(listFrom(toolPath));
	}
	
}