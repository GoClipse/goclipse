package com.googlecode.goclipse.gocode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collections;
import java.util.List;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoEnvironmentPrefs.GoRoot;
import com.googlecode.goclipse.core.GoWorkspace;
import com.googlecode.goclipse.gocode.preferences.GocodePreferences;

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
    
    GoRoot goRoot = GoEnvironmentPrefs.getGoRoot();
    
    
    IPath gocodePath = GocodePlugin.getPlugin().getBestGocodeInstance();
    if (gocodePath == null) {
      return Collections.emptyList();
    }
      
    String gocodePathStr = gocodePath.toOSString();

    // set the package path for the current project
    ArrayList2<String> arguments = new ArrayList2<>();
    if (GocodePreferences.USE_TCP) {
      arguments.add("-sock=tcp");
    }
    arguments.add("set");
    arguments.add("lib-path");

    if (project == null) {
      arguments.add(goRoot.getGoPackagesLocation().toString());
    } else {
      IPath projectPath = new GoWorkspace(project).getPkgFolderLocation();
      
      arguments.add(goRoot.getGoPackagesLocation().toString() + File.pathSeparatorChar + projectPath.toOSString());
    }
    
    // TODO: we should run this outside the UI thread, with an actual monitor to allow cancelling!
    IProgressMonitor pm = new NullProgressMonitor();
	GoToolManager.getDefault().runEngineClientTool(gocodePathStr, arguments, null, pm);

    arguments = new ArrayList2<String>();
    if (GocodePreferences.USE_TCP) {
      arguments.add("-sock=tcp");
    }
    arguments.add("-f=csv");
    arguments.add("autocomplete");
    arguments.add(fileName);
    arguments.add("c" + offset);
    
	ExternalProcessResult processResult = GoToolManager.getDefault().runEngineClientTool(
		gocodePathStr, arguments, bufferText, pm);
    
	ByteArrayOutputStreamExt stdout = processResult.getStdOutBytes();
	
	if(processResult.exitValue != 0) {
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
