/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.actions;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

public class GocodeClient extends GocodeCompletionOperation {
	
	protected final IProgressMonitor pm;
	
	public GocodeClient(String gocodePath, GoEnvironment goEnvironment, IProgressMonitor pm) {
		super(goEnvironment, gocodePath);
		this.pm = pm;
	}
	
	@Override
	protected ExternalProcessResult runGocodeProcess(ProcessBuilder pb, String input) 
			throws CommonException, OperationCancellation {
		EclipseUtils.checkMonitorCancelation(pm);
		
		return GoToolManager.getDefault().new RunEngineClientOperation(pb, pm).doRunProcess(input, false);
	}
	
}