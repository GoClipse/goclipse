package com.googlecode.goclipse.core.tools;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.lang.ide.core.operations.StartEngineDaemonOperation;
import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

/**
 * Start up an instance of Gocode in server mode.
 */
public class GocodeServerManager implements IDisposable {
	
	public static GocodeServerManager startGocodeServer(IPath path) throws CoreException {
		if (DaemonEnginePreferences.AUTO_START_SERVER.get() == false)
			return null;
			
		if(path == null || path.isEmpty()) {
			throw LangCore.createCoreException("No gocode path provided.", null);
		}
		
		GocodeServerManager gocodeServer = new GocodeServerManager(path);
		gocodeServer.startServer();
		return gocodeServer;
	}
	
	protected final String gocodePath;
	protected ExternalProcessNotifyingHelper gocodeProcess;
	
	public GocodeServerManager(IPath gocodePath) {
		this.gocodePath = gocodePath.toOSString();
	}
	
	public void startServer() throws CoreException {
		
		GoCore.logInfo("starting gocode server [" + gocodePath + "]");
		
		ArrayList2<String> commandLine = new ArrayList2<String>();
		commandLine.add(gocodePath);
		commandLine.add("-s");
		if (GocodeCompletionOperation.USE_TCP) {
			commandLine.add("-sock=tcp");
		}
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		
		EclipseExternalProcessHelper eph = new StartEngineDaemonOperation(GoToolManager.getDefault(), pb).call();
		gocodeProcess = eph.getNotifyingProcessHelper();
	}
	
	public void stopServer() {
		if (gocodeProcess != null) {
			GoCore.logInfo("stopping gocode server");
			
			gocodeProcess.getProcess().destroy();
			gocodeProcess = null;
		}
	}
	
	@Override
	public void dispose() {
		stopServer();
	}
	
	public static IPath getBestGocodePath() {
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

}