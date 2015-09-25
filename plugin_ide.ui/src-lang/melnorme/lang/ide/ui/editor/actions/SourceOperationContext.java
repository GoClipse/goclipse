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
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class SourceOperationContext {
	
	protected final ITextViewer viewer;
	protected final SourceRange range;
	protected final IDocument document;
	protected final ITextEditor editor; // can be null
	
	public SourceOperationContext(ITextViewer viewer, int offset, ITextEditor editor) {
		this(viewer, SourceRange.srStartToEnd(offset, offset), editor);
	}
	
	public SourceOperationContext(ITextViewer viewer, SourceRange range, ITextEditor editor) {
		this.range = range;
		this.viewer = viewer;
		this.editor = editor;
		assertTrue(viewer != null || editor != null);
		this.document = viewer != null ? viewer.getDocument() : EditorUtils.getEditorDocument(editor);
		
		assertNotNull(document);
	}
	
	public IDocument getDocument() {
		return document;
	}
	
	public int getInvocationOffset() {
		return range.getOffset();
	}
	
	public ITextEditor getEditor_maybeNull() {
		return editor;
	}
	
	public ITextViewer getViewer_maybeNull() {
		return viewer;
	}
	
	
	public ITextViewer getViewer_nonNull() throws CoreException {
		if(viewer == null) {
			throw LangCore.createCoreException("Error, no viewer available.", null);
		}
		return viewer;
	}
	
	public IEditorPart getEditor_nonNull() throws CoreException {
		if(editor == null) {
			throw LangCore.createCoreException("Error, no editor available.", null);
		}
		return editor;
	}
	
	public Location getEditorInputLocation() throws CoreException {
		Location fileLocation = EditorUtils.getLocationFromEditorInput(editor.getEditorInput());
		if(fileLocation == null) {
			throw LangCore.createCoreException("Error, invalid location for editor input.", null);
		}
		return fileLocation;
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
	
	public ISourceFile getSourceFile() throws CoreException {
		final Location sourceFileLoc = getEditorInputLocation();
		return new ISourceFile() {
			@Override
			public Location getLocation() {
				return sourceFileLoc;
			}
		};
	}
	
}