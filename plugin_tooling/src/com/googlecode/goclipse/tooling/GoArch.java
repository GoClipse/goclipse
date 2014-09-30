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

public class GoArch {
	
	public static final String ARCH_AMD64 = "amd64";
	public static final String ARCH_386   = "386";
	public static final String ARCH_ARM   = "arm";
	
	protected final String goArch;
	
	public GoArch(String goArch) {
		this.goArch = goArch;
	}
	
	public String asString() {
		return goArch;
	}
	
}