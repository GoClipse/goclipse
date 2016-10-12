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
package com.googlecode.goclipse.tooling.tools;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import com.googlecode.goclipse.tooling.oracle.GoOperationContext;
import com.googlecode.goclipse.tooling.oracle.GodefOperation;
import com.googlecode.goclipse.tooling.oracle.GuruFindDefinitionOperation;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.StatusException;

public abstract class GoFindDefinitionOperation {
	
	protected final boolean retryWithGuru = false;
	protected final GoOperationContext goOpContext;
	
	public GoFindDefinitionOperation(GoOperationContext goOpContext) {
		this.goOpContext = assertNotNull(goOpContext);
	}
	
	public GoOperationContext getGoOpContext() {
		return goOpContext;
	}
	
	public SourceLocation getValidResult(IOperationMonitor om) 
			throws CommonException, OperationCancellation, OperationSoftFailure {
		
		try {
			String godefPath = getGoDefPath().toString();
			GodefOperation godefOp = new GodefOperation(goOpContext, godefPath);
			
			return godefOp.executeToolOperation(om);
			
		} catch(CommonException | OperationSoftFailure e) {
			
			// retry with guru
			if(retryWithGuru) {
				try {
					String guruPath = getGuruPath().toString();
					return findWithGuru(guruPath, om);
				} catch(CommonException e2) {
					// Ignore, use original exception
				}
			}
			
			throw e;
		}
	}
	
	
	protected abstract Path getGuruPath() throws StatusException;
	
	protected abstract Path getGoDefPath() throws StatusException;
	
	
	protected SourceLocation findWithGuru(String guruPath, IOperationMonitor om) 
			throws CommonException, OperationCancellation, OperationSoftFailure {
		GuruFindDefinitionOperation guruOp = new GuruFindDefinitionOperation(goOpContext, guruPath);
		goOpContext.getSourceBuffer().trySaveBufferIfDirty();
		return guruOp.executeToolOperation(om);
	}
	
}