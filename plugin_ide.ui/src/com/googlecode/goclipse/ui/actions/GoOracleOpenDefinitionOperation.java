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
package com.googlecode.goclipse.ui.actions;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.actions.AbstractOpenElementOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.GoEnvironment;
import com.googlecode.goclipse.tooling.StatusException;
import com.googlecode.goclipse.tooling.oracle.GoOracleFindDefinitionOperation;
import com.googlecode.goclipse.tooling.oracle.GoOracleFindDefinitionOperation.GoOracleFindDefinitionResult;

public class GoOracleOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition (go oracle)";
	
	protected final SourceRange range;
	protected final IProject project;
	protected final OpenNewEditorMode openEditorMode;
	
	protected GoOracleFindDefinitionResult oracleResult;
	
	public GoOracleOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
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
		String goOraclePath = GoToolPreferences.GO_ORACLE_Path.get();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		try {
			GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation(goOraclePath);
			ProcessBuilder pb = op.createProcessBuilder(goEnv, getFilePath(), range.getOffset());
			
			ExternalProcessResult result = GoToolManager.getDefault().runEngineTool(pb, null, monitor);
			
			oracleResult = op.parseJsonResult(result);
		} catch (StatusException se) {
			throw LangCore.createCoreException(se.getMessage(), se.getCause());
		}
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		
		IEditorInput newInput = getNewEditorInput(oracleResult.path);
		
		SourceRange sr = new SourceRange(0, 0);
		ITextEditor newEditor = EditorUtils.openEditor(editor, EditorSettings_Actual.EDITOR_ID, 
			newInput, sr, openEditorMode);
		
		IDocument doc = EditorUtils.getEditorDocument(newEditor);
		int selectionOffset = getOffsetFrom(doc, oracleResult.line, oracleResult.column);
		
		EditorUtils.setEditorSelection(newEditor, new SourceRange(selectionOffset, 0));
	}
	
}