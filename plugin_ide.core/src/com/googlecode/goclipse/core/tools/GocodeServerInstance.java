/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.tools;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.engine.LanguageServerInstance;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

public class GocodeServerInstance extends LanguageServerInstance {
	
	public static final String GOCODE_SERVER_Name = LangCore_Actual.LANGUAGE_SERVER_Name + " server";
	
	public GocodeServerInstance(Path serverPath, ExternalProcessNotifyingHelper serverProcess) {
		super(serverPath, serverProcess);
	}
	
	@Override
	protected String getLanguageServerName() {
		return GOCODE_SERVER_Name;
	}
	
}