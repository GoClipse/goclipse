package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class SourceWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
