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
package melnorme.lang.ide.ui.utils.operations;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class BasicEditorOperation extends BasicUIOperation {
	
	protected final IEditorPart editorPart;
	
	public BasicEditorOperation(String operationName, IEditorPart editorPart) {
		super(operationName);
		this.editorPart = assertNotNull(editorPart);
	}
	
	@Override
	protected void handleError(CommonException ce) {
		UIOperationsStatusHandler.handleOperationStatus(getOperationName(), ce);
	}
	
	@Override
	protected void doOperation() throws CoreException, CommonException, OperationCancellation {
		if(editorPart instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editorPart;
			doRunWithEditor(langEditor);
		} else {
			throw new CommonException("Internal Error: editor is AbstractLangEditor.");
		}
	}
	
	/** editor: not null */
	protected abstract void doRunWithEditor(AbstractLangEditor editor) throws CoreException, CommonException;
	
}