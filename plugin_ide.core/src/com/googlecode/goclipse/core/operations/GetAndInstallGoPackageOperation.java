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
package com.googlecode.goclipse.core.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.utils.operation.CoreOperationRunnable;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class GetAndInstallGoPackageOperation implements CoreOperationRunnable {
	
	protected final AbstractToolManager toolMgr = LangCore.getToolManager();
	
	protected final GoEnvironment goEnv;
	protected final String goPackage;
	protected final String exeName;
	protected boolean preventWindowsConsoleGUI = false;
	
	public GetAndInstallGoPackageOperation(GoEnvironment goEnv, String goPackage, String exeName) {
		this.goEnv = assertNotNull(goEnv);
		this.goPackage = assertNotNull(goPackage);
		this.exeName = assertNotNull(exeName);
	}
	
	protected Location workingDir;
	
	@Override
	public void doRun(IProgressMonitor monitor) throws CommonException, CoreException, OperationCancellation {
		ProcessBuilder pb = getProcessToStart();
		
		IOperationConsoleHandler opHandler = toolMgr.startNewOperation(ProcessStartKind.ENGINE_TOOLS, false, true);
		toolMgr.newRunProcessTask(opHandler, pb, monitor).runProcess();
	}
	
	public ProcessBuilder getProcessToStart() throws CommonException {
		workingDir = getFirstGoPathEntry(goEnv);
		
		ArrayList2<String> cmdLine = getCmdLine();
		
		return goEnv.createProcessBuilder(cmdLine, workingDir, true);
	}
	
	protected Location getFirstGoPathEntry(GoEnvironment goEnv) throws CommonException {
		GoPath goPath = goEnv.getGoPath();
		
		if(goPath.isEmpty()) {
			throw new CommonException("GOPATH is empty, can't install.");
		}
		String workingDirStr = goPath.getGoPathEntries().get(0);
		Location workingDir = Location.createValidLocation(workingDirStr, "Invalid GOPATH: ");
		return workingDir;
	}
	
	public ArrayList2<String> getCmdLine() throws CommonException {
		String sdkPath = toolMgr.getSDKToolPath(null).toString();
		ArrayList2<String> cmdLine = CollectionUtil.createArrayList(sdkPath, "get", "-u");
		
		if(preventWindowsConsoleGUI && MiscUtil.OS_IS_WINDOWS) {
			cmdLine.addElements("-ldflags", "-H=windowsgui");
		}
		
		cmdLine.addElements(goPackage);
		return cmdLine;
	}
	
	public Location getDownloadedToolLocation() {
		return workingDir.resolve_fromValid("bin/"+exeName + (MiscUtil.OS_IS_WINDOWS ? ".exe" : ""));
	}
	
}