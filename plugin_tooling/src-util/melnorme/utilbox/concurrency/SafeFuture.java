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
package melnorme.utilbox.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Like a {@link Future}, but does not throw {@link ExecutionException}s 
 */
public interface SafeFuture<DATA> extends Future<DATA> {
	
	@Override
	public DATA get() throws InterruptedException;
	
	@Override
	public DATA get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException;
	
}