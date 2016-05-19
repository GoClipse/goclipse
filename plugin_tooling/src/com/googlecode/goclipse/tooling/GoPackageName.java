/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;

import melnorme.lang.tooling.AbstractElementName2;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.PathUtil;

public class GoPackageName extends AbstractElementName2 implements Comparable<GoPackageName> {
	
	public static final String NAME_SEP = "/";
	
	public GoPackageName(String moduleFullName) {
		super(moduleFullName, NAME_SEP);
	}
	
	/** Note: this new class will own segments array, it should not be modified. */
	public GoPackageName(String[] segments) {
		super(segments, NAME_SEP);
	}
	
	public String getCUSimpleName() {
		return getLastSegment();
	}

	public static GoPackageName fromPath(Path path) {
		if(path == null) {
			return null;
		}
		assertTrue(path.isAbsolute() == false);
		
		return new GoPackageName(path.toString().replace(File.separator, NAME_SEP));
	}
	
	public static GoPackageName createValid(String goPackageString) throws CommonException {
		if(goPackageString.isEmpty()) {
			throw new CommonException("Go package name is empty.");
		}
		Path goPackagePath = PathUtil.createPath(goPackageString);
		
		ArrayList2<String> segments = new ArrayList2<>();
		
		for(Path pathSegment : goPackagePath) {
			String segmentStr = pathSegment.toString();
			if(segmentStr.isEmpty() || segmentStr.equals(".") || segmentStr.equals("..")) {
				throw new CommonException(
					MessageFormat.format("Invalid Go package segment: `{0}`", segmentStr));
			}
			segments.add(segmentStr);
		}
		
		return new GoPackageName(segments.toArray(String.class));
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public String toString() {
		return elementName;
	}
	
	public String asString() {
		return elementName;
	}
	
	@Override
	public int compareTo(GoPackageName other) {
		return elementName.compareTo(other.elementName);
	}
	
}