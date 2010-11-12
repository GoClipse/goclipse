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
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.builder.ProcessOStreamFilter;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoCodeClient {

	private String error;

	public GoCodeClient() {
	}
	
	public List<String> getCompletions(String fileName, final String bufferText, int offset) {
		String goroot = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOROOT);
		String exeName = "gocode" + Environment.INSTANCE.getExecutableExtension();
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

		List<String> parameters = new LinkedList<String>();
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
