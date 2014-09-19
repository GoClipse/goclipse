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
package melnorme.lang.ide.ui.actions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractEditorOperation extends AbstractUIOperation {
	
	protected final ITextEditor editor;
	protected final IWorkbenchWindow window;
	protected final IEditorInput editorInput;
	protected final Path inputPath;
	protected final IDocument doc;
	
	public AbstractEditorOperation(String operationName, ITextEditor editor) {
		super(operationName);
		this.editor = editor;
		this.window = editor.getSite().getWorkbenchWindow();
		this.editorInput = editor.getEditorInput();
		this.inputPath = EditorUtils.getFilePathFromEditorInput(editorInput);
		if(inputPath == null) {
			LangCore.logError("Could not determine filesystem path from editor input");
		}
		this.doc = assertNotNull(editor.getDocumentProvider().getDocument(editor.getEditorInput()));
	}
	
	@Override
	public void executeOperation() throws CoreException {
		if(inputPath == null) {
			throw LangCore.createCoreException("Could not determine filesystem path from editor input", null); 
		}
		
		super.executeOperation();
		
		performOperation_handleResult();
	}
	
	protected abstract void performOperation_handleResult() throws CoreException;
	
	protected void dialogError(String msg) {
		UIUserInteractionsHelper.openError(window.getShell(), operationName, msg);
	}
	
	protected void dialogWarning(String msg) {
		UIUserInteractionsHelper.openWarning(window.getShell(), operationName, msg);
	}
	
	protected void dialogInfo(String msg) {
		UIUserInteractionsHelper.openInfo(window.getShell(), operationName, msg);
	}
	
	protected void handleSystemError(String msg) {
		LangCore.logError(msg);
		dialogError(msg);
	}
	
}