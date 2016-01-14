package com.googlecode.goclipse.core.tools;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.utils.operation.EclipseCancelMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

/**
 * Start up an instance of Gocode in server mode.
 */
public class GocodeServerManager implements IDisposable {
	
	protected ExternalProcessNotifyingHelper gocodeProcess;
	
	public GocodeServerManager() {
	}
	
	public static IPath getGocodePath() {
		String pref = ToolchainPreferences.DAEMON_PATH.get();
		
		if (pref == null || pref.length() == 0) {
			return null;
		}
		
		return new Path(pref);
	}
	
	protected boolean isChildServerRunning() {
		return gocodeProcess != null;
	}
	
	public void requestServerStart(IPath path, IProgressMonitor monitor) throws CoreException {
		boolean needsStart = prepareServerStart(path);
		
		if(needsStart) {
			doStartServer(path, monitor);
		}
	}
	
	public boolean prepareServerStart(IPath gocodePath) throws CoreException {
		if(gocodePath == null || gocodePath.isEmpty()) {
			throw LangCore.createCoreException("No gocode path provided.", null);
		}
		
		// TODO: check path hasn't changed
		return gocodeProcess == null;
	}
	
	public void doStartServer(IPath gocodePath, IProgressMonitor monitor) throws CoreException {
		
		LangCore.createInfoStatus("starting gocode server [" + gocodePath + "]").logInPlugin();
		
		ArrayList2<String> commandLine = new ArrayList2<String>();
		commandLine.add(gocodePath.toOSString());
		commandLine.add("-s");
		if (GocodeCompletionOperation.USE_TCP) {
			commandLine.add("-sock=tcp");
		}
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		
		try {
			GoToolManager toolMgr = GoToolManager.getDefault();
			IOperationConsoleHandler opHandler = toolMgr.startNewOperation(ProcessStartKind.ENGINE_SERVER, true, false);
			String prefixText = "Starting gocode server";
			gocodeProcess = toolMgr.new RunToolTask(opHandler, prefixText, pb, 
				new EclipseCancelMonitor(monitor)).startProcess();
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce.getMessage(), ce.getCause());
		}
	}
	
	public void stopServer() {
		if (gocodeProcess != null) {
			LangCore.createInfoStatus("stopping gocode server").logInPlugin();
			
			gocodeProcess.getProcess().destroy();
			gocodeProcess = null;
		}
	}
	
	@Override
	public void dispose() {
		stopServer();
	}
	
}