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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

import com.googlecode.goclipse.tooling.GoPackageName;

public class GoRoot {
	
	protected final String goRootString;
	
	public GoRoot(String goRoot) {
		this.goRootString = assertNotNull(goRoot);
	}
	
	public String asString() {
		return goRootString;
	}
	
	public boolean isEmpty() {
		return goRootString.isEmpty();
	}
	
	public Location asLocation() throws CommonException {
		return Location.createValidLocation(goRootString, "Invalid GOROOT: ");
	}
	
	public Location getSourceRootLocation() throws CommonException {
		Location loc = asLocation().resolve_fromValid("src/pkg");
		if(loc.toFile().isDirectory()) {
			// if this path exists, the it is likely a Go installation before 1.4
			return loc;
		}
		// but for Go 1.4 and after, the source root is just in src
		return asLocation().resolve_fromValid("src");
	}
	
	public GoPackageName findGoPackageForLocation(Location goPackageLocation) throws CommonException {
		return GoEnvironment.getGoPackageForLocation(goPackageLocation, getSourceRootLocation());
	}
	
}