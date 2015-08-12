/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class CompositeBuildOperation implements IToolOperation {
	
	protected final Indexable<IToolOperation> operations;
	
	public CompositeBuildOperation(Indexable<IToolOperation> operations) {
		this.operations = assertNotNull(operations);
	}
	
	@Override
	public void execute(IProgressMonitor monitor)
			throws CoreException, CommonException, OperationCancellation {
		
		for (IToolOperation subOperation : operations) {
			subOperation.execute(monitor);
		}
		
	}
	
}