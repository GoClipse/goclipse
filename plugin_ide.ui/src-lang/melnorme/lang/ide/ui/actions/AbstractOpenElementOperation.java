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
package melnorme.lang.ide.ui.actions;


import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.SourceLineColumnRange;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractOpenElementOperation extends AbstractEditorOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open Definition";
	
	protected final SourceRange range; // range of element to open. Usually only offset matters
	protected final OpenNewEditorMode openEditorMode;
	protected final IProject project;
	
	protected FindDefinitionResult findResult;
	
	public AbstractOpenElementOperation(String operationName, ITextEditor editor, SourceRange range,
			OpenNewEditorMode openEditorMode) {
		super(operationName, editor);
		
		this.range = range;
		this.openEditorMode = openEditorMode;
		
		IFile file = EditorUtils.findFileOfEditor(editor);
		this.project = file == null ? null : file.getProject();
	}
	
	protected IEditorInput getNewEditorInput(Path newEditorFilePath) throws CoreException {
		if(newEditorFilePath == null) {
			throw LangCore.createCoreException("No path provided for new element. ", null);
		}
		
		if(areEqual(newEditorFilePath, inputPath)) {
			return editor.getEditorInput();
		} else {
			return EditorUtils.getBestEditorInputForPath(newEditorFilePath);
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
	
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		
		if(findResult.getErrorMessage() != null) {
			dialogError(findResult.getErrorMessage());
			return;
		}
		
		if(findResult.getInfoMessage() != null) {
			dialogInfo(findResult.getInfoMessage());
		}
		
		SourceLineColumnRange location = findResult.getLocation();
		if(location == null) {
			Display.getCurrent().beep();
			return;
		}
		
		openEditorForLocation(location);
	}
	
	protected void openEditorForLocation(SourceLineColumnRange location) throws CoreException {
		IEditorInput newInput = getNewEditorInput(location.path);
		
		ITextEditor newEditor = EditorUtils.openTextEditorAndSetSelection(editor, EditorSettings_Actual.EDITOR_ID, 
			newInput, openEditorMode, null);
		
		IDocument doc = EditorUtils.getEditorDocument(newEditor);
		int selectionOffset = getOffsetFrom(doc, location.line, location.column);
		
		EditorUtils.setEditorSelection(newEditor, new SourceRange(selectionOffset, 0));
	}
	
}