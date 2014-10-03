/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;

public class TimeoutProgressMonitor implements IProgressMonitor {
	
	protected final int timeoutMs;
	protected long startTimeMs = -1;
	
	public TimeoutProgressMonitor(int timeoutMs, boolean start) {
		this.timeoutMs = timeoutMs;
		if(start) {
			beginTask(null, UNKNOWN);
		}
	}
	
	@Override
	public void beginTask(String name, int totalWork) {
		startTimeMs = System.currentTimeMillis();
	}
	
	@Override
	public void done() {
	}
	
	@Override
	public void internalWorked(double work) {
	}
	
	@Override
	public boolean isCanceled() {
		return System.currentTimeMillis() - startTimeMs > timeoutMs;
	}
	
	@Override
	public void setCanceled(boolean value) {
	}
	
	@Override
	public void setTaskName(String name) {
	}
	
	@Override
	public void subTask(String name) {
	}
	
	@Override
	public void worked(int work) {
	}
	
}