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
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.LangSourceViewer;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class AbstractEditorOperation2<RESULT> extends CalculateValueUIOperation<RESULT> {
	
	protected final ITextEditor editor;
	protected final IWorkbenchWindow window;
	protected final IEditorInput editorInput;
	protected final Location inputLoc;
	protected final IDocument doc;
	
	protected String statusErrorMessage;
	
	public AbstractEditorOperation2(String operationName, ITextEditor editor) {
		super(operationName);
		this.editor = assertNotNull(editor);
		this.window = editor.getSite().getWorkbenchWindow();
		this.editorInput = editor.getEditorInput();
		Path inputPath = EditorUtils.getFilePathFromEditorInput(editorInput);
		this.inputLoc = Location.createValidOrNull(inputPath);
		this.doc = assertNotNull(editor.getDocumentProvider().getDocument(editor.getEditorInput()));
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		if(inputLoc == null) {
			throw LangCore.createCoreException("Could not determine filesystem path from editor input", null); 
		}
	}
	
	@Override
	protected void handleComputationResult() throws CoreException, CommonException {
		if(statusErrorMessage != null) {
			handleStatusErrorMessage(statusErrorMessage);
		}
	}
	
	protected void handleStatusErrorMessage(String statusErrorMessage) throws CommonException {
		if(editor instanceof AbstractTextEditor) {
			AbstractTextEditor abstractTextEditor = (AbstractTextEditor) editor;
			EditorUtils.setStatusLineErrorMessage(abstractTextEditor, statusErrorMessage, null);
			Display.getCurrent().beep();
		} else {
			throw new CommonException(statusErrorMessage);
		}
	}
	
	/* -----------------  ----------------- */
	
	protected LangSourceViewer getEditorSourceViewer() throws CommonException {
		if(editor instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editor;
			return langEditor.getSourceViewer_();
		} else {
			throw new CommonException("Possible internal error: SourceViewer not available.");
		}
	}
	
	protected void setEditorTextPreservingCarret(String newContents) throws CommonException {
		if(areEqual(newContents, doc.get())) {
			return;
		}
		
		LangSourceViewer sourceViewer = getEditorSourceViewer();
		
		ISelection sel = sourceViewer.getSelectionProvider().getSelection();
		
		int line = -1;
		int col = -1;
		if(sel instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) sel;
			
			try {
				line = doc.getLineOfOffset(textSelection.getOffset());
				col = textSelection.getOffset() - doc.getLineOffset(line);
			} catch(BadLocationException e) {
				// Ignore
			}
		} 
		
		IDocument document = sourceViewer.getDocument();
		
		if(document instanceof IDocumentExtension4) {
			IDocumentExtension4 doc_4 = (IDocumentExtension4) document;
			// We use the DocumentRewriteSession to prevent the caret from jumping around
			DocumentRewriteSession rewriteSession = doc_4.startRewriteSession(DocumentRewriteSessionType.SEQUENTIAL);
			document.set(newContents);
			doc_4.stopRewriteSession(rewriteSession);
		} else {
			int topIndex = sourceViewer.getTopIndex();
			document.set(newContents);
			sourceViewer.setTopIndex(topIndex);
		}
		
		int newOffset = -1;
		if(line != -1 && col != -1) {
			try {
				newOffset = getOffsetFor(line, col);
			} catch(BadLocationException e) {
				// ignore
			}
		}
		
		if(newOffset != -1) {
			sourceViewer.getSelectionProvider().setSelection(new TextSelection(newOffset, 0));
		} else {
			sourceViewer.getSelectionProvider().setSelection(sel);
		}
		
	}
	
	protected int getOffsetFor(int line, int col) throws BadLocationException {
		IRegion lineRegion = doc.getLineInformation(line);
		int offset = lineRegion.getOffset();
		
		return offset + Math.min(col, lineRegion.getLength());
	}
	
}