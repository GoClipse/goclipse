package com.googlecode.goclipse.builder;

import java.io.InputStream;

public interface ProcessIStreamFilter {
	public void process(InputStream iStream);
	public void clear();
}
