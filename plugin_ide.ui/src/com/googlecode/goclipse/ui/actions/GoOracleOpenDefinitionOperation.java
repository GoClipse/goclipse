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
import melnorme.lang.ide.ui.actions.AbstractOpenElementOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.json.JSONException;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.GoWorkspace2;
import com.googlecode.goclipse.editors.GoEditor;
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
	protected void performLongRunningComputation_do() throws CoreException {
		GoWorkspace2 goWorkspace = new GoWorkspace2(project);
		Path goPackage = goWorkspace.getGoPackageFromGoModule(getFilePath());
		
		ArrayList2<String> commandLine = new ArrayList2<>(
			GoToolPreferences.GO_ORACLE_Path.get(),
			"-pos=" + getFilePath().toString() + ":#" + range.getOffset() + ",#" + range.getEndPos(),
			"-format=json",
			"describe",
			goPackage.toString()
		);
		
		NullProgressMonitor pm = new NullProgressMonitor();
		ExternalProcessResult result = GoToolManager.getDefault().runEngineClientTool(
			commandLine, null, project, pm);
		
		try {
			oracleResult = new GoOracleFindDefinitionOperation().parseJsonResult(result);
		} catch (JSONException e) {
			throw LangCore.createCoreException("Error parsing output. ", e);
		} catch (StatusException se) {
			throw LangCore.createCoreException(se.getMessage(), se.getCause());
		}
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		
		IEditorInput newInput = getNewEditorInput(oracleResult.path);
		
		SourceRange sr = new SourceRange(0, 0);
		ITextEditor newEditor = EditorUtils.openEditor(editor, GoEditor.EDITOR_ID, newInput, sr, openEditorMode);
		
		IDocument doc = EditorUtils.getEditorDocument(newEditor);
		int selectionOffset = getOffsetFrom(doc, oracleResult.line, oracleResult.column);
		
		EditorUtils.setEditorSelection(newEditor, new SourceRange(selectionOffset, 0));
	}
	
}