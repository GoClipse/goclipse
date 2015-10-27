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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.SourceLineColumnRange;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class AbstractOpenElementOperation extends AbstractEditorOperation2<FindDefinitionResult> {
	
	protected final String source;
	protected final SourceRange range; // range of element to open. Usually only offset matters
	protected final OpenNewEditorMode openEditorMode;
	protected final IProject project;
	protected final SourceOperationContext context;
	
	// prepare_items
	protected int line_0;
	protected int col_0;
	
	protected String statusErrorMessage;
	
	/**
	 * @param dummy  
	 */
	public AbstractOpenElementOperation(String operationName, ITextEditor editor, SourceRange range,
			OpenNewEditorMode openEditorMode, Void dummy) {
		super(operationName, editor);
		
		this.source = doc.get();
		this.range = assertNotNull(range);
		this.openEditorMode = openEditorMode;
		
		IFile file = EditorUtils.findFileOfEditor(editor);
		this.project = file == null ? null : file.getProject();
		
		this.context = new SourceOperationContext(range.getOffset(), range, doc, editor);
	}
	
	public int getInvocationOffset() {
		return context.getInvocationOffset();
	}
	
	public SourceOperationContext getContext() {
		return context;
	}
	
	protected AbstractToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		super.prepareOperation();
		
		if(! (new SourceRange(0, source.length())).contains(range)) {
			throw new CommonException("Invalid range, out of bounds of the document");
		}
		
		line_0 = getContext().getInvocationLine_0();
		col_0 = getContext().getInvocationColumn_0();
	}
	
	@Override
	protected FindDefinitionResult doBackgroundValueComputation(IProgressMonitor monitor)
			throws CoreException, CommonException, OperationCancellation {
		return performLongRunningComputation_doAndGetResult(monitor);
	}
	
	protected abstract FindDefinitionResult performLongRunningComputation_doAndGetResult(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation;
	
	@Override
	protected void handleComputationResult() throws CoreException, CommonException {
		if(statusErrorMessage != null) {
			handleStatusErrorMessage();
		}
		if(result == null) {
			Display.getCurrent().beep();
			return;
		}
		
		if(result.getInfoMessage() != null) {
			dialogInfo(result.getInfoMessage());
		}
		
		SourceLineColumnRange sourceRange = result.getSourceRange();
		
		openEditorForLocation(result.getFileLocation(), sourceRange);
	}
	
	protected void handleStatusErrorMessage() {
		if(editor instanceof AbstractTextEditor) {
			AbstractTextEditor abstractTextEditor = (AbstractTextEditor) editor;
			EditorUtils.setStatusLineErrorMessage(abstractTextEditor, statusErrorMessage, null);
		}
		Display.getCurrent().beep();
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
	
	protected IEditorInput getNewEditorInput(Location newEditorFilePath) throws CoreException {
		if(newEditorFilePath == null) {
			throw LangCore.createCoreException("No path provided for new element. ", null);
		}
		
		if(areEqual(newEditorFilePath, inputLoc)) {
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
	
}