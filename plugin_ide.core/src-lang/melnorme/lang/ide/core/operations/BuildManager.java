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
package melnorme.lang.ide.core.operations;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.utilbox.collections.Indexable;


public class BuildManager {
	
	private static final BuildManager instance = LangCore_Actual.createBuildManager();
	
	public static BuildManager getInstance() {
		return instance;
	}
	
	/* -----------------  ----------------- */
	
	protected final Indexable<BuildTarget> buildConfigs;
	
	public BuildManager(Indexable<BuildTarget> buildConfigs) {
		this.buildConfigs = buildConfigs;
	}
	
	public Indexable<BuildTarget> getTargets(IProject project) {
		/* TODO */
		return buildConfigs;
	}
	
}