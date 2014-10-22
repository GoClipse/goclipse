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

import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GoEnvironmentPrefs {
	
	static StringPreference GO_ROOT = new StringPreference("com.googlecode.goclipse.goroot", "");
	static StringPreference GO_PATH = new StringPreference("com.googlecode.goclipse.gopath", "");
	static StringPreference GO_OS = new StringPreference("com.googlecode.goclipse.goos", "");
	static StringPreference GO_ARCH = new StringPreference("com.googlecode.goclipse.goarch", "");
	
	static StringPreference COMPILER_PATH = new StringPreference("com.googlecode.goclipse.compiler.path", "");
	static StringPreference FORMATTER_PATH = new StringPreference("com.googlecode.goclipse.formatter.path", "");
	static StringPreference DOCUMENTOR_PATH = new StringPreference("com.googlecode.goclipse.documentor.path", "");
	
}