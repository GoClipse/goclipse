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

import com.googlecode.goclipse.core.tools.GocodeServerManager;

import melnorme.lang.ide.ui.utils.operations.WorkbenchOperationExecutor;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class StartGocodeServerOperation {
	
	public StartGocodeServerOperation() {
		super();
	}
	
	public void execute(GocodeServerManager gocodeServerManager) throws CommonException, OperationCancellation {
		
		boolean needsStart = gocodeServerManager.prepareServerStart();
		if(!needsStart) {
			return;
		}
		
		CommonOperation backgroundOperation = CommonOperation.namedOperation("Starting gocode server", om -> {
			gocodeServerManager.doStartServer(om);
		});
		WorkbenchOperationExecutor backgroundOperationExecutor = new WorkbenchOperationExecutor();
		backgroundOperationExecutor.execute(backgroundOperation);
	}
	
}