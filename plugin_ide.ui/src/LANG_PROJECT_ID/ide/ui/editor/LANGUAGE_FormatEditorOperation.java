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
package LANG_PROJECT_ID.ide.ui.editor;

import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.editor.actions.AbstractEditorToolOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class LANGUAGE_FormatEditorOperation extends AbstractEditorToolOperation<Object> {
	
	public LANGUAGE_FormatEditorOperation(String operationName, ITextEditor editor) {
		super(operationName, editor);
	}
	
	@Override
	protected Object doBackgroundToolResultComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation, OperationSoftFailure {
		// TODO: Lang
		throw new CommonException("Operation not implemented");
	}
	
	@Override
	protected void handleResultData(Object resultData) throws CommonException {
	}
	
}