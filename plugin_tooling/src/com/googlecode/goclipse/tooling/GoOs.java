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

public class GoOs {
	
	public static final String OS_WINDOWS = "windows";
	public static final String OS_DARWIN  = "darwin";
	public static final String OS_LINUX   = "linux";
	public static final String OS_FREEBSD = "freebsd";
	public static final String OS_NACL    = "nacl";
	
	protected final String goOs;
	
	public GoOs(String goOs) {
		this.goOs = goOs;
	}
	
	public String asString() {
		return goOs;
	}
	
}