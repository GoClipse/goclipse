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
package com.googlecode.goclipse.ui.actions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.oracle.GoOperationContext;
import com.googlecode.goclipse.tooling.oracle.GodefOperation;
import com.googlecode.goclipse.tooling.oracle.GuruFindDefinitionOperation;

import melnorme.lang.ide.ui.utils.operations.ComputeValueUIOperation;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class GoFindDefinitionOperation extends ComputeValueUIOperation<ToolResponse<SourceLocation>> {
	
	protected final GoOperationContext goOpContext;
	
	public GoFindDefinitionOperation(GoOperationContext goOpContext) {
		this.goOpContext = assertNotNull(goOpContext);
	}
	
	@Override
	public ToolResponse<SourceLocation> call() throws CommonException, OperationCancellation {
		
		ToolResponse<SourceLocation> originalResult;
		CommonException originalException = null;
		
		try {
			String godefPath = GoToolPreferences.GODEF_Path.getDerivedValue().toString();
			GodefOperation godefOp = new GodefOperation(goOpContext, godefPath);
			
			originalResult = invokeInBackground(godefOp);
			
			if(originalResult.isValidResult()) {
				return originalResult;
			}
		} catch(CommonException e) {
			originalException = e;
			originalResult = null;
		}
		
		String guruPath = GoToolPreferences.GO_GURU_Path.getDerivedValue().toString();
		// retry with guru
		GuruFindDefinitionOperation guruOp = new GuruFindDefinitionOperation(goOpContext, guruPath);
		
		try {
			boolean saveSuccess = goOpContext.getSourceBuffer().trySaveBufferIfDirty();
			if(saveSuccess) {
				ToolResponse<SourceLocation> result2 = invokeInBackground(guruOp);
				
				if(result2.isValidResult()) {
					// Use guru result
					return result2;
				}
			}
		} catch(CommonException e) {
			// Ignore, use original result, even if it was an error
		}
		
		if(originalException != null) {
			throw originalException;
		}
		return originalResult;
	}
	
}