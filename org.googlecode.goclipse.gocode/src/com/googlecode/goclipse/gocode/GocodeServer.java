package com.googlecode.goclipse.gocode;

import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.ProcessIStreamFilter;
import com.googlecode.goclipse.gocode.utils.Utils;

import org.eclipse.core.runtime.IPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

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
    
    //System.out.println("starting gocode [" + path + "]");
    
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
    command.execute(Arrays.asList(new String[] {"-s"}));
    
  }
  
  public void stopServer() {
    if (command != null) {
      //System.out.println("stopping gocode");
      
      command.destroy();
      command = null;
    }
  }

  public IPath getPath() {
    return path;
  }
  
  protected void writeInputToLog(InputStream in) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    
    try {
      String line = reader.readLine();
      
      while (line != null) {
        GocodePlugin.logWarning("gocode: " + line);
        
        line = reader.readLine();
      }
    } catch (IOException exception) {
      GocodePlugin.logError(exception);
    }
  }

}
