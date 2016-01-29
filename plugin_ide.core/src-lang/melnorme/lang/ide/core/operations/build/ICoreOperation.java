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
package melnorme.lang.ide.core.operations.build;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;


public interface ICoreOperation {
	
	void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation;
	
	default void execute_adapted(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		try {
			execute(pm);
		} catch(OperationCanceledException oce) {
			throw new OperationCancellation();
		}
	}
	
}