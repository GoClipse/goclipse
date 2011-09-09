package com.googlecode.goclipse.debug.gdb;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author devoncarew
 */
public class GdbProperties {
	protected String name;
	protected Map<String, Object> properties = new HashMap<String, Object>();
	
	public GdbProperties() {
		
	}
	
	public GdbProperties(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public String getPropertyString(String key) {
		return (String)getProperty(key);
	}

	public Set<String> getKeys() {
		return properties.keySet();
	}
	
	void parseKeyValues(PushbackReader in) throws IOException {
		while (true) {
			parseKeyValue(in);
			
			int ch = in.read();
			
			if (ch != ',') {
				if (ch != -1) {
					in.unread(ch);
				}
				
				return;
			}
		}
	}
	
	void parseKeyValue(PushbackReader in) throws IOException {
		String name = parseName(in);
		
		int ch = in.read();
		
		if (ch != '=') {
			throw new IOException("unexpected char: " + (char)ch);
		}
		
		Object value = parseValue(in);
		
		if (value instanceof GdbProperties) {
			GdbProperties props = (GdbProperties)value;
			
			props.name = name;
		}
		
		properties.put(name, value);
	}

	String parseName(PushbackReader in) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		int ch = in.read();
		
		while (ch != '=' && ch != -1) {
			builder.append((char)ch);
			
			ch = in.read();
		}
		
		if (ch != -1) {
			in.unread(ch);
		}
		
		return builder.toString();
	}

	Object parseValue(PushbackReader in) throws IOException {
		int ch = in.read();
		
		switch (ch) {
		case '"':
			return parseStringValue(in);
		case '{':
			return parsePropertiesValue(in);
		case '[':
			return parseArrayValue(in);
		default:
			throw new IOException("unexpected char: " + (char)ch);
		}
	}

	String parseStringValue(PushbackReader in) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		int lb = ' ';
		int ch = in.read();
				
		while ((lb=='\\' && ch == '"') || ch != '"' && ch != -1) {
			builder.append((char)ch);
			lb = ch;
			ch = in.read();
		}
		
		return builder.toString();
	}

	GdbProperties parsePropertiesValue(PushbackReader in) throws IOException {
		GdbProperties props = new GdbProperties();
		
		props.parseKeyValues(in);
		
		int ch = in.read();
		
		if (ch != '}') {
			throw new IOException("unexpected char: " + (char)ch);
		}
		
		return props;
	}

	Object[] parseArrayValue(PushbackReader in) throws IOException {
		// [object,oject,object]
		// {, or alphanum, or ]
		
		List<Object> objects = new ArrayList<Object>();
		
		while (true) {
			int ch = in.read();
			
			if (ch == ']') {
				return objects.toArray();
			} else if (ch == '{') {
				objects.add(parsePropertiesValue(in));
			} else {
				in.unread(ch);
				GdbProperties props = new GdbProperties();
				props.parseKeyValue(in);
				objects.add(props);
			}
			
			ch = in.read();
			
			if (ch == ']') {
				return objects.toArray();
			} else if (ch != ',') {
				throw new IOException("unexpected char: " + (char)ch);
			}
		}
	}

	public int getPropertyParseInt(String key) {
		try {
			return Integer.parseInt(getPropertyString(key));
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

}
