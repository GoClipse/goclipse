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
package LANG_PROJECT_ID.ide.ui.actions;

import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class LANGUAGE_OpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public LANGUAGE_OpenDefinitionOperation(ITextEditor editor, SourceRange range, 
			OpenNewEditorMode openEditorMode) {
		super(LangUIMessages.Op_OpenDefinition_Name, editor, range, openEditorMode);
	}
	
	@Override
	protected ToolResponse<SourceLocation> doBackgroundValueComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		throw new CommonException("Feature not implemented.");
	}
	
}