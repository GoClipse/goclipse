package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.goclipse.SysUtils;

public class StreamAsLines implements ProcessIStreamFilter {

	private List<String> lines = new ArrayList<String>();
	@Override
	public void process(InputStream iStream) {
		try {
			lines.clear();
			InputStreamReader isr = new InputStreamReader(iStream);
		    BufferedReader br = new BufferedReader(isr);
		    String line;
	        while ((line = br.readLine()) != null) {
	        	lines.add(line);
	        }
		}catch(Exception e) {
			SysUtils.debug(e);
		}
	}
	public List<String> getLines() {
		return lines;
	}
	
	public void clear() {
		lines.clear();
	}

}
