package com.googlecode.goclipse.core.tools;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.lang.ide.core.operations.StartEngineDaemonOperation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

/**
 * Start up an instance of Gocode in server mode.
 */
public class GocodeServerManager implements IDisposable {
	
	protected ExternalProcessNotifyingHelper gocodeProcess;
	
	public GocodeServerManager() {
	}
	
	public IPath getBestGocodePath() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
			GoCore.CONTENT_ASSIST_EXTENSION_ID);
		
		try {
			for (IConfigurationElement e : config) {
				final Object extension = e.createExecutableExtension("class");
				
				if (extension instanceof IGocodePathProvider) {
					return ((IGocodePathProvider) extension).getBestGocodePath();
				}
			}
		} catch (CoreException ex) {
			// do nothing
		}
		return null;
	}
	
	public void requestServerStart(IPath path, IProgressMonitor monitor) throws CoreException {
		if (DaemonEnginePreferences.AUTO_START_SERVER.get() == false)
			return;
		
		if(path == null || path.isEmpty()) {
			throw LangCore.createCoreException("No gocode path provided.", null);
		}
		
		if(gocodeProcess != null) {
			// TODO: check path hasn't changed
		} else {
			doStartServer(path, monitor);
		}
	}
	
	public void doStartServer(IPath gocodePath, IProgressMonitor monitor) throws CoreException {
		
		GoCore.createInfoStatus("starting gocode server [" + gocodePath + "]").logInPlugin();
		
		ArrayList2<String> commandLine = new ArrayList2<String>();
		commandLine.add(gocodePath.toOSString());
		commandLine.add("-s");
		if (GocodeCompletionOperation.USE_TCP) {
			commandLine.add("-sock=tcp");
		}
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		
		try {
			gocodeProcess = new StartEngineDaemonOperation(GoToolManager.getDefault(), pb, monitor).startProcess();
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce.getMessage(), ce.getCause());
		}
	}
	
	public void stopServer() {
		if (gocodeProcess != null) {
			GoCore.createInfoStatus("stopping gocode server").logInPlugin();
			
			gocodeProcess.getProcess().destroy();
			gocodeProcess = null;
		}
	}
	
	@Override
	public void dispose() {
		stopServer();
	}
	
}