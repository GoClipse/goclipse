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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

public class GoRoot {
	
	protected final String goRoot;
	
	public GoRoot(String goRoot) {
		this.goRoot = assertNotNull(goRoot);
	}
	
	public String asString() {
		return goRoot;
	}
	
	public Path asPath() throws StatusException {
		return GoEnvironment.createPath(goRoot);
	}
	
	public Path getSourceRootLocation() throws StatusException {
		return asPath().resolve("src/pkg");
	}
	
	public Path getGoPackageFromSourceModule(Path goModulePath) throws StatusException {
		return getGoPackageFromSourceModule(goModulePath, getSourceRootLocation());
	}
	
	public static Path getGoPackageFromSourceModule(Path goModulePath, Path sourceRoot) {
		goModulePath = goModulePath.normalize();
		if(!goModulePath.startsWith(sourceRoot)) {
			return null;
		}
		goModulePath = sourceRoot.relativize(goModulePath);
		return goModulePath.getParent(); // Discard file name
	}
	
}