package com.googlecode.goclipse.core.tools;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.StartEngineDaemonOperation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

/**
 * Start up an instance of Gocode in server mode.
 */
public class GocodeServer {
	
	private IPath path;
	protected ExternalProcessNotifyingHelper gocodeProcess;
	
	public GocodeServer(IPath gocodePath) {
		this.path = gocodePath;
	}
	
	public void startServer() {
		
		GoCore.logInfo("starting gocode server [" + path + "]");
		
		ArrayList2<String> commandLine = new ArrayList2<String>();
		commandLine.add(path.toOSString());
		
		commandLine.add("-s");
		
		if (GocodeCompletionOperation.USE_TCP) {
			commandLine.add("-sock=tcp");
		}
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		
		try {
			new StartEngineDaemonOperation(GoToolManager.getDefault(), pb).call();
		} catch (CoreException ce) {
			LangCore.logStatus(ce.getStatus());
			return;
		}
		
	}
	
	public void stopServer() {
		if (gocodeProcess != null) {
			GoCore.logInfo("stopping gocode server");
			
			gocodeProcess.getProcess().destroy();
			gocodeProcess = null;
		}
	}
	
}
