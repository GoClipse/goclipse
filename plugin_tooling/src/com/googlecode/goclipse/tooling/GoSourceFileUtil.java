/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling;

import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.core.CommonException;

public class GoSourceFileUtil {
	
	/**
	 * Heuristic (not entirely precise) method to find offset of package name in package declaration
	 */
	public static int findPackageDeclaration_NameStart(String source) throws CommonException {
		
		StringParseSource parser = new StringParseSource(source);
		
		while(parser.hasCharAhead()) {
			
			if(parser.lookaheadMatches("//")) {
				parser.consumeLine();
				continue;
			}
			
			if(parser.tryConsume(" ")) {
				continue;
			}
			
			if(parser.tryConsume("package ")) {
				
				consumeSpaces(parser);
				return parser.getOffset();
			} else {
				String line = parser.consumeLine();
				if(line.trim().isEmpty()) {
					continue;
				}
				break;
			}
		}
		throw new CommonException("Go package declaration not found. ");
	}
	
	protected static void consumeSpaces(StringParseSource parser) {
		while(parser.tryConsume(" ")) {
		}
	}
	
}