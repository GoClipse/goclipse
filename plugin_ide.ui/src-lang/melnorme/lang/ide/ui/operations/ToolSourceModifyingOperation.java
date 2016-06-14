/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.operations;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.PlatformUI;

import melnorme.lang.ide.core.operations.RunToolOperationOnResource;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.ui.utils.operations.UIOperation;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

/**
 * Run a tool that modifies source files.
 */
public class ToolSourceModifyingOperation extends UIOperation {
	
	public ToolSourceModifyingOperation(String operationName, IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions) {
		this(operationName, new RunToolOperationOnResource(project, commands, opViewOptions));
	}
	
	public ToolSourceModifyingOperation(String operationName, CommonOperation backgroundOp) {
		super(operationName, backgroundOp);
	}
	
	@Override
	public void execute() throws CommonException, OperationCancellation {
		boolean result = PlatformUI.getWorkbench().saveAllEditors(true);
		if(result == false) {
			throw new OperationCancellation();
		}
		
		super.execute();
	}
	
}