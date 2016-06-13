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
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;

public class GoOperationContext {
	
	protected final ISourceBuffer sourceBuffer;
	protected final SourceOpContext opContext;
	protected final IToolOperationService toolOpService;
	protected final GoEnvironment goEnv;
	
	public GoOperationContext(ISourceBuffer sourceBuffer, SourceOpContext opContext, IToolOperationService toolOpService, 
			GoEnvironment goEnv) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.opContext = assertNotNull(opContext);
		this.toolOpService = assertNotNull(toolOpService);
		this.goEnv = assertNotNull(goEnv);
	}
	
	public ISourceBuffer getSourceBuffer() {
		return sourceBuffer;
	}
	
	public IToolOperationService getToolOpService() {
		return toolOpService;
	}
	
	public GoEnvironment getGoEnv() {
		return goEnv;
	}
	
}