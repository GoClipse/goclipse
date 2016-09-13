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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class RunnableContextUtils {
	
	public static void runOperation(IRunnableContext context, Operation op, boolean isCancellable) 
		throws OperationCancellation, CommonException {
		try {
			context.run(true, isCancellable, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						op.execute(EclipseUtils.om(monitor));
					} catch(CommonException e) {
						throw new InvocationTargetException(e);
					} catch(OperationCancellation | OperationCanceledException e) {
						throw new InterruptedException();
					}
				}
			});
		} catch (InterruptedException e) {
			throw new OperationCancellation();
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if(targetException instanceof InterruptedException) {
				throw new OperationCancellation();
			}
			if(targetException instanceof CommonException) {
				throw (CommonException) targetException;
			}
			if(targetException instanceof RuntimeException) {
				throw (RuntimeException) targetException;
			}
			
			assertFail(); // Should not be possible
		}
	}
	
	public static void runOperationInWorkspace(IRunnableContext context, boolean isCancellable, Operation op) 
			throws OperationCancellation, CommonException {
		
		runOperation(context, (pm) -> {
			ResourceUtils.runOperation(ResourceUtils.getWorkspaceRoot(), pm, op);
		}, isCancellable);
	}
	
}