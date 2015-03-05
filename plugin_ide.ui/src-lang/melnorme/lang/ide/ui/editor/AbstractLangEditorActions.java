/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public abstract class AbstractLangEditorActions implements IDisposable {
	
	protected final AbstractDecoratedTextEditor editor;

	public AbstractLangEditorActions(AbstractDecoratedTextEditor editor) {
		this.editor = assertNotNull(editor);
	}
	
	public void editorContextMenuAboutToShow(IMenuManager ctxMenu) {
		IWorkbenchWindow window = editor.getSite().getWorkbenchWindow();
		ctxMenu.prependToGroup(ICommonMenuConstants.GROUP_OPEN, new CommandContributionItem(
			new CommandContributionItemParameter(window, 
				null, 
				EditorSettings_Actual.COMMAND_OpenDef_ID,
				CommandContributionItem.STYLE_PUSH
			)
		));
		
		prepareSourceMenu(ctxMenu);
	}
	
	protected void prepareSourceMenu(IMenuManager ctxMenu) {
		ctxMenu.remove(ITextEditorActionConstants.SHIFT_RIGHT);
		ctxMenu.remove(ITextEditorActionConstants.SHIFT_LEFT);
		
		
		IMenuManager sourceMenu = LangEditorActionContributor.
				createSourceMenuSkeleton(); // I wonder if it's ok to reuse the same menu id as the workbench menu
		
		contributeSourceMenu(sourceMenu);
		ctxMenu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT, sourceMenu);
	}
	
	// There is duplication with LangEditorActionContributor.contributeSourceMenu(IMenuManager) 
	// TODO: use command contributions?
	protected void contributeSourceMenu(IMenuManager sourceMenu) {
		sourceMenu.appendToGroup(LangEditorActionContributor.SOURCE_MENU_GroupFormat, 
			editor.getAction(ITextEditorActionConstants.SHIFT_RIGHT));
		sourceMenu.appendToGroup(LangEditorActionContributor.SOURCE_MENU_GroupFormat, 
			editor.getAction(ITextEditorActionConstants.SHIFT_LEFT));
		
		// TODO: ToggleComment action
	}
	
	@Override
	public void dispose() {
		doDispose();
	}
	
	protected abstract void doDispose();
	
}