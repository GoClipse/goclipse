/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.actions;


import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.eclipse.ui.texteditor.ITextEditorExtension2;

public abstract class TextEditorAction_Adapter extends AbstractEditorHandler {
	
	public TextEditorAction_Adapter(IWorkbenchPage page) {
		super(page);
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Checks the editor's modifiable state. Returns <code>true</code> if the editor can be modified,
	 * taking in account the possible editor extensions.
	 *
	 * <p>If the editor implements <code>ITextEditorExtension2</code>,
	 * this method returns {@link ITextEditorExtension2#isEditorInputModifiable()};<br> else if the editor
	 * implements <code>ITextEditorExtension</code>, it returns {@link ITextEditorExtension#isEditorInputReadOnly()};<br>
	 * else, {@link ITextEditor#isEditable()} is returned, or <code>false</code> if the editor is <code>null</code>.</p>
	 *
	 * <p>There is only a difference to {@link #validateEditorInputState()} if the editor implements
	 * <code>ITextEditorExtension2</code>.</p>
	 *
	 * @return <code>true</code> if a modifying action should be enabled, <code>false</code> otherwise
	 * @since 3.0
	 */
	protected boolean canModifyEditor() {
		ITextEditor editor= getTextEditor();
		if (editor instanceof ITextEditorExtension2)
			return ((ITextEditorExtension2) editor).isEditorInputModifiable();
		else if (editor instanceof ITextEditorExtension)
			return !((ITextEditorExtension) editor).isEditorInputReadOnly();
		else if (editor != null)
			return editor.isEditable();
		else
			return false;
	}

	/**
	 * Checks and validates the editor's modifiable state. Returns <code>true</code> if an action
	 * can proceed modifying the editor's input, <code>false</code> if it should not.
	 *
	 * <p>If the editor implements <code>ITextEditorExtension2</code>,
	 * this method returns {@link ITextEditorExtension2#validateEditorInputState()};<br> else if the editor
	 * implements <code>ITextEditorExtension</code>, it returns {@link ITextEditorExtension#isEditorInputReadOnly()};<br>
	 * else, {@link ITextEditor#isEditable()} is returned, or <code>false</code> if the editor is <code>null</code>.</p>
	 *
	 * <p>There is only a difference to {@link #canModifyEditor()} if the editor implements
	 * <code>ITextEditorExtension2</code>.</p>
	 *
	 * @return <code>true</code> if a modifying action can proceed to modify the underlying document, <code>false</code> otherwise
	 * @since 3.0
	 */
	protected boolean validateEditorInputState() {
		ITextEditor editor= getTextEditor();
		if (editor instanceof ITextEditorExtension2)
			return ((ITextEditorExtension2) editor).validateEditorInputState();
		else if (editor instanceof ITextEditorExtension)
			return !((ITextEditorExtension) editor).isEditorInputReadOnly();
		else if (editor != null)
			return editor.isEditable();
		else
			return false;
	}
}
