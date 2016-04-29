/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor.actions;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.operations.AbstractEditorOperation2;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractEditorGoToolOperation extends AbstractEditorOperation2<String> {
	
	protected IProject project;
	protected ProcessBuilder pb;
	
	public AbstractEditorGoToolOperation(String operationName, ITextEditor editor) {
		super(operationName, editor);
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		
		project = EditorUtils.getAssociatedProject(editorInput);
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		pb = prepareProcessBuilder(goEnv);
	}
	
	protected abstract ProcessBuilder prepareProcessBuilder(GoEnvironment goEnv) throws CommonException;
	
	@Override
	protected String doBackgroundValueComputation(IProgressMonitor monitor)
			throws CommonException, OperationCancellation {
		
		ToolManager toolMgr = LangCore.getToolManager();
		
		String editorText = doc.get();
		ExternalProcessResult processResult = toolMgr.runEngineTool(pb, editorText, monitor);
		ProcessUtils.validateNonZeroExitValue(processResult.exitValue);
		
		return processResult.getStdOutBytes().toString();
	}
	
	@Override
	protected void handleComputationResult() throws CommonException {
		assertNotNull(result);
		
		setEditorTextPreservingCarret(result);
	}
	
}