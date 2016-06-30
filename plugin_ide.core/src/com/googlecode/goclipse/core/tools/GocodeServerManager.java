package com.googlecode.goclipse.core.tools;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;

import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

/**
 * Start up an instance of Gocode in server mode.
 */
public class GocodeServerManager implements IDisposable {
	
	protected final GoToolManager toolMgr = LangCore.getToolManager();
	protected ExternalProcessNotifyingHelper gocodeProcess;
	
	public GocodeServerManager() {
	}
	
	public IPath getGocodePath() {
		String pref = ToolchainPreferences.DAEMON_PATH.get();
		
		if (pref == null || pref.length() == 0) {
			return null;
		}
		
		return new Path(pref);
	}
	
	protected boolean isChildServerRunning() {
		return gocodeProcess != null;
	}
	
	public boolean prepareServerStart() throws CommonException {
		IPath gocodePath = getGocodePath();
		
		if(gocodePath == null || gocodePath.isEmpty()) {
			throw new CommonException("No gocode path provided.", null);
		}
		
		// TODO: check path hasn't changed
		return gocodeProcess == null;
	}
	
	public void doStartServer(IOperationMonitor monitor) throws CommonException, OperationCancellation {
		IPath gocodePath = getGocodePath(); 
		
		ArrayList2<String> commandLine = new ArrayList2<String>();
		commandLine.add(gocodePath.toOSString());
		commandLine.add("-s");
		if (GocodeCompletionOperation.USE_TCP) {
			commandLine.add("-sock=tcp");
		}
		
		LangCore.logInfo("Starting gocode server: " + 
			DebugPlugin.renderArguments(commandLine.toArray(String.class), null));
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		
		IToolOperationMonitor opMonitor = toolMgr.startNewOperation(ProcessStartKind.ENGINE_SERVER, true, false);
		String prefixText = "==== Starting gocode server ====\n";
		
		gocodeSetEnableBuiltins(gocodePath, monitor, opMonitor, prefixText);
		
		gocodeProcess = toolMgr.new RunToolTask(opMonitor, prefixText, pb, monitor).startProcess();
	}
	
	protected void gocodeSetEnableBuiltins(IPath gocodePath, IOperationMonitor om, IToolOperationMonitor toolOpMonitor, 
			String prefixText)
			throws CommonException, OperationCancellation {
		ProcessBuilder pb = new ProcessBuilder(gocodePath.toString(), "set", "propose-builtins", "true");
		toolMgr.new RunToolTask(toolOpMonitor, prefixText, pb, om).runProcess();
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