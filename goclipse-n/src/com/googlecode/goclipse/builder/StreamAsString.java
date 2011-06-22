package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.googlecode.goclipse.SysUtils;

public class StreamAsString implements ProcessIStreamFilter {

	private StringBuilder streamContent;
	@Override
	public void process(InputStream iStream) {
		try {
			clear();
			InputStreamReader isr = new InputStreamReader(iStream);
		    BufferedReader br = new BufferedReader(isr);
		    char [] cbuf = new char[4096];
		    int n;
		    while ((n=br.read(cbuf))>0) {
	        	streamContent.append(cbuf, 0, n);
	        }
		}catch(Exception e) {
			SysUtils.debug(e);
		}
	}
	public String getString() {
		return streamContent == null ? "" : streamContent.toString();
	}
	
	public void clear() {
		streamContent = new StringBuilder();
	}

}
