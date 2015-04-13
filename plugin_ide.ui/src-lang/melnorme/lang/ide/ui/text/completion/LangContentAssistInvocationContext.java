/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.completion;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

public class LangContentAssistInvocationContext {
	
	protected final ITextViewer viewer;
	protected final int offset;
	protected final IDocument document;
	protected final IEditorPart editor; // can be null
	
	public LangContentAssistInvocationContext(ITextViewer viewer, int offset, IEditorPart editor) {
		this.viewer = assertNotNull(viewer);
		this.offset = offset;
		this.document = assertNotNull(viewer.getDocument());
		
		this.editor = editor;
	}
	
	public ITextViewer getViewer() {
		return viewer;
	}
	
	public IDocument getDocument() {
		return document;
	}
	
	public int getInvocationOffset() {
		return offset;
	}
	
	/** @return the editor for this context, or null if none. */
	public IEditorPart getEditor_maybeNull() {
		return editor;
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
	
}