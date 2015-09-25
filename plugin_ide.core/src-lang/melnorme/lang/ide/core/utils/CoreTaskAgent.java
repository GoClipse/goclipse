/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.concurrency.ExecutorTaskAgent;

public class CoreTaskAgent extends ExecutorTaskAgent {
	
	public CoreTaskAgent(String name) {
		super(name);
	}
	
	@Override
	protected void handleUnexpectedException(Throwable throwable) {
		// Log unexpected exceptions. This is important for two reasons:
		// 1: Give some user feedback an internal error ocurred, even if is for the log only.
		// 2: For tests to be able to determine if exceptions that should fail the test have occurred.
		LangCore.logError("Unhandled exception in CoreExecutor: " + getName(), throwable);
	}
	
}