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

import java.nio.file.Path;

import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.MiscUtil.InvalidPathExceptionX;

import org.eclipse.core.runtime.CoreException;


public abstract class CommonGoLocation {
	
	public static final String DEFAULT_BIN_OUTPUT_FOLDER = "bin";
	public static final String DEFAULT_PKG_OUTPUT_FOLDER = "pkg";
	
	protected final String location;
	protected final String go_os;
	protected final String go_arch;
	
	public CommonGoLocation(String location, String go_os, String go_arch) {
		this.location = location;
		this.go_os = go_os;
		this.go_arch = go_arch;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getGo_OS() {
		return go_os;
	}
	
	public String getGo_Arch() {
		return go_arch;
	}
	
	public Path getGoPackagesLocation() throws CoreException {
		return createPath(location + "/pkg").resolve(getGo_OS_Arch_segment());
	}
	
	public Path getGo_OS_Arch_segment() throws CoreException {
		return createPath(go_os + "_" + go_arch);
	}
	
	public static Path createPath(String pathString) throws CoreException {
		try {
			return MiscUtil.createPath(pathString);
		} catch (InvalidPathExceptionX e) {
			throw GoCore.createCoreException("Invalid path", e);
		}
	}
	
}