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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.tools.console.DaemonToolMessageConsole;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.oracle.GoOracleFindDefinitionOperation;

public class GoOracleOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition (go oracle)";
	
	public GoOracleOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor, range, openEditorMode);
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		super.prepareOperation();
		
		editor.doSave(new NullProgressMonitor());
	}
	
	@Override
	protected FindDefinitionResult performLongRunningComputation_doAndGetResult(IProgressMonitor monitor) 
			throws CoreException, OperationCancellation {
		String goOraclePath = GoToolPreferences.GO_ORACLE_Path.get();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		try {
			GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation(goOraclePath);
			ProcessBuilder pb = op.createProcessBuilder(goEnv, inputLoc, range.getOffset());
			
			ExternalProcessResult result = GoToolManager.getDefault().runEngineTool(pb, null, monitor);
			if(result.exitValue != 0) {
				statusErrorMessage = "Go oracle did not complete successfully.";
				return null;
			}
			
			return op.parseToolResult(result);
		} catch (CommonException se) {
			throw LangCore.createCoreException(se.getMessage(), se.getCause());
		}
	}
	
	@Override
	protected void handleStatusErrorMessage() {
		DaemonToolMessageConsole engineToolsConsole = DaemonToolMessageConsole.getConsole();
		engineToolsConsole.activate();
		
		super.handleStatusErrorMessage();
	}
	
}