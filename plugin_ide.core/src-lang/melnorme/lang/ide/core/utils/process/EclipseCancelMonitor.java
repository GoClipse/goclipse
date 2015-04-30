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
package melnorme.lang.ide.core.utils.process;

import melnorme.utilbox.concurrency.ICancelMonitor;

import org.eclipse.core.runtime.IProgressMonitor;

public final class EclipseCancelMonitor implements ICancelMonitor {
	
	protected final IProgressMonitor monitor;
	
	public EclipseCancelMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public boolean isCanceled() {
		return monitor.isCanceled();
	}
	
}