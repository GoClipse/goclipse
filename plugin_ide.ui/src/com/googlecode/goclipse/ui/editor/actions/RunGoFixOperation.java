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
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class RunGoFixOperation extends AbstractEditorGoToolOperation {
	
	public static AbstractEditorHandler getHandler(IWorkbenchPage page) {
		return new AbstractEditorHandler(page) {
			@Override
			public void runOperation(ITextEditor editor) {
				new RunGoFixOperation(editor).executeAndHandle();
			}
		};
	}
	
	public RunGoFixOperation(ITextEditor editor) {
		super("Run 'go fix'", editor);
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