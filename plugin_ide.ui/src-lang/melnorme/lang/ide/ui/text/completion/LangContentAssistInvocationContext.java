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


import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.utilbox.misc.Location;
import melnorme_org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

public class LangContentAssistInvocationContext extends ContentAssistInvocationContext {
	
	protected final IEditorPart editor; // can be null
	
	public LangContentAssistInvocationContext(ITextViewer viewer, int offset, IEditorPart editor) {
		super(viewer, offset);
		this.editor = editor;
	}
	
//	public IEditorPart getEditor() {
//		return editor;
//	}
	
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
	
}