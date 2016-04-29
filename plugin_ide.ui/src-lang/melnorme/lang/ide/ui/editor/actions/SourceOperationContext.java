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
import melnorme.lang.ide.core.ISourceFile;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class SourceOperationContext {
	
	public static SourceOperationContext create(ITextViewer viewer, int offset, ITextEditor editor) {
		SourceRange selection = viewer == null ? null : EditorUtils.getSelectedRange(viewer);
		
		return create(offset, selection, viewer, editor);
	}
	
	public static SourceOperationContext create(int offset, SourceRange selection, ITextViewer viewer,
			ITextEditor editor) {
		assertTrue(viewer != null || editor != null);
		IDocument document = viewer != null ? viewer.getDocument() : EditorUtils.getEditorDocument(editor);
		
		return new SourceOperationContext(offset, selection, document, editor);
	}
	
	/* -----------------  ----------------- */
	
	protected final int offset;
	protected final SourceRange selection;
	protected final IDocument document;
	
	protected final IEditorPart editor; // can be null
	
	public SourceOperationContext(int offset, SourceRange selection, IDocument document, IEditorPart editor) {
		this.offset = offset;
		this.selection = selection != null ? selection : SourceRange.srStartToEnd(offset, offset);
		this.document = assertNotNull(document);
		
		this.editor = editor;
	}
	
	public IDocument getDocument() {
		return document;
	}
	
	public String getSource() {
		return document.get();
	}
	
	public int getInvocationOffset() {
		return offset;
	}
	
	public SourceRange getSelection() {
		return selection;
	}
	
	public Point getSelection_asPoint() {
		return new Point(selection.getStartPos(), selection.getLength());
	}
	
	public IEditorPart getEditor_maybeNull() {
		return editor;
	}
	
	public IEditorPart getEditor_nonNull() throws CommonException {
		if(editor == null) {
			throw new CommonException("Error, no editor available.");
		}
		return editor;
	}
	
	public Location getEditorInputLocation() throws CommonException {
		Location fileLocation = EditorUtils.getLocationFromEditorInput(getEditor_nonNull().getEditorInput());
		if(fileLocation == null) {
			throw new CommonException("Error, invalid location for editor input.");
		}
		return fileLocation;
	}
	
	/** Can be null. */
	public IProject getProject() throws CommonException {
		return EditorUtils.getAssociatedProject(getEditor_nonNull().getEditorInput());
	}
	
	public boolean isSourceDocumentDirty() throws CommonException {
		return getEditor_nonNull().isDirty();
	}
	
	public int getInvocationLine_0() throws CoreException {
		try {
			return getDocument().getLineOfOffset(getInvocationOffset());
		} catch (BadLocationException e) {
			throw LangCore.createCoreException("Could not get line position.", e);
		}
	}
	
	public int getInvocationColumn_0() throws CoreException {
		try {
			return getInvocationOffset() - getDocument().getLineInformation(getInvocationLine_0()).getOffset();
		} catch (BadLocationException e) {
			throw LangCore.createCoreException("Could not get column position.", e);
		}
	}
	
	public ISourceFile getSourceFile() throws CommonException {
		final Location sourceFileLoc = getEditorInputLocation();
		return new ISourceFile() {
			@Override
			public Location getLocation() {
				return sourceFileLoc;
			}
		};
	}
	
}