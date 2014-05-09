package com.googlecode.goclipse.gocode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.gocode.utils.Utils;

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
    if (Utils.isWindows()) {
      GocodePlugin.winGocodeKill();
    }
    
    GocodePlugin.logInfo("starting gocode server [" + path + "]");
    
    List<String> args = new ArrayList<String>();
    args.add("-s");
    
    if (GocodePlugin.USE_TCP) {
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
  
  private final class GocodeServerListener implements IProcessOutputListener {
	  @Override
	  public void notifyStdOutListeners(byte[] buffer, int offset, int readCount) {
	  }
	  
	  @Override
	  public void notifyStdErrListeners(byte[] buffer, int offset, int readCount) {
		  // XXX: this implementation is buggy if the chunk ends in the middle of a multi-byte unicode character
		  String string = new String(buffer, offset, readCount, StringUtil.UTF8);
		  GocodePlugin.logWarning("gocode:\n" + string.trim());
	  }
	  
	  @Override
	  public void notifyProcessTerminatedAndRead(int exitCode) {
	  }
  }

}
