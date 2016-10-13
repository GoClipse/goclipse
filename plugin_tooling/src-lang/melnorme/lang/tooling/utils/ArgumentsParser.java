/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.utils;

import static java.lang.Character.isWhitespace;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.utils.parse.LexingUtils;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class ArgumentsParser {

	protected final char singleQuote;
	protected final char doubeQuote;
	protected final char escapeChar;
	
	public ArgumentsParser() {
		this('\'', '"', '\\');
	}
	
	public static Indexable<String> parse(String argsSource) {
		return new ArgumentsParser().parseFrom(argsSource);
	}
	
	public ArgumentsParser(char singleQuote, char doubeQuote, char escapeChar) {
		this.singleQuote = singleQuote;
		this.doubeQuote = doubeQuote;
		this.escapeChar = escapeChar;
	}
	
	public Indexable<String> parseFrom(String source) {
		StringCharSource charSource = new StringCharSource(source);
		return parseArguments(charSource, new ArrayList2<>());
	}
	
	public ArrayList2<String> parseArguments(StringCharSource source, ArrayList2<String> args) {
		while(true) {
			
			LexingUtils.skipWhitespace(source);
			
			if(!source.hasCharAhead()) {
				return args;
			}
			
			String arg = parseArgument(source);
			assertNotNull(arg);
			args.add(arg);
		}
	}
	
	protected String parseArgument(StringCharSource source) {
		if(source.tryConsume(singleQuote)) {
			return LexingUtils.consumeUntilDelimiterOrEOS(source, singleQuote, escapeChar);
		} 
		if(source.tryConsume(doubeQuote)) {
			return LexingUtils.consumeUntilDelimiterOrEOS(source, doubeQuote, escapeChar);
		}
		{
			return source.consumeUntil((reader) -> isWhitespace(reader.lookahead()));
		}
	}
	
}