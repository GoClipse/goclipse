package com.googlecode.goclipse.gocode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.IPath;

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
      GocodePlugin.winGocodeKill();
    }
    
    GocodePlugin.logInfo("starting gocode server [" + path + "]");
    
    List<String> args = new ArrayList<String>();
    args.add("-s");
    
    if (GocodePreferences.USE_TCP) {
      args.add("-sock=tcp");
    }
    
    try {
    	Process process = new ProcessBuilder(path.toOSString()).start();
   		gocodeProcess = new ExternalProcessNotifyingHelper(process, true, false);
   		gocodeProcess.getOutputListenersHelper().addListener(new GocodeServerListener());
   		gocodeProcess.startReaderThreads();
	} catch (IOException e) {
		// BM TODO: perhaps report this to user in UI
		GocodePlugin.logError("Could not start gocode:\n");
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

}
