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
package com.googlecode.goclipse.ui.actions;

import java.nio.file.Path;

import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.oracle.GoOperationContext;
import com.googlecode.goclipse.tooling.tools.GoFindDefinitionOperation;

import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.StatusException;

public class GoOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition";
	
	public GoOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor, range, openEditorMode);
	}
	
	@Override
	protected SourceLocation doBackgroundToolResultComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation, OperationSoftFailure {
		GoFindDefinitionOperation op = getFindDefinitionOperation(sourceBuffer, getOperationOffset());
		return op.getValidResult(om);
	}
	
	public static GoFindDefinitionOperation getFindDefinitionOperation(ISourceBuffer sourceBuffer, int offset) {
		GoOperationContext goOpContext = GoProjectEnvironment.getGoOperationContext(sourceBuffer, offset);
		
		return new GoFindDefinitionOperation(goOpContext) {
			@Override
			protected Path getGoDefPath() throws StatusException {
				return GoToolPreferences.GODEF_Path.getDerivedValue();
			}
			
			@Override
			protected Path getGuruPath() throws StatusException {
				return GoToolPreferences.GO_GURU_Path.getDerivedValue();
			}
		};
	}
	
}