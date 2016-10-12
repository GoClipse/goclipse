/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.concurrency;

import melnorme.utilbox.concurrency.Future2;

public interface JobFuture<RET> extends Future2<RET> {
	
	/** Shedule this job future for execution. Has no effect if job already started. */
	void start();
	
	/** @return if job future has been started already. */
	boolean isStarted();
	
}