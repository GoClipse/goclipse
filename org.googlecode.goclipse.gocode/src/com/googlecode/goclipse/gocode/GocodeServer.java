package com.googlecode.goclipse.gocode;

import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.ProcessIStreamFilter;
import com.googlecode.goclipse.gocode.utils.Utils;

import org.eclipse.core.runtime.IPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Start up an instance of Gocode in server mode.
 */
public class GocodeServer {
  private IPath path;
  private ExternalCommand command;
  
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
    
    command = new ExternalCommand(path, false);
    command.setErrorFilter(new ProcessIStreamFilter() {
      @Override
      public void process(InputStream in) {
        writeInputToLog(in);
      }

      @Override
      public void clear() {

      }
    });
    command.execute(args);
  }
  
  public void stopServer() {
    if (command != null) {
      GocodePlugin.logInfo("stopping gocode server");
      
      command.destroy();
      command = null;
    }
  }

  public IPath getPath() {
    return path;
  }
  
  protected void writeInputToLog(InputStream in) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    char[] buffer = new char[4096];
    
    try {
      int count = reader.read(buffer);
      
      while (count != -1) {
        GocodePlugin.logWarning("gocode:\n" + (new String(buffer, 0, count)).trim());
        
        count = reader.read(buffer);
      }
    } catch (IOException exception) {
      GocodePlugin.logError(exception);
    }
  }

}
