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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.EditorSourceBuffer;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class AbstractEditorOperation2<RESULT> extends CalculateValueUIOperation<RESULT> {
	
	protected final ITextEditor editor;
	protected final IWorkbenchWindow window;
	
	protected final IDocument doc;
	protected final IProject project;
	protected final SourceOpContext sourceOpContext;
	
	
	public AbstractEditorOperation2(String operationName, ITextEditor editor) {
		this(operationName, editor, new SourceRange(0, 0));
	}
	
	public AbstractEditorOperation2(String operationName, ITextEditor editor, SourceRange range) {
		super(operationName);
		this.editor = assertNotNull(editor);
		this.window = editor.getSite().getWorkbenchWindow();
		
		this.doc = assertNotNull(editor.getDocumentProvider().getDocument(editor.getEditorInput()));
		this.project = EditorUtils.getAssociatedProject(editor.getEditorInput());
		this.sourceOpContext = new EditorSourceBuffer(editor).getSourceOpContext(range);
	}
	
	public SourceOpContext getContext2() {
		return sourceOpContext;
	}
	
	public String getSource() {
		return sourceOpContext.getSource();
	}
	
	public SourceRange getOperationRange() {
		return sourceOpContext.getOperationRange();
	}
	
	public int getOperationOffset() {
		return sourceOpContext.getOffset();
	}
	
	public Location getInputLocation() throws CommonException {
		return sourceOpContext.getFileLocation();
	}
	
	@Override
	public void prepareOperation() throws CommonException {
		if(!getContext2().getOptionalFileLocation().isPresent()) {
			throw new CommonException("No file available for editor contents.");
		}
	}
	
	/* -----------------  ----------------- */ 
	
	@Override
	protected abstract void handleComputationResult(RESULT result) throws CommonException;
	
	protected void handleStatusErrorMessage(String statusErrorMessage) {
		EditorUtils.setStatusLineErrorMessage(editor, statusErrorMessage, null);
		Display.getCurrent().beep();
	}
	
	/* -----------------  ----------------- */
	
	protected SourceViewer getEditorSourceViewer() throws CommonException {
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
		
		SourceViewer sourceViewer = getEditorSourceViewer();
		
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