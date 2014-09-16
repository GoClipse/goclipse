package com.googlecode.goclipse.gocode;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.StartEngineDaemonOperation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.gocode.preferences.GocodePreferences;

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
    if (MiscUtil.OS_IS_WINDOWS) {
      GocodeServer.winGocodeKill();
    }
    
    GocodePlugin.logInfo("starting gocode server [" + path + "]");
    
    ArrayList2<String> commandLine = new ArrayList2<String>();
    commandLine.add(path.toOSString());
    
    commandLine.add("-s");

    if (GocodePreferences.USE_TCP) {
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
      GocodePlugin.logInfo("stopping gocode server");
      
      gocodeProcess.getProcess().destroy();
      gocodeProcess = null;
    }
  }

  public IPath getPath() {
    return path;
  }

  public static void winGocodeKill() {
    // shutdown previous gocode instances with command:
    //    TASKKILL /F /IM "gocode.exe"
    try {
      ExternalProcessHelper ph = new ExternalProcessHelper(
    	  new ProcessBuilder("TASKKILL", "/F", "/IM", "\"gocode.exe\""));
      ph.strictAwaitTermination();
      
    } catch (Exception error) {
      GocodePlugin.getPlugin().getLog().log(
          new Status(IStatus.ERROR, GocodePlugin.PLUGIN_ID,
              "Windows taskkill process failed.  Could not kill gocode process."));
    }
  }

}
