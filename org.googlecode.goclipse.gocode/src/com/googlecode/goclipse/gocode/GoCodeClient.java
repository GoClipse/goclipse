package com.googlecode.goclipse.gocode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.ProcessOStreamFilter;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoCodeClient {

	private String error;

	public GoCodeClient() {
	}
	
	public List<String> getCompletions(String fileName, final String bufferText, int offset) {
		
		String goarch = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOARCH);
		
		String goos = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOOS);
		
		String goroot = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOROOT);
		
		String exeName = "gocode" + Environment.INSTANCE.getExecutableExtension();
		
		ExternalCommand configure = 
			new ExternalCommand(Path.fromOSString(goroot).append("bin").append(exeName).toOSString());
		
		
		
		// set the package path for the current project
		List<String> parameters = new LinkedList<String>();
		parameters.add("-sock=tcp");
		parameters.add("set");
		parameters.add("lib-path");
				
		String pkgPath = 
			Environment.INSTANCE.getAbsoluteProjectPath()+
			"/"+Environment.INSTANCE.getPkgOutputFolder().toOSString();
		
		parameters.add(goroot+"/pkg/"+goos+"_"+goarch+":"+pkgPath);
		configure.execute(parameters);
		
		
		
		
		
		ExternalCommand command = new 
			ExternalCommand(Path.fromOSString(goroot).append("bin").append(exeName).toOSString());
		
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
		parameters.add("-sock=tcp");
		parameters.add("-f=csv");
		parameters.add("autocomplete");
		parameters.add(fileName);
		parameters.add(""+offset);
		error = command.execute(parameters, true);
		if (error != null) {
			Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, error));
		}
		return output.getLines();
	}

	public String getError() {
		return error;
	}

}
