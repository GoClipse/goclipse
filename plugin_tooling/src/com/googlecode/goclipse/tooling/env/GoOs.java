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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.array;
import static melnorme.utilbox.misc.StringUtil.emptyAsNull;

public class GoOs {
	
	public static final String OS_DARWIN  = "darwin";
	public static final String OS_DRAGONFLY  = "dragonfly";
	public static final String OS_FREEBSD = "freebsd";
	public static final String OS_LINUX   = "linux";
	public static final String OS_NETBSD = "netbsd";
	public static final String OS_OPENBSD = "openbsd";
	public static final String OS_PLAN9    = "plan9";
	public static final String OS_WINDOWS = "windows";
	public static final String OS_SOLARIS    = "solaris";
	
	public static final String[] GOOS_VALUES = array(
		OS_DARWIN,
		OS_DRAGONFLY,
		OS_FREEBSD,
		OS_LINUX,
		OS_NETBSD,
		OS_OPENBSD,
		OS_PLAN9,
		OS_WINDOWS,
		OS_SOLARIS
	);
	
	protected final String goOs;
	
	public GoOs(String goOs) {
		this.goOs = assertNotNull(emptyAsNull(goOs));
	}
	
	public String asString() {
		return goOs;
	}
	
	@Override
	public String toString() {
		return goOs;
	}
	
}