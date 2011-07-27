package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.goclipse.Activator;

public class StreamAsLines implements ProcessIStreamFilter {
	private boolean combineLines;
	
	private List<String> lines = new ArrayList<String>();
	
	public StreamAsLines() {
		
	}
	
	/**
	 * If true then successive lines indented by a tab will be combined into one line.
	 * This is used by {@link GoCompiler}.
	 * 
	 * @param value
	 */
	public void setCombineLines(boolean value) {
		this.combineLines = value;
	}
	
	@Override
	public void process(InputStream iStream) {
		try {
			lines.clear();
			InputStreamReader isr = new InputStreamReader(iStream);
		    BufferedReader br = new BufferedReader(isr);
		    String line;
	        while ((line = br.readLine()) != null) {
	        	if (combineLines && line.startsWith("\t") && lines.size() > 0) {
	        		int index = lines.size() - 1;
	        		
	        		lines.set(index, lines.get(index) + " - " + line.substring(1));
	        	} else {	        	
	        		lines.add(line);
	        	}
	        }
		}catch(Exception e) {
			Activator.logInfo(e);
		}
	}
	
	public List<String> getLines() {
		return lines;
	}
	
	public void clear() {
		lines.clear();
	}

}
