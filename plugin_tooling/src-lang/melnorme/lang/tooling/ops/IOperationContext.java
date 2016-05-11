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
package melnorme.lang.tooling.ops;

import melnorme.utilbox.concurrency.ICancelMonitor;

/* FIXME: rename*/
public interface IOperationContext extends ICancelMonitor {

	// TODO: in future we might add methods here to report progress to UI, similar to IProgressMonitor
	
	/* -----------------  ----------------- */
	
	public class NullOperationContext extends NullCancelMonitor implements IOperationContext {
		
	}
	
}
