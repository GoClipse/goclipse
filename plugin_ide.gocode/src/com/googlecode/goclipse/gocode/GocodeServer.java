package com.googlecode.goclipse.gocode;

import java.io.IOException;

import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

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
    
    Process process;
    try {
    	process = new ProcessBuilder(commandLine).start();
	} catch (IOException ioe) {
		UIOperationExceptionHandler.handleError("Could not start gocode:", ioe);
		return;
	}
    
	gocodeProcess = new ExternalProcessNotifyingHelper(process, true, false);
	
	if(GocodePreferences.GOCODE_CONSOLE_ENABLE.get()) {
		GocodeServerListener.getConsole().writeOperationInfo(">>> Starting gocode server:\n");
		GocodeServerListener.getConsole().writeOperationInfo("   " +
				StringUtil.collToString(commandLine, " ") + "\n");
		gocodeProcess.getOutputListenersHelper().addListener(new GocodeServerListener());
	}
	
	gocodeProcess.startReaderThreads();
    
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
