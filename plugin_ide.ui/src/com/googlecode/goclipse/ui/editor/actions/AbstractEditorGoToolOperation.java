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


import static melnorme.lang.ide.ui.editor.EditorUtils.getEditorDocument;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public abstract class AbstractEditorGoToolOperation extends AbstractEditorOperation {
	
	protected GoEditor goEditor;
	protected String editorText;
	protected String toolPath;
	protected ProcessBuilder pb;
	
	protected String outputText;

	public AbstractEditorGoToolOperation(String operationName, ITextEditor editor) {
		super(operationName, editor);
	}
	
	@Override
	protected void prepareOperation() throws CoreException {
		if(!(editor instanceof GoEditor)) {
			throw LangCore.createCoreException("Editor is not a GoEditor.", null);
		}
		goEditor = (GoEditor) editor;
		editorText = getEditorDocument(editor).get();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(null);
		
		try {
			prepareProcessBuilder(goEnv);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected abstract void prepareProcessBuilder(GoEnvironment goEnv) throws CoreException, CommonException;
	
	@Override
	protected void performLongRunningComputation_do(IProgressMonitor pm) throws CoreException, OperationCancellation {
		
		ExternalProcessResult processResult = 
				GoToolManager.getDefault().newRunToolTask(pb, null, pm).runProcess(editorText, true);
		
		outputText = processResult.getStdOutBytes().toString();
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		if (!outputText.equals(editorText)) {
			GoEditor.replaceText(goEditor.getSourceViewer_(), outputText);
		}
	}
	
}