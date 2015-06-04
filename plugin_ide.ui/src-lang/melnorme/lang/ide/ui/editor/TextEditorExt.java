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
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * A few extensions to TextEditor for non-lang-specific functionality.
 */
public class TextEditorExt extends TextEditor {
	
	protected final ArrayList2<IDisposable> owned = new ArrayList2<>();
	
	public TextEditorExt() {
		super();
	}
	
	protected <T extends IDisposable> T addOwned(T disposable) {
		owned.add(disposable);
		return disposable;
	}
	
	/**
	 * Add given disposable, if not present already.
	 */
	protected <T extends IDisposable> T putOwned(T disposable) {
		if(!CollectionUtil.containsSame(owned, disposable)) {
			owned.add(disposable);
		}
		return disposable;
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
	
	public Location getInputLocation() throws CoreException {
		return EditorUtils.getLocationFromEditorInput(getEditorInput());
	}
	
}