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
package com.googlecode.goclipse.core;

import static java.util.Collections.unmodifiableList;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StringUtil;

import org.eclipse.core.resources.IProject;

/**
 * Helper class to work with a project's GOPATH entry list.
 * (new API intended to replace {@link GoWorkspace} 
 */
public class GoWorkspace2 {
	
	protected final List<String> goPathElements;
	
	public GoWorkspace2(IProject project) {
		this(project, GoEnvironmentPrefs2.getEffectiveGoPath(project));
	}
	
	public GoWorkspace2(IProject project, String defaultGoPathString) {
		this(project.getLocation().toOSString() + File.pathSeparator + defaultGoPathString);
	}
	
	public GoWorkspace2(String goPathString) {
		this.goPathElements = unmodifiableList(new ArrayList2<>(goPathString.split(File.pathSeparator)));
	}
	
	public List<String> getGoPathElements() {
		return goPathElements;
	}
	
	public String getGoPathWorkspaceString() {
		return StringUtil.collToString(goPathElements, File.pathSeparator);
	}
	

	public Path getGoWorkspacePathEntry(Path path) {
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
	
	public Path getGoPackageFromGoModule(Path goModulePath) {
		Path goWorkspaceEntry = getGoWorkspacePathEntry(goModulePath);
		if(goWorkspaceEntry == null) {
			return null;
		}
		
		Path relPath = goWorkspaceEntry.relativize(goModulePath);
		if(!relPath.startsWith("src")) {
			return null;
		} 
		relPath = relPath.subpath(1, relPath.getNameCount());
		
		return relPath.getParent();
	}
	
}