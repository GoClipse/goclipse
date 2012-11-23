package com.googlecode.goclipse.debug.utils;

import com.googlecode.goclipse.debug.GoDebugPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class ProcessRunner {
  private ProcessBuilder builder;
  private StringBuilder stdout = new StringBuilder();
  
  public ProcessRunner(String... args) {
    builder = new ProcessBuilder(args);
    builder.redirectErrorStream(true);
  }

  public int execute() throws IOException {
    stdout.setLength(0);
    
    final Process process = builder.start();
    
    // Read from stdout.
    final Thread stdoutThread = new Thread(new Runnable() {
      @Override
      public void run() {
        pipeOutput(process.getInputStream(), stdout);
      }
    });

    stdoutThread.start();
    
    try {
      return process.waitFor();
    } catch (InterruptedException e) {
      return 0;
    }
  }

  public String getStdout() {
    return stdout.toString();
  }

  protected void pipeOutput(InputStream in, StringBuilder builder) {
    try {
      Reader reader = new InputStreamReader(in, "UTF-8");
      char[] buffer = new char[512];

      int count = reader.read(buffer);

      while (count != -1) {
        builder.append(buffer, 0, count);

        count = reader.read(buffer);
      }
    } catch (UnsupportedEncodingException e) {
      GoDebugPlugin.logError(e);
    } catch (IOException e) {
      // This exception is expected.

    }
  }

}
