/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;

import java.nio.file.Path;

import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.StringUtil;

public abstract class AbstractBundleInfo {
	
	public abstract Path getEffectiveTargetFullPath();
	
	public abstract Indexable<BuildConfiguration> getBuildConfigurations();
	
	
	public static class BuildConfiguration {
		
		protected final String name;
		protected final Path artifactPath;
		
		public BuildConfiguration(String name, Path artifactPath) {
			this.name = StringUtil.nullAsEmpty(name);
			this.artifactPath = artifactPath;
		}
		
		public String getName() {
			return name;
		}
		
		public Path getArtifactPath() {
			return artifactPath;
		}
		
	}
	
}