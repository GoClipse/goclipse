/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.concurrency;


public interface ICancelMonitor {
	
	public boolean isCanceled();
	
	/* -----------------  ----------------- */
	
	public class NullCancelMonitor implements ICancelMonitor {
		
		@Override
		public boolean isCanceled() {
			return false;
		}
		
	}
	
	public static final NullCancelMonitor NULL_MONITOR = new NullCancelMonitor();
	
	
	public static void checkCancelation(ICancelMonitor cm) throws OperationCancellation {
		if(cm.isCanceled()) {
			throw new OperationCancellation();
		}
	}
	
}