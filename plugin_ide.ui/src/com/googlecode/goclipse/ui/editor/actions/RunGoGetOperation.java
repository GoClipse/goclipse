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
package com.googlecode.goclipse.ui.editor.actions;

import melnorme.lang.ide.ui.actions.AbstractEditorOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.tools.GoGetOperation;

public class RunGoGetOperation extends AbstractEditorOperation {
	
	public RunGoGetOperation(ITextEditor editor) {
		super(null, editor);
	}
	
	@Override
	protected void performLongRunningComputation_do(IProgressMonitor monitor) throws CoreException {
		IFile file = EditorUtils.findFileOfEditor(editor);
		IProject project = file == null ? null : file.getProject();
		
		new GoGetOperation(project).goGetDependencies(monitor, inputPath.toFile());
	}
	
	@Override
	protected void performOperation_handleResult() throws CoreException {
		// TODO: refresh dependencies?
	}
	
}