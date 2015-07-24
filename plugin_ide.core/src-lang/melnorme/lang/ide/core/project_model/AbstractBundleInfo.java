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
package melnorme.lang.ide.core.project_model;

import java.nio.file.Path;

import melnorme.lang.ide.core.operations.build.BuildTargetRunner.BuildConfiguration;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractBundleInfo {
	
	public abstract Path getEffectiveTargetFullPath() throws CommonException;
	
	public abstract Indexable<BuildConfiguration> getBuildConfigurations();
	
}