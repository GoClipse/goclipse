/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.operation;


import java.lang.reflect.InvocationTargetException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.operation.IRunnableWithProgress;

@FunctionalInterface
public interface CoreOperationRunnable extends IRunnableWithProgress {
	
	public abstract void doRun(IProgressMonitor pm) throws CommonException, CoreException, OperationCancellation;
	
	@Override
	default void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			coreAdaptedRun(monitor);
		} catch (CoreException e) {
			throw new InvocationTargetException(e);
		} catch (OperationCancellation e) {
			throw new InterruptedException(e.getMessage());
		}
	}
	
	default void coreAdaptedRun(IProgressMonitor monitor) throws CoreException, OperationCancellation {
		try {
			doRun(monitor);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		} catch (OperationCanceledException e) {
			throw new OperationCancellation();
		} 
	}
	
}