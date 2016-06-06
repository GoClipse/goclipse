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
package melnorme.lang.ide.ui.editor.actions;


import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.utils.operations.AbstractEditorOperation2;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.FindDefinitionResult;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class AbstractOpenElementOperation extends AbstractEditorOperation2<FindDefinitionResult> {
	
	protected final OpenNewEditorMode openEditorMode;
	
	public AbstractOpenElementOperation(String operationName, ITextEditor editor, SourceRange range, 
			OpenNewEditorMode openEditorMode) {
		super(operationName, editor, range);
		
		this.openEditorMode = openEditorMode;
	}
	
	public int getInvocationOffset() {
		return getOperationOffset();
	}
	
	protected ToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	@Override
	protected FindDefinitionResult doBackgroundValueComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		return performLongRunningComputation_doAndGetResult(om);
	}
	
	protected abstract FindDefinitionResult performLongRunningComputation_doAndGetResult(IOperationMonitor monitor) 
			throws CommonException, OperationCancellation;
	
	@Override
	protected void handleComputationResult() throws CommonException {
		super.handleComputationResult();
		
		if(result == null) {
			Display.getCurrent().beep();
			return;
		}
		
		if(result.getInfoMessage() != null) {
			dialogInfo(result.getInfoMessage());
		}
		
		SourceLineColumnRange sourceRange = result.getSourceRange();
		
		EclipseUtils.run(() -> openEditorForLocation(result.getFileLocation(), sourceRange));
	}
	
	protected void openEditorForLocation(Location fileLoc, SourceLineColumnRange sourceRange) 
			throws CoreException, CommonException {
		IEditorInput newInput = getNewEditorInput(fileLoc);
		
		ITextEditor newEditor = EditorUtils.openTextEditorAndSetSelection(editor, EditorSettings_Actual.EDITOR_ID, 
			newInput, openEditorMode, null);
		
		IDocument doc = EditorUtils.getEditorDocument(newEditor);
		int selectionOffset = getOffsetFrom(doc, sourceRange.line, sourceRange.column);
		
		EditorUtils.setEditorSelection(newEditor, new SourceRange(selectionOffset, 0));
	}
	
	protected IEditorInput getNewEditorInput(Location newEditorFilePath) throws CommonException {
		if(newEditorFilePath == null) {
			throw new CommonException("No path provided for new element. ");
		}
		
		if(areEqual(newEditorFilePath, getInputLocation())) {
			return editor.getEditorInput();
		} else {
			return EditorUtils.getBestEditorInputForLoc(newEditorFilePath);
		}
	}
	
	protected static int getOffsetFrom(IDocument doc, int line_oneBased, int column_oneBased) throws CoreException {
		int lineOffset;
		try {
			lineOffset = doc.getLineOffset(line_oneBased-1);
		} catch (BadLocationException e) {
			throw LangCore.createCoreException("Invalid line number: " + line_oneBased, e);
		}
		
		return lineOffset + column_oneBased-1;
	}
	
	/* -----------------  ----------------- */
	
	public IToolOperationService getToolService() {
		return LangCore.getToolManager().getEngineToolsOperationService();
	}
	
}