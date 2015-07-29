/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.env;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;

import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.GoPackagesVisitor;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;

public class GoWorkspaceLocation {
	
	protected final Location location;
	
	public GoWorkspaceLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof GoWorkspaceLocation)) return false;
		
		GoWorkspaceLocation other = (GoWorkspaceLocation) obj;
		
		return areEqual(location, other.location);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(location);
	}
	
	@Override
	public String toString() {
		return location.toPathString();
	}
	
	/* -----------------  ----------------- */
	
	public Location getLocation() {
		return location;
	}
	
	public Location getBinLocation() {
		return location.resolve_valid("bin");
	}
	
	public Location getSrcLocation() {
		return location.resolve_valid("src");
	}
	
	public Location getGoPackageLocation(String goPackageName) throws CommonException {
		GoPackageName goPackage = new GoPackageName(goPackageName);
		return getSrcLocation().resolve(goPackage.getFullNameAsString());
	}
	
	public ArrayList2<GoPackageName> findSourcePackages(Location directory) {
		GoPackagesVisitor goPackagesVisitor = new GoPackagesVisitor(this, directory) {
			
			@Override
			protected FileVisitResult handleFileVisitException(Path file, IOException exc) {
				// TODO: some sort of logging
				return FileVisitResult.CONTINUE;
			}
		};
		
		return new ArrayList2<>(goPackagesVisitor.getModuleNames());
	}
	
	public ArrayList2<GoPackageName> findSubPackages(String parentGoPackage) throws CommonException {
		Location goPackageLocation = getGoPackageLocation(parentGoPackage);
		return findSourcePackages(goPackageLocation);
	}
	
}