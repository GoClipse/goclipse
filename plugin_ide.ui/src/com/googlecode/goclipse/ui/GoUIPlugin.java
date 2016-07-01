/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui;

import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.ui.actions.StartGocodeServerOperation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class GoUIPlugin extends LangUIPlugin {
	
	@Override
	protected GoOperationsConsoleUIHandler createOperationsConsoleListener() {
		return new GoOperationsConsoleUIHandler();
	}
	
	@Override
	protected void doInitializeAfterLoad(IOperationMonitor om) throws CommonException {
	}
	
	public static GocodeServerManager prepareGocodeManager_inUI() 
			throws CommonException, OperationCancellation {
		
		GocodeServerManager gocodeServerManager = LangCore.get().gocodeServerManager();
		new StartGocodeServerOperation().execute(gocodeServerManager);
		
		return gocodeServerManager;
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
	}
	
}