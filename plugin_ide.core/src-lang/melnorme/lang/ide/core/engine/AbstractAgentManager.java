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
package melnorme.lang.ide.core.engine;


import org.eclipse.core.runtime.Platform;

import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.utilbox.concurrency.ICommonExecutor;
import melnorme.utilbox.misc.SimpleLogger;
import melnorme.utilbox.ownership.LifecycleObject;

public class AbstractAgentManager extends LifecycleObject {
	
	public final SimpleLogger log = init_log();
	
	protected final ICommonExecutor executor = init_executor();
	
	public AbstractAgentManager() {
	}
	
	protected SimpleLogger init_log() {
		return new SimpleLogger(Platform.inDebugMode());
	}
	protected ICommonExecutor init_executor() {
		return CoreExecutors.newCachedThreadPool(getClass());
	}
	
	@Override
	protected void dispose_post() {
		executor.shutdown();
	}
	
}