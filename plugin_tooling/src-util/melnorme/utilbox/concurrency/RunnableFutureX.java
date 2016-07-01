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
package melnorme.utilbox.concurrency;

import java.util.concurrent.RunnableFuture;

/**
 * An analogue of {@link RunnableFuture}, but using {@link FutureX} which has a safer API.
 * 
 * Just like {@link RunnableFuture}, 
 * successful execution of the {@link #run()} method causes completion of the future.
 * 
 */
public interface RunnableFutureX<RESULT, EXCEPTION extends Throwable> extends Runnable, FutureX<RESULT, EXCEPTION> {
	
}
