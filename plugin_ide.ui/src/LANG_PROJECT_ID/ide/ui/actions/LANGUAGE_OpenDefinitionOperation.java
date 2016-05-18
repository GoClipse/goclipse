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
import melnorme.lang.tooling.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.FindDefinitionResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.DevelopmentCodeMarkers;

public class LANGUAGE_OpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public LANGUAGE_OpenDefinitionOperation(ITextEditor editor, SourceRange range, 
			OpenNewEditorMode openEditorMode) {
		super(LangUIMessages.Op_OpenDefinition_Name, editor, range, openEditorMode);
	}
	
	@Override
	protected FindDefinitionResult performLongRunningComputation_doAndGetResult(IOperationMonitor monitor)
			throws OperationCancellation {
		if(DevelopmentCodeMarkers.UNIMPLEMENTED_FUNCTIONALITY) {
			
		}
		return null; // TODO: Lang
	}
	
	@Override
	protected void handleComputationResult() throws CommonException  {
		if(result == null) {
			throw new CommonException("Feature not implemented.");
		}
		
		super.handleComputationResult();
	}
	
}