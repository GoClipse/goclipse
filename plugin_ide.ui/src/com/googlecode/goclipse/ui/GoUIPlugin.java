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
package com.googlecode.goclipse.ui;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.core.tools.GocodeServerManager;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	protected static GocodeServerManager gocodeServerManager = new GocodeServerManager();
	
	protected GoOperationsConsoleListener operationsListener;
	
	@Override
	protected void doCustomStart_finalStage() {
		operationsListener = new GoOperationsConsoleListener();
		GoToolManager.getDefault().addListener(operationsListener);
		
		super.doCustomStart_finalStage();
	}
	
	@Override
	protected void doInitializeAfterLoad(IProgressMonitor monitor) throws CoreException {
	}
	
	public static GocodeServerManager prepareGocodeManager_inUI() {
		assertTrue(Display.getCurrent() != null); // This must run from UI thread
		try {
			gocodeServerManager.prepareGocodeServer();
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handleOperationStatus("Error starting gocode server.", ce);
		}
		return gocodeServerManager;
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
		MiscUtil.dispose(gocodeServerManager);
		gocodeServerManager = null;
		
		GoToolManager.getDefault().removeListener(operationsListener);
	}
	
}