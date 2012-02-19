package com.googlecode.goclipse.gocode;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.ProcessOStreamFilter;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.preferences.PreferenceConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Use the configured GoCode settings to call a GoCode client, which ends up talking to a running
 * GoCode server. This class is called from GoCodeContentAssistProcessor.
 */
public class GocodeClient {
  private String error;

  public GocodeClient() {
    
  }

  /**
   * @param fileName
   * @param bufferText
   * @param offset
   * @return
   */
  public List<String> getCompletions(IProject project, String fileName, final String bufferText, int offset) {
    error = null;
    
    String goroot = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);
    
    String goarch = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);
    String goos = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOOS);
    
    ExternalCommand goCodeCommand = buildGoCodeCommand();
    
    if (goCodeCommand == null) {
      return Collections.emptyList();
    }

    goCodeCommand.setTimeout(100);

    // set the package path for the current project
    List<String> parameters = new LinkedList<String>();
    parameters.add("set");
    parameters.add("lib-path");

    String rootPath = goroot + "/pkg/" + goos + "_" + goarch;

    File pkgDir = new File(goroot + "/pkg/");
    if (pkgDir.isDirectory() && pkgDir.listFiles().length > 0) {
      rootPath = pkgDir.listFiles()[0].getAbsolutePath();
    }

    // remove drive letters
    if (rootPath.contains(":")) {
      rootPath = rootPath.replaceFirst("[A-Z]:", "");
    }

    if (project == null) {
      parameters.add(rootPath.replace("\\", "/"));
    } else {
      IPath pkgPath = project.getFullPath().append(Environment.INSTANCE.getPkgOutputFolder(project));
      String pkgPathStr = pkgPath.toOSString();

      if (pkgPathStr.contains(":")) {
        pkgPathStr = pkgPathStr.replaceFirst("[A-Z]:", "");
      }

      parameters.add((rootPath + ":" + pkgPathStr).replace("\\", "/"));
    }
        
    goCodeCommand.execute(parameters);

    ExternalCommand command = buildGoCodeCommand();

    StreamAsLines output = new StreamAsLines();
    command.setResultsFilter(output);
    command.setInputFilter(new ProcessOStreamFilter() {
      @Override
      public void setStream(OutputStream outputStream) {
        OutputStreamWriter osw = new OutputStreamWriter(outputStream);
        try {
          osw.append(bufferText);
          osw.flush();
          outputStream.close();
        } catch (IOException e) {
          // do nothing
        }
      }
    });

    parameters = new LinkedList<String>();
//		parameters.add("-sock=tcp");
    parameters.add("-f=csv");
    parameters.add("autocomplete");
    parameters.add(fileName);
    parameters.add("" + offset);
    error = command.execute(parameters, true);
    if (error != null) {
      String out = output.getLinesAsString();

      Activator.getDefault().getLog().log(
          new Status(Status.ERROR, Activator.PLUGIN_ID, out == null ? error : error + ": " + out));
    }

    return output.getLines();
  }

  protected String getError() {
    return error;
  }

  private ExternalCommand buildGoCodeCommand() {
    IPath gocodePath = GocodePlugin.getPlugin().getBestGocodeInstance();
    
    if (gocodePath == null) {
      return null;
    } else {
      return new ExternalCommand(gocodePath, true);
    }
  }

}
