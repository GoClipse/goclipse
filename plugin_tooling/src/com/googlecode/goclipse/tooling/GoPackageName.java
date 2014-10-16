/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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

import melnorme.lang.tooling.AbstractElementName;

public class GoPackageName extends AbstractElementName {
	
	public static final String NAME_SEP = "/";
	
	public GoPackageName(String moduleFullName) {
		super(moduleFullName, NAME_SEP);
	}
	
	/** Note: the new class will own segments array, it should not be modified. */
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
	
}