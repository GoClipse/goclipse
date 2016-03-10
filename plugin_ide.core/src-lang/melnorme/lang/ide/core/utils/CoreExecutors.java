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

import java.util.concurrent.SynchronousQueue;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.concurrency.ExecutorTaskAgent;
import melnorme.utilbox.concurrency.ICommonExecutor;
import melnorme.utilbox.concurrency.ThreadPoolExecutorExt;
import melnorme.utilbox.concurrency.ThreadPoolExecutorExt.UncaughtExceptionHandler;

public class CoreExecutors {
	
	public static ExecutorTaskAgent newExecutorTaskAgent(Class<?> ownerKlass) {
		return newExecutorTaskAgent(ownerKlass.getSimpleName());
	}
	
	public static ExecutorTaskAgent newExecutorTaskAgent(String name) {
		return new ExecutorTaskAgent(name, new CoreUncaughtExceptionHandler(name));
	}
	
	public static ICommonExecutor newCachedThreadPool(Class<?> ownerKlass) {
		return newCachedThreadPool(ownerKlass, 0);
	}
	
	public static ICommonExecutor newCachedThreadPool(Class<?> ownerKlass, int corePoolSize) {
		String name = ownerKlass.getSimpleName();
		return newCachedThreadPool(name, corePoolSize);
	}
	
	public static ICommonExecutor newCachedThreadPool(String name, int corePoolSize) {
		return new ThreadPoolExecutorExt(corePoolSize, Integer.MAX_VALUE, new SynchronousQueue<Runnable>(), 
			name, new CoreUncaughtExceptionHandler(name));
	}
	
	/* -----------------  ----------------- */
	
	public static class CoreUncaughtExceptionHandler implements UncaughtExceptionHandler {
		
		protected final  String name;
		
		public CoreUncaughtExceptionHandler(String name) {
			this.name = name;
		}
		
		@Override
		public void accept(Throwable throwable) {
			LangCore.logError("Unhandled exception in Executor " + name, throwable);
		}
	}
	
}