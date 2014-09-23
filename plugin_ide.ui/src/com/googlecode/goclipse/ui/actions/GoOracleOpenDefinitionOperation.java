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

import melnorme.lang.ide.ui.actions.AbstractEditorOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.GoWorkspace2;

public class GoOracleOpenDefinitionOperation extends AbstractEditorOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition (go oracle)";
	
	protected final SourceRange range;
	protected final IProject project;
	
	protected GoOracleOpenDefinitionResult oracleResult;
	
	public GoOracleOpenDefinitionOperation(ITextEditor editor, SourceRange range) {
		super(OPEN_DEFINITION_OpName, editor);
		this.range = range;
		
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
		
		oracleResult = parseJsonResult(result);
	}
	
	protected GoOracleOpenDefinitionResult parseJsonResult(ExternalProcessResult result) {
		if(result.exitValue != 0) {
			return new GoOracleOpenDefinitionResult("Program exited with non-zero status: " + result.exitValue);
		}
		
		// TODO
		return new GoOracleOpenDefinitionResult(null);
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		
		if(oracleResult.errorMessage != null) {
			throw GoCore.createCoreException(oracleResult.errorMessage, null);
		}
		
		// TODO
	}
	
}