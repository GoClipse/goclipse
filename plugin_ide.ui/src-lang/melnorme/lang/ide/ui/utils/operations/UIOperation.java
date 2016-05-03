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
package melnorme.lang.ide.ui.utils.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.ICommonOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class UIOperation extends AbstractUIOperation {
	
	protected final ICommonOperation backgroundOp;
	
	public UIOperation(String operationName, ICommonOperation backgroundOp) {
		super(operationName);
		this.backgroundOp = assertNotNull(backgroundOp);
	}
	
	@Override
	protected void doBackgroundComputation(IProgressMonitor pm) throws CommonException, OperationCancellation {
		backgroundOp.execute(pm);
	}
	
}