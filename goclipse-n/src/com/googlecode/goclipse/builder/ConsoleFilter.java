package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.ui.console.MessageConsoleStream;

import com.googlecode.goclipse.SysUtils;

public class ConsoleFilter implements ProcessIStreamFilter{

	private MessageConsoleStream con;
	private String prefix;

	public ConsoleFilter(MessageConsoleStream con) {
		this.con = con;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public void clear() {
		
	}

	@Override
	public void process(InputStream iStream) {
		try {
			InputStreamReader isr = new InputStreamReader(iStream);
		    BufferedReader br = new BufferedReader(isr);
		    String line;
	        while ((line = br.readLine()) != null) {
 	    		con.println((prefix==null?"":prefix) + line);		
	        }
		}catch(Exception e) {
			SysUtils.debug(e);
		}
	}

}
