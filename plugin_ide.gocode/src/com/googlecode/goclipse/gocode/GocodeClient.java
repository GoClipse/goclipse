package com.googlecode.goclipse.gocode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
    List<String> arguments = new LinkedList<String>();
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
    
	GoToolManager.getDefault().startPrivateGoTool(gocodePathStr, arguments, null).strictAwaitTermination(100);

    arguments = new LinkedList<String>();
    if (GocodePreferences.USE_TCP) {
      arguments.add("-sock=tcp");
    }
    arguments.add("-f=csv");
    arguments.add("autocomplete");
    arguments.add(fileName);
    arguments.add("c" + offset);
    
	EclipseExternalProcessHelper processHelper = GoToolManager.getDefault().
		startPrivateGoTool(gocodePathStr, arguments, bufferText);
	
    if(GocodePreferences.GOCODE_CONSOLE_ENABLE.get()) {
        GocodeMessageConsole gocodeConsole = GocodeMessageConsole.getConsole();
    	gocodeConsole.writeOperationInfo(">> Running: " + gocodePathStr + " "
    			+ StringUtil.collToString(arguments, " ") + "\n");
    }
	
	ExternalProcessResult processResult = processHelper.strictAwaitTermination();
    
	ByteArrayOutputStreamExt stdout = processResult.getStdOutBytes();
	ByteArrayOutputStreamExt stderr = processResult.getStdErrBytes();
	
	if(processHelper.getProcess().exitValue() != 0) {
		error = "Error running gocode: " + stdout.toString();
		GoCore.logError(error);
	} else {
		error = null;
	}
    
    if(GocodePreferences.GOCODE_CONSOLE_ENABLE.get()) {
        GocodeMessageConsole gocodeConsole = GocodeMessageConsole.getConsole();
        try {
    		gocodeConsole.stdOut.write(stdout.toString());
    		gocodeConsole.stdErr.write(stderr.toString());
    	} catch (IOException e) {
    		// ignore
    	}
    }
    
    StreamAsLines output = new StreamAsLines();
    output.process(new ByteArrayInputStream(stdout.toByteArray()));
    return output.getLines();
  }

  protected String getError() {
    return error;
  }

}
