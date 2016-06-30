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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.ui.actions.StartGocodeServerOperation;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.Disposable;

public class GoUIPlugin extends LangUIPlugin {
	
	protected static GocodeServerManager gocodeServerManager = new GocodeServerManager();
	
	@Override
	protected GoOperationsConsoleUIHandler createOperationsConsoleListener() {
		return new GoOperationsConsoleUIHandler();
	}
	
	@Override
	protected void doInitializeAfterLoad(IProgressMonitor monitor) throws CoreException {
	}
	
	public static GocodeServerManager prepareGocodeManager_inUI() 
			throws CommonException, OperationCancellation {
		
		new StartGocodeServerOperation().execute(gocodeServerManager);
		
		return gocodeServerManager;
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
		Disposable.dispose(gocodeServerManager);
		gocodeServerManager = null;
	}
	
}