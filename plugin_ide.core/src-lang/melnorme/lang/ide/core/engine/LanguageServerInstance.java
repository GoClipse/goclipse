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
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.process.ExternalProcessHelper;

public abstract class LanguageServerInstance {
	
	protected final Path serverPath;
	protected final ExternalProcessHelper serverProcess;
	
	public LanguageServerInstance(Path serverPath, ExternalProcessHelper serverProcess) {
		this.serverPath = serverPath;
		this.serverProcess = assertNotNull(serverProcess);
	}
	
	public Path getServerPath() {
		return serverPath;
	}
	
	public ExternalProcessHelper getServerProcess() {
		return serverProcess;
	}
	
	public void stop() {
		LangCore.logInfo("Stopping " + getLanguageServerName());
		serverProcess.getProcess().destroy();
	}
	
	protected abstract String getLanguageServerName();
	
}