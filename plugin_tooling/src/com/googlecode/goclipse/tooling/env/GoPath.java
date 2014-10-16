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
package com.googlecode.goclipse.tooling.env;

import static java.util.Collections.unmodifiableList;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.googlecode.goclipse.tooling.GoPackageName;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StringUtil;

/**
 * Helper class to work with a GOPATH entry list.
 */
public class GoPath {
	
	protected final List<String> goPathElements;
	
	public GoPath(String goPathString) {
		assertNotNull(goPathString);
		String[] elements = goPathString.isEmpty() ? new String[0] : goPathString.split(File.pathSeparator);
		this.goPathElements = unmodifiableList(new ArrayList2<>(elements));
	}
	
	public List<String> getGoPathEntries() {
		return goPathElements;
	}
	
	public String asString() {
		return getGoPathWorkspaceString(); 
	}
	
	public String getGoPathWorkspaceString() {
		return StringUtil.collToString(goPathElements, File.pathSeparator);
	}
	
	/** @return the full path of a GOPATH workspace entry (a workspace root) that contains the given path. */
	public Path findGoPathEntry(Path path) {
		if(path == null) {
			return null;
		}
		for (String pathElement : goPathElements) {
			if(path.startsWith(pathElement)) {
				return MiscUtil.createPathOrNull(pathElement);
			}
		}
		return null;
	}
	
	public Path findGoPathEntryForSourceModule(Path path) {
		Path workspaceEntry = findGoPathEntry(path);
		
		if(path.startsWith(workspaceEntry.resolve("src"))) {
			return workspaceEntry;
		}
		return null;
	}
	
	public GoPackageName findGoPackageForSourceModule(Path goModulePath) {
		Path goWorkspaceEntry = findGoPathEntry(goModulePath);
		if(goWorkspaceEntry == null) {
			return null;
		}
		
		Path sourceRoot = goWorkspaceEntry.resolve("src");
		return GoRoot.findGoPackageForSourceModule(goModulePath, sourceRoot);
	}
	
	public boolean isEmpty() {
		return goPathElements.size() == 0;
	}
	
}