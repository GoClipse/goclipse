package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.googlecode.goclipse.SysUtils;

public class ConsoleFilter implements ProcessIStreamFilter{

	private MessageConsole console;
	private String prefix;
	private int r = -1;
	private int g = -1;
	private int b = -1;

	public ConsoleFilter(MessageConsole console) {
		this.console = console;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public void clear() {
		
	}

	@Override
	public void process(InputStream iStream) {
		Color c = null;
		try {
			final MessageConsoleStream con = console.newMessageStream();
			InputStreamReader isr = new InputStreamReader(iStream);
		    BufferedReader br = new BufferedReader(isr);
		    String line;
		    
		    if (r != -1) {
		    	c = new Color(Display.getDefault(),r,g,b);
		    	final Color cc = c;
		    	Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						con.setColor(cc);
					}
				});
		    }
	        while ((line = br.readLine()) != null) {
 	    		con.println((prefix==null?"":prefix) + line);		
	        }
	        con.close();
		}catch(Exception e) {
			SysUtils.debug(e);
		}finally {
			if (c != null) {
				//the color is still used by the console
				//FIXME: will it be freed later? or a listener on the console's dispose should free this?
				c = null;
			}
		}
	}
	public void setColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		
	}

}
