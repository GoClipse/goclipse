package com.googlecode.goclipse.builder;

import java.io.File;
import java.io.IOException;
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
			InputStream is = p.getInputStream();
			if (resultsFilter != null) {
				resultsFilter.clear();
				resultsFilter.process(is);//the filter will use the stream
			}
			consume(is);
			is.close();
			//should allow error stream to be filtered
			InputStream es = p.getErrorStream();
			consume(es);
			es.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			rez = e.getLocalizedMessage();
		}
		return rez;
	}
	
	private void consume(InputStream is) {
		//go through stream up to the end
		byte[] buf = new byte[256];
		try {
			while(is.read(buf, 0, buf.length)>0) {
				//just consume data
			}
		}catch(IOException e) {
			//ignore
		}
	}

}
