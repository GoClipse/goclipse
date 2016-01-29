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
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class CompositeBuildOperation implements IToolOperation {
	
	protected final Indexable<IToolOperation> operations;
	protected final ISchedulingRule rule; // Can be null
	
	public CompositeBuildOperation(Indexable<IToolOperation> operations, ISchedulingRule rule) {
		this.operations = assertNotNull(operations);
		this.rule = rule;
	}
	
	@Override
	public void execute(IProgressMonitor monitor)
			throws CoreException, CommonException, OperationCancellation {
		
		if(rule == null) {
			doExecute(monitor);
		} else {
			ResourceUtils.runToolOperationInWorkspace((pm) -> doExecute(pm), rule, monitor);
		}
		
	}
	
	protected void doExecute(IProgressMonitor monitor) throws CoreException, CommonException, OperationCancellation {
		for (IToolOperation subOperation : operations) {
			subOperation.execute(monitor);
		}
	}
	
}