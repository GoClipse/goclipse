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

import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.actions.CalculateValueUIOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class AbstractEditorOperation2<RESULT> extends CalculateValueUIOperation<RESULT> {
	
	protected final ITextEditor editor;
	protected final IWorkbenchWindow window;
	protected final IEditorInput editorInput;
	protected final Location inputLoc;
	protected final IDocument doc;
	
	public AbstractEditorOperation2(String operationName, ITextEditor editor) {
		super(operationName);
		this.editor = assertNotNull(editor);
		this.window = editor.getSite().getWorkbenchWindow();
		this.editorInput = editor.getEditorInput();
		Path inputPath = EditorUtils.getFilePathFromEditorInput(editorInput);
		this.inputLoc = Location.createValidOrNull(inputPath);
		this.doc = assertNotNull(editor.getDocumentProvider().getDocument(editor.getEditorInput()));
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		if(inputLoc == null) {
			throw LangCore.createCoreException("Could not determine filesystem path from editor input", null); 
		}
	}
	
	protected void dialogInfo(String message) {
		UIOperationsStatusHandler.displayStatusMessage(operationName, StatusLevel.INFO, message);  
	}
	
	protected void dialogError(String message) {
		UIOperationsStatusHandler.displayStatusMessage(operationName, StatusLevel.ERROR, message);  
	}
	
}