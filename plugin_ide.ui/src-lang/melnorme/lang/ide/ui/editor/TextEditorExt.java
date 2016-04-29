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
package melnorme.lang.ide.ui.editor;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.IOwner;
import melnorme.utilbox.ownership.OwnedObjects;

/**
 * A few extensions to TextEditor for non-lang-specific functionality.
 */
public class TextEditorExt extends TextEditor {
	
	protected final IOwner owned = new OwnedObjects();
	
	public TextEditorExt() {
		super();
	}
	
	protected <T extends IDisposable> T addOwned(T disposable) {
		owned.bind(disposable);
		return disposable;
	}
	
	@Override
	public void dispose() {
		owned.disposeAll();
		super.dispose();
	}
	
	/* -----------------  ----------------- */
	
	protected ExternalBreakpointWatcher breakpointWatcher;
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		
		owned.disposeOwned(breakpointWatcher);
		breakpointWatcher = null;
		
		if(input == null) {
			return;
		}
		
		IAnnotationModel annotationModel = getDocumentProvider().getAnnotationModel(input);
		IFile file = EditorUtils.getAssociatedFile(input);
		if(file != null) {
			return; // No need for external breakpoint watching
		}
		breakpointWatcher = new ExternalBreakpointWatcher(input, getDocument(),  annotationModel);
		
		owned.bind(breakpointWatcher);
	}
	
	protected IDocument getDocument() {
		return getDocumentProvider().getDocument(getEditorInput());
	}
	
	/* -----------------  ----------------- */
	
	// public access to method
	@Override
	public void setStatusLineErrorMessage(String message) {
		super.setStatusLineErrorMessage(message);
	}
	
	// public access to method
	@Override
	public void setTitleImage(Image titleImage) {
		super.setTitleImage(titleImage);
	}
	
	/* ----------------- actions ----------------- */
	
	protected LangEditorContextMenuContributor editorActionsManager;
	
	@Override
	protected void createActions() {
		super.createActions();
		
		editorActionsManager = createActionsManager();
	}
	
	protected LangEditorContextMenuContributor createActionsManager() {
		return EditorSettings_Actual.createCommandsContribHelper(getSite().getWorkbenchWindow());
	}
	
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		editorContextMenuAboutToShow_extend(menu);
	}
	
	protected void editorContextMenuAboutToShow_extend(IMenuManager menu) {
		editorActionsManager.editorContextMenuAboutToShow(menu);
	}
	
	/* ----------------- Helpers ----------------- */
	
	public boolean isActivePart() {
		return WorkbenchUtils.getActivePart(getSite()) == this;
	}
	
	public Location getInputLocation() throws CommonException {
		return EditorUtils.getLocationFromEditorInput(getEditorInput());
	}
	
}