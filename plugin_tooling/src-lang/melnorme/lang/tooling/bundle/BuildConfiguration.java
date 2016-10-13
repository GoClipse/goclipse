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
package melnorme.lang.tooling.bundle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

public class BuildConfiguration {
	
	protected final String name;
	protected final String artifactPath;
	
	public BuildConfiguration(String name, String artifactPath) {
		this.name = assertNotNull(name);
		this.artifactPath = artifactPath;
	}
	
	public String getName() {
		return name;
	}
	
	public String getArtifactPath() {
		return artifactPath;
	}
	
}