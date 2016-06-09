/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.option;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/* FIXME: refactor this class, simplify and remove */
public class EditorOperationContext {
	
	public static EditorOperationContext create(ISourceBuffer sourceBuffer, ITextViewer viewer, int offset, ITextEditor editor) {
		assertNotNull(sourceBuffer);
		SourceRange selection = viewer == null ? null : EditorUtils.getSelectedRange(viewer);
		
		return create(sourceBuffer, offset, selection, viewer, editor);
	}
	
	public static EditorOperationContext create(ISourceBuffer sourceBuffer, int offset, SourceRange selection, ITextViewer viewer,
			ITextEditor editor) {
		assertTrue(viewer != null || editor != null);
		IDocument document = viewer != null ? viewer.getDocument() : EditorUtils.getEditorDocument(editor);
		
		return new EditorOperationContext(sourceBuffer, offset, selection, document, editor);
	}
	
	/* -----------------  ----------------- */
	
	protected final ISourceBuffer sourceBuffer;
	protected final SourceOpContext context;
	
	protected final int offset;
	protected final SourceRange selection;
	protected final IDocument document;
	
	protected final IEditorPart editor; // can be null
	
	public EditorOperationContext(ISourceBuffer sourceBuffer, int offset, SourceRange selection, IDocument document, IEditorPart editor) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.offset = offset;
		this.selection = selection != null ? selection : SourceRange.srStartToEnd(offset, offset);
		this.document = assertNotNull(document);
		
		this.editor = editor;
		
		Location fileLocation = null;
		boolean dirty = true;
		if(editor != null) {
			fileLocation = EditorUtils.getInputLocationOrNull(editor);
			dirty = editor.isDirty();
		}
		context = new SourceOpContext(option(fileLocation), offset, document.get(), dirty);
	}
	
	public ISourceBuffer getSourceBuffer() {
		return assertNotNull(sourceBuffer);
	}
	
	public IDocument getDocument() {
		return document;
	}
	
	public String getSource() {
		return context.getSource();
	}
	
	public int getOffset() {
		return context.getOffset();
	}
	
	public SourceRange getSelection() {
		return selection;
	}
	
	public SourceOpContext getContext() {
		return context;
	}
	
	public Point getSelection_asPoint() {
		return new Point(selection.getStartPos(), selection.getLength());
	}
	
//	public IEditorPart getEditor_maybeNull() {
//		return editor;
//	}
//	
//	public IEditorPart getEditor_nonNull() throws CommonException {
//		if(editor == null) {
//			throw new CommonException("Error, no editor available.");
//		}
//		return editor;
//	}
	
	public Location getEditorInputLocation() throws CommonException {
		return getContext().getFileLocation();
	}
	
}