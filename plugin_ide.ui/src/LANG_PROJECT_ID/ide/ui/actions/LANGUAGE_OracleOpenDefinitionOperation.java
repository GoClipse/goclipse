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
package LANG_PROJECT_ID.ide.ui.actions;

import java.nio.file.Path;

import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.actions.AbstractOpenElementOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

public class LANGUAGE_OracleOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open Definition";
	
	protected final SourceRange range;
	protected final IProject project;
	protected final OpenNewEditorMode openEditorMode;
	
	public LANGUAGE_OracleOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor);
		this.range = range;
		this.openEditorMode = openEditorMode;
		
		IFile file = EditorUtils.findFileOfEditor(editor);
		this.project = file == null ? null : file.getProject();
	}
	
	protected Path getFilePath() {
		return inputPath;
	}
	
	@Override
	protected void performLongRunningComputation_do(IProgressMonitor monitor) throws CoreException {
		// TODO:
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		MessageDialog.openInformation(editor.getSite().getWorkbenchWindow().getShell(), "Error", "Not implemented.");
		
		IEditorInput newInput = editorInput;
		
		SourceRange sr = new SourceRange(0, 0);
		EditorUtils.openEditor(editor, EditorSettings_Actual.EDITOR_ID, newInput, sr, openEditorMode);
	}
	
}