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

import com.googlecode.goclipse.tooling.oracle.GoDocParser;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.AbstractToolOperation;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class GoFindDocOperation implements AbstractToolOperation<String> {
	
	protected final GoFindDefinitionOperation findDefOp;
	
	public GoFindDocOperation(GoFindDefinitionOperation findDefOp) {
		this.findDefOp = assertNotNull(findDefOp);
	}
	
	@Override
	public String executeToolOperation(IOperationMonitor om) throws CommonException, OperationCancellation {
		
		SourceLocation findDefResult;
		try {
			findDefResult = findDefOp.getValidResult(om);
		} catch(OperationSoftFailure e) {
			return null; // No documentation will be available
		}
		
		SourceOpContext opContext = findDefOp.getGoOpContext().getOpContext();
		String fileContents = opContext.getSourceFor(findDefResult.getFileLocation());
		int offset = opContext.getOffsetFor(findDefResult);
		
		return new GoDocParser().parseDocForDefinitionAt(fileContents, offset);
	}

}