package com.googlecode.goclipse.builder;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.SysUtils;

public class ExternalCommand {
	private String command;
	private ProcessBuilder pBuilder;
	private List<String> args = new ArrayList<String>();
	private ProcessIStreamFilter resultsFilter;
	
	/**
	 * new external command using a full path
	 * @param command
	 */
	public ExternalCommand(String command) {
		this.command = command;
		pBuilder = new ProcessBuilder(args);
		String workingFolder = Path.fromOSString(command).removeLastSegments(1).toOSString();
		setWorkingFolder(workingFolder);
	}

	public void setEnvironment(Map<String, String> env) {
		if (env != null) {
			pBuilder.environment().putAll(env);
		}
	}
	
	public void setWorkingFolder(String folder) {
		pBuilder.directory(new File(folder));
	}
	
	public void setResultsFilter(ProcessIStreamFilter resultsFilter) {
		this.resultsFilter = resultsFilter;
	}

	

	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * returns an error string or null if no errors occured
	 * @param parameters
	 * @return
	 */
	public String execute(List<String> parameters) {
		String rez = null;
		try {
			args.clear();
			args.add(command);
			if (parameters != null) {
				args.addAll(parameters);
			}
			SysUtils.debug(pBuilder.directory() + " executing: " +  args);
			
			Process p = pBuilder.start();
			if (resultsFilter != null) {
				resultsFilter.clear();
				InputStream is = p.getInputStream();
				resultsFilter.process(is);
				is.close();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			rez = e.getLocalizedMessage();
		}
		return rez;
	}

}
