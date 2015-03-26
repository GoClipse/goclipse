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
package LANG_PROJECT_ID.ide.ui.actions;

import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.DevelopmentCodeMarkers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

public class LANGUAGE_OracleOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public LANGUAGE_OracleOpenDefinitionOperation(ITextEditor editor, SourceRange range, 
			OpenNewEditorMode openEditorMode) {
		super(LangUIMessages.Op_OpenDefinition_Name, editor, range, openEditorMode);
	}
	
	@Override
	protected FindDefinitionResult performLongRunningComputation_doAndGetResult(IProgressMonitor monitor)
			throws CoreException, OperationCancellation {
		return null; // TODO: Lang
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		if(DevelopmentCodeMarkers.UNIMPLEMENTED_FUNCTIONALITY) {
		} else {
			MessageDialog.openInformation(editor.getSite().getWorkbenchWindow().getShell(), "Error", "Not implemented.");
			return;
		}
		
		IEditorInput newInput = editorInput;
		
		EditorUtils.openTextEditorAndSetSelection(editor, EditorSettings_Actual.EDITOR_ID, newInput, 
			openEditorMode, new SourceRange(0, 0));
	}
	
}