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
package melnorme.lang.ide.ui.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public abstract class ProgressRunnableWithResult<R> implements IRunnableWithProgress {
	
	public volatile R result;

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		result = doCall(monitor);
	}
	
	public abstract R doCall(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;
	
}