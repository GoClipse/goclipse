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
package melnorme.lang.ide.core.operations;

import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public interface ICommonOperation {
	
	void execute(IProgressMonitor pm) throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	public static ICommonOperation NULL_COMMON_OPERATION = (pm) -> { };
	
}