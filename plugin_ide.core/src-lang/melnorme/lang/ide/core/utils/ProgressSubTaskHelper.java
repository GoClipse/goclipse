/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.SubProgressMonitor;

public class ProgressSubTaskHelper extends ProgressMonitorWrapper implements AutoCloseable, IProgressMonitor {
	
	public ProgressSubTaskHelper(IProgressMonitor parentMonitor, String subTaskMessage) {
		super(new SubProgressMonitor(parentMonitor, 0));
		getWrappedProgressMonitor().subTask(subTaskMessage);
	}
	
	@Override
	public void close() {
		getWrappedProgressMonitor().subTask("");
	}
	
}