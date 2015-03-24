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
package melnorme.lang.ide.ui.editor.actions;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

public class ToggleCommentHandler extends AbstractEditorHandler {
	
	public ToggleCommentHandler(IWorkbenchPage page) {
		super(page);
	}
	
	@Override
	public boolean isEnabled() {
		return false; // TODO implement
	}
	
	@Override
	protected void doRunWithEditor(AbstractLangEditor editor) {
		Shell shell = editor.getSite().getShell();
		MessageDialog.openInformation(shell, "", "Not implemented");
	}
	
}