package com.googlecode.goclipse.builder;

/**
 * 
 * @author steel
 */
public class LineData {
	String line;
	int charactersBeforeLine;
	
	/**
	 * @param line
	 * @param charactersBeforeLine
	 */
	public LineData(String line, int charactersBeforeLine) {
		this.line = line;
		this.charactersBeforeLine = charactersBeforeLine;
	}
}
