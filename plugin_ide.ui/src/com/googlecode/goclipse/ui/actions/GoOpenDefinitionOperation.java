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
package com.googlecode.goclipse.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.oracle.GoFindDefinitionOperation;
import com.googlecode.goclipse.tooling.oracle.GoOperationContext;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.editor.EditorSourceBuffer;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.FindDefinitionResult;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.StatusException;

public class GoOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition";
	
	public GoOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor, range, openEditorMode);
	}
	
	@Override
	protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
		if(true) {
			super.executeBackgroundOperation();
			return;
		}
		/* FIXME: need to review this code, add this save logic to  GoFindDefinitionOperation2 */
		
//		CommonException originalException = null;
//		
//		try {
//			super.executeBackgroundOperation();
//			if(statusErrorMessage == null) {
//				return; // godef completed successfully
//			}
//		} catch(CommonException e) {
//			originalException = e;
//		}
//		
//		// retry with guru
//		GoOpenDefinitionOperation guruOp = new GoOpenDefinitionOperation(editor, getOperationRange(), openEditorMode) {
//			@Override
//			protected void handleComputationResult() throws CommonException {
//				// Ignore 
//				//super.handleComputationResult();
//			}
//		};
//		try {
//			saveEditor(new NullProgressMonitor());
//			guruOp.prepareOperation();
//			guruOp.executeBackgroundOperation();
//			if(guruOp.statusErrorMessage == null)  {
//				result = guruOp.result;
//				// Use guruOp result
//				return;
//			}
//		} catch(CommonException e) {
//			// Ignore, use original result
//		}
//		
//		if(originalException != null) {
//			throw originalException;
//		}
//		
	}
	
	@Override
	protected FindDefinitionResult performLongRunningComputation_doAndGetResult(IOperationMonitor cm) 
			throws CommonException, OperationCancellation {
		
		ToolResponse<FindDefinitionResult> opResult = getToolOperation().executeOp(cm);
		try {
			return opResult.getValidResult();
		} catch(CommonException e) {
			statusErrorMessage = e.getMessage();
			return null;
		}
	}
	
	public GoFindDefinitionOperation getToolOperation() {
		return getFindDefinitionOperation(new EditorSourceBuffer(editor), getOperationOffset());
	}
	
	public static GoFindDefinitionOperation getFindDefinitionOperation(ISourceBuffer sourceBuffer,
			int offset) {
		return GoOpenDefinitionOperation.getFindDefOperation(getOperationContext(sourceBuffer, offset));
	}
	
	public static GoOperationContext getOperationContext(ISourceBuffer sourceBuffer, int offset) {
		SourceOpContext opContext = sourceBuffer.getSourceOpContext(new SourceRange(offset, 0));
		
		IProject project = ResourceUtils.getProject(opContext.getOptionalFileLocation());
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		IToolOperationService toolsOpService = LangCore.getToolManager().getEngineToolsOperationService();
		return new GoOperationContext(sourceBuffer, opContext, toolsOpService, goEnv);
	}
	
	public static GoFindDefinitionOperation getFindDefOperation(GoOperationContext operationContext) {
		return new GoFindDefinitionOperation2(operationContext);
	}
	
	public static class GoFindDefinitionOperation2 extends GoFindDefinitionOperation {
		public GoFindDefinitionOperation2(GoOperationContext goOperationContext) {
			super(goOperationContext);
		}
		
		@Override
		protected String getGodefPath() throws StatusException {
			return GoToolPreferences.GODEF_Path.getDerivedValue().toString();
		}
		
		@Override
		protected String getGuruPath() throws StatusException {
			return GoToolPreferences.GO_GURU_Path.getDerivedValue().toString();
		}
		
		@Override
		public ToolResponse<FindDefinitionResult> executeOp(IOperationMonitor cm)
				throws CommonException, OperationCancellation {
			return super.executeOp(cm);
		}
	}
	
}