/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.actions;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.ide.ui.utils.operations.AbstractEditorOperation2;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.StatusMessage;

public abstract class AbstractEditorToolOperation<RESULT> extends AbstractEditorOperation2<ToolResponse<RESULT>> {
	
	public boolean handleSoftFailureWithDialog = true;
	
	public AbstractEditorToolOperation(String operationName, ITextEditor editor) {
		super(operationName, editor);
	}
	
	public AbstractEditorToolOperation(String operationName, ITextEditor editor, SourceRange range) {
		super(operationName, editor, range);
	}
	
	public IToolOperationService getToolService() {
		return LangCore.getToolManager().getEngineToolsOperationService();
	}
	
	@Override
	protected final ToolResponse<RESULT> doBackgroundValueComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		try {
			return new ToolResponse<>(doBackgroundToolResultComputation(om));
		} catch(OperationSoftFailure e) {
			return new ToolResponse<>(null, e.getMessage());
		}
	};
	
	protected abstract RESULT doBackgroundToolResultComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation, OperationSoftFailure;
	
	@Override
	public void prepareAndCalculateResult() throws CommonException, OperationCancellation {
		super.prepareAndCalculateResult();
		assertNotNull(result);
	}
	
	@Override
	protected void handleComputationResult(ToolResponse<RESULT> response) throws CommonException {
		assertNotNull(result);
		
		if(response.getResultData() != null) {
			handleResultData(response.getResultData());
		}
		
		String errorMessage = response.getStatusMessageText();
		if(errorMessage != null) {
			handleStatus(new StatusMessage(errorMessage));
		}
	}
	
	protected abstract void handleResultData(RESULT resultData) throws CommonException;
	
	protected void handleStatus(IStatusMessage status) {
		String statusMessage = status.getMessage().trim();
		
		if(handleSoftFailureWithDialog) {
			// Use a dialog
			UIOperationsStatusHandler.displayStatusMessage(operationName, status.getSeverity(), statusMessage);
		} else {
			// Note, message may have newlines, if so, only first line will be displayed
			EditorUtils.setStatusLineErrorMessage(editor, statusMessage, null);
			Display.getCurrent().beep();
		}
	}
	
}