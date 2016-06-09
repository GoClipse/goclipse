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
package melnorme.lang.tooling.common.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public interface IOperationMonitor extends ICancelMonitor {
	
	abstract IOperationSubMonitor enterSubTask(String subTaskName);
	
	default void runSubTask(String subTaskName, CommonOperation subOp) throws CommonException, OperationCancellation {
		try(IOperationSubMonitor subMonitor = enterSubTask(subTaskName)) {
			subOp.execute(subMonitor);
		}
	}
	
	/* -----------------  ----------------- */
	
	public abstract class BasicOperationMonitor implements IOperationMonitor {
		
		protected final ICancelMonitor cm;
		protected final String operationName; // can be null
		
		
		public BasicOperationMonitor(ICancelMonitor cm, String operationName, boolean initialize) {
			this.cm = assertNotNull(cm);
			this.operationName = operationName;
			
			if(initialize) {
				setTaskName();
			}
		}
		
		@Override
		public final boolean isCanceled() {
			return cm.isCanceled();
		}
		
		public void setTaskName() {
			if(operationName != null) {
				setTaskName(operationName);
			}
		}
		
		public abstract void setTaskName(String taskName);
		
		@Override
		public IOperationSubMonitor enterSubTask(String subTaskName) {
			return new OperationSubMonitor(this, subTaskName);
		}
		
	}
	
	public static interface IOperationSubMonitor extends IOperationMonitor, AutoCloseable {
		
		@Override
		abstract void close();
		
	}
	
	public class OperationSubMonitor extends BasicOperationMonitor implements IOperationSubMonitor {
		
		protected final BasicOperationMonitor parentMonitor;
		
		public OperationSubMonitor(BasicOperationMonitor parentMonitor, String operationName) {
			super(parentMonitor.cm, operationName, false);
			this.parentMonitor = assertNotNull(parentMonitor);
			
			setTaskName();
		}
		
		@Override
		public void close() {
			parentMonitor.setTaskName();
		}
		
		@Override
		public void setTaskName(String taskName) {
			parentMonitor.setTaskName(taskName);
		}
		
	}
	
	public class NullOperationMonitor extends BasicOperationMonitor {
		
		public NullOperationMonitor() {
			this(new NullCancelMonitor());
		}
		
		public NullOperationMonitor(ICancelMonitor cm) {
			super(cm, null, true);
		}
		
		@Override
		public void setTaskName(String taskName) {
			// Do nothing
		}
		
	}
	
}