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
package melnorme.lang.ide.core.utils;


import java.lang.reflect.InvocationTargetException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.operation.IRunnableWithProgress;

public abstract class CoreOperationAdapter implements IRunnableWithProgress {
	
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			coreRun(monitor);
		} catch (CoreException e) {
			throw new InvocationTargetException(e);
		} catch (OperationCanceledException e) {
			throw new InterruptedException(e.getMessage());
		}
	}
	
	protected void coreRun(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		try {
			doRun(monitor);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		} catch (OperationCancellation e) {
			throw new OperationCanceledException();
		} 
	}
	
	public abstract void doRun(IProgressMonitor monitor) throws CommonException, CoreException, OperationCancellation;
	
}