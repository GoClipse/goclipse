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
package melnorme.lang.ide.ui.editor;


import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * A few extensions to TextEditor for non-lang-specific functionality.
 */
public class TextEditorExt extends TextEditor {
	
	protected final ArrayList2<IDisposable> owned = new ArrayList2<>();
	
	public TextEditorExt() {
		super();
	}
	
	protected <T extends IDisposable> T addOwned(T ownedObject) {
		owned.add(ownedObject);
		return ownedObject;
	}
	
	@Override
	public void dispose() {
		for (IDisposable disposable : owned) {
			disposable.dispose();
		}
		owned.clear();
		super.dispose();
	}
	
	/* -----------------  ----------------- */
	
	// Allow public access to method
	@Override
	public void setStatusLineErrorMessage(String message) {
		super.setStatusLineErrorMessage(message);
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
	
}