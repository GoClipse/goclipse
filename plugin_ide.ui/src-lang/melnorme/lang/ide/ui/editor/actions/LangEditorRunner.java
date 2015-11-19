/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;

public abstract class LangEditorRunner implements Runnable {
	
	protected final IEditorPart editorPart;
	
	public LangEditorRunner(IEditorPart editorPart) {
		this.editorPart = assertNotNull(editorPart);
	}
	
	protected Shell getShell() {
		return editorPart.getEditorSite().getShell();
	}
	
	@Override
	public void run() {
		if(editorPart instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editorPart;
			runWithLangEditor(langEditor);
		} else {
			handleInternalError2("Editor is not of the expected kind.");
			return;
		}
		
	}
	
	protected abstract void handleInternalError2(String message);
	
	protected abstract void runWithLangEditor(AbstractLangEditor langEditor);
	
}