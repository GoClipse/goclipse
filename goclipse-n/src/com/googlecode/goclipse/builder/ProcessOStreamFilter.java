package com.googlecode.goclipse.builder;

import java.io.OutputStream;

public interface ProcessOStreamFilter {
	/**
	 * called by the Command when the stream is available
	 * the stream may be used to pass data as input to the process
	 * 
	 * this method should return fast.
	 * @param outputStream
	 */
	void setStream(OutputStream outputStream);
}
