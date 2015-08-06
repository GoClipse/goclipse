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
package melnorme.lang.ide.ui.utils;

import java.lang.reflect.InvocationTargetException;

import melnorme.lang.ide.core.LangCore;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class UIOperationsHelper extends UIOperationsStatusHandler {

	public static boolean runAndHandle(IRunnableContext runnableContext, IRunnableWithProgress op, 
			boolean isCancellable, String errorTitle) {
		
		try {
			runnableContext.run(true, isCancellable, op);
			return true;
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if(targetException instanceof OperationCanceledException) {
				return false;
			}
			
			CoreException ce;
			if(targetException instanceof CoreException) {
				ce = (CoreException) e.getTargetException();
			} else {
				ce = LangCore.createCoreException("Internal error: ", targetException);
			}
			UIOperationsStatusHandler.handleOperationStatus(errorTitle, ce);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}
	
}