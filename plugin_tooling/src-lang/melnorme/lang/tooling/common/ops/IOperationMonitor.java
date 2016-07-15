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
	
	String getOperationName();
	
	void setTaskLabel(String taskLabel);
	
	abstract IOperationSubMonitor enterSubTask(String subTaskName);
	
	default void runSubTask(String subTaskName, Operation subOp) throws CommonException, OperationCancellation {
		try(IOperationSubMonitor subMonitor = enterSubTask(subTaskName)) {
			subOp.execute(subMonitor);
		}
	}
	
	/* -----------------  ----------------- */
	
	public abstract class BasicOperationMonitor implements IOperationMonitor {
		
		protected final ICancelMonitor cm;
		protected final String operationName; // can be null
		
		public BasicOperationMonitor(ICancelMonitor cm) {
			this(cm, null, true);
		}
		
		public BasicOperationMonitor(ICancelMonitor cm, String operationName, boolean initialize) {
			this.cm = assertNotNull(cm);
			this.operationName = operationName;
			
			if(initialize) {
				initializeLabel();
			}
		}
		
		@Override
		public final boolean isCanceled() {
			return cm.isCanceled();
		}
		
		@Override
		public String getOperationName() {
			return operationName;
		}
		
		public void initializeLabel() {
			if(getOperationName() != null) {
				setTaskLabel(getOperationName());
			}
		}
		
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
		
		protected final IOperationMonitor parentMonitor;
		protected final String originalTaskName;
		
		public OperationSubMonitor(IOperationMonitor parentMonitor, String operationName) {
			super(parentMonitor, operationName, false);
			this.parentMonitor = assertNotNull(parentMonitor);
			this.originalTaskName = parentMonitor.getOperationName();
			
			initializeLabel();
		}
		
		@Override
		public void close() {
			if(originalTaskName != null) {
				parentMonitor.setTaskLabel(originalTaskName);
			}
		}
		
		@Override
		public void setTaskLabel(String taskName) {
			parentMonitor.setTaskLabel(taskName);
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
		public void setTaskLabel(String taskName) {
			// Do nothing
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public class DelegatingOperationMonitor implements IOperationMonitor {
		
		protected final IOperationMonitor om;
		
		public DelegatingOperationMonitor(IOperationMonitor om) {
			this.om = assertNotNull(om);
		}
		
		@Override
		public boolean isCanceled() {
			return om.isCanceled();
		}
		
		@Override
		public String getOperationName() {
			return om.getOperationName();
		}
		
		@Override
		public void setTaskLabel(String taskLabel) {
			om.setTaskLabel(taskLabel);
		}
		
		@Override
		public IOperationSubMonitor enterSubTask(String subTaskName) {
			return new OperationSubMonitor(this, subTaskName);
		}
		
	}
	
}