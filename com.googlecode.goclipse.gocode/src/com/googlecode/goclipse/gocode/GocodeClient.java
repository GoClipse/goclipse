package com.googlecode.goclipse.gocode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import melnorme.lang.ide.core.utils.process.ExternalProcessEclipseHelper;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Use the configured GoCode settings to call a GoCode client, which ends up talking to a running
 * GoCode server. This class is called from GoCodeContentAssistProcessor.
 */
public class GocodeClient {
  private String error;

  public GocodeClient() {
    
  }

  public List<String> getCompletions(IProject project, String fileName, final String bufferText, int offset) {
	  try {
		return getCompletionsDo(project, fileName, bufferText, offset);
	} catch (CoreException e) {
		return Collections.emptyList();
	}
  }
  
  public List<String> getCompletionsDo(IProject project, String fileName, final String bufferText, int offset)
		  throws CoreException {
    error = null;
    
    String goroot = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);
    
    String goarch = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);
    String goos = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOOS);
    
    
    IPath gocodePath = GocodePlugin.getPlugin().getBestGocodeInstance();
    if (gocodePath == null) {
      return Collections.emptyList();
    }
      
    String gocodePathStr = gocodePath.toOSString();

    // set the package path for the current project
    List<String> parameters = new LinkedList<String>();
    if (GocodePlugin.USE_TCP) {
      parameters.add("-sock=tcp");
    }
    parameters.add("set");
    parameters.add("lib-path");

    IPath rootPath = new Path(goroot).append("pkg").append(goos + "_" + goarch);

    if (project == null) {
      parameters.add(rootPath.toOSString());
    } else {
      IPath projectPath = project.getLocation().append(Environment.INSTANCE.getPkgOutputFolder(project));

      parameters.add(rootPath.toOSString() + File.pathSeparatorChar + projectPath.toOSString());
    }
    
	ExternalProcessEclipseHelper processHelperLibPath = GoToolManager.getDefault().
		runPrivateGoTool(gocodePathStr, parameters, null);
	processHelperLibPath.awaitTermination_CoreException(100);

    parameters = new LinkedList<String>();
    if (GocodePlugin.USE_TCP) {
      parameters.add("-sock=tcp");
    }
    parameters.add("-f=csv");
    parameters.add("autocomplete");
    parameters.add(fileName);
    parameters.add("c" + offset);
    
	ExternalProcessEclipseHelper processHelper = GoToolManager.getDefault().
		runPrivateGoTool(gocodePathStr, parameters, bufferText);
	processHelper.awaitTermination_CoreException();
    
	ByteArrayOutputStreamExt stdout = processHelper.getStdOutBytes_CoreException();
    
    if(processHelper.getProcess().exitValue() != 0) {
      error = "Error running gocode: " + stdout.toString();
      GoCore.logError(error);
    } else {
    	error = null;
    }
    
    StreamAsLines output = new StreamAsLines();
    output.process(new ByteArrayInputStream(stdout.toByteArray()));
    return output.getLines();
  }

  protected String getError() {
    return error;
  }

}
