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
package com.googlecode.goclipse.tooling.env;

import static java.util.Collections.unmodifiableList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import com.googlecode.goclipse.tooling.GoPackageName;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.misc.StringUtil;

/**
 * Helper class to work with a GOPATH entry list.
 */
public class GoPath {
	
	public static final String SRC_DIR = "src";
	
	protected final List<String> goPathElements;
	
	public GoPath(String goPathString) {
		this(StringUtil.splitToList(goPathString, File.pathSeparator));
	}
	
	public GoPath(Collection<String> goPathElements) {
		// Use HashSet to remove duplicates
		LinkedHashSet<String> newElements = new LinkedHashSet<>();
		for (String string : goPathElements) {
			if(!string.isEmpty()) {
				newElements.add(string);
			}
		}
		this.goPathElements = unmodifiableList(new ArrayList<>(newElements));
	}
	
	public List<String> getGoPathEntries() {
		return goPathElements;
	}
	
	public boolean isEmpty() {
		return goPathElements.size() == 0;
	}
	
	public void validate() throws CommonException {
		if(isEmpty()) {
			throw new CommonException("Invalid Go environment, GOPATH is empty.");
		}
	}
	
	public String asString() {
		return getGoPathWorkspaceString(); 
	}
	
	public String getGoPathWorkspaceString() {
		return StringUtil.collToString(goPathElements, File.pathSeparator);
	}
	
	/** @return the GoWorkspaceLocation from the GOPATH entries that contains the given goPathSubLocation. */
	public GoWorkspaceLocation findGoPathEntry(Location goPathSubLocation) {
		if(goPathSubLocation == null) {
			return null;
		}
		for (String pathElement : goPathElements) {
			Location pathElementLoc = Location.createValidOrNull(PathUtil.createPathOrNull(pathElement));
			
			if(pathElementLoc != null && goPathSubLocation.startsWith(pathElementLoc)) {
				return new GoWorkspaceLocation(pathElementLoc);
			}
		}
		return null;
	}
	
	/** @return the GOPATH entry that contains the given sourcePath, if it's in the "src" folder of that entry. 
	 * Return null otherwise. */
	public GoWorkspaceLocation findGoPathEntryForSourcePath(Location sourcePath) {
		GoWorkspaceLocation goWorkspace = findGoPathEntry(sourcePath);
		
		if(goWorkspace != null && sourcePath.startsWith(goWorkspace.getSrcLocation())) {
			return goWorkspace;
		}
		return null;
	}
	
	/** @return the Go package name for given goPackageLocation, if it's within a source folder of a GOPATH entry, 
	 * null otherwise. */
	public GoPackageName findGoPackageForLocation(Location goPackageLocation) {
		GoWorkspaceLocation goPathEntry = findGoPathEntry(goPackageLocation);
		if(goPathEntry == null) {
			return null;
		}
		
		Location sourceRoot = goPathEntry.getSrcLocation();
		return GoEnvironment.getGoPackageForLocation(goPackageLocation, sourceRoot);
	}
	
	public static GoPackageName getGoPackageForPath(Location goPathEntry, Location packageLoc) {
		Location sourceRoot = goPathEntry.resolve_fromValid(SRC_DIR);
		if(!packageLoc.startsWith(goPathEntry)) {
			return null;
		}
		return GoPackageName.fromPath(sourceRoot.relativize(packageLoc));
	}
	
	public ArrayList2<GoPackageName> findGoSourcePackages(Location subLocation) {
		GoWorkspaceLocation workspaceEntry = findGoPathEntry(subLocation);
		if(workspaceEntry == null) {
			return new ArrayList2<>();
		}
		
		if(subLocation.equals(workspaceEntry.getLocation())) {
			subLocation = workspaceEntry.getSrcLocation();
		}
		
		return workspaceEntry.findSourcePackages(subLocation);
	}
	
}