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
import org.eclipse.core.runtime.Platform;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.SimpleLogger;


public class BuildManager 
	extends ProjectBasedModel<Indexable<BuildTarget>, IProjectModelListener<Indexable<BuildTarget>>> {
	
	private static final BuildManager instance = LangCore_Actual.createBuildManager();
	
	public static BuildManager getInstance() {
		return instance;
	}
	
	protected final SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	/* -----------------  ----------------- */
	
	protected final Indexable<BuildTarget> buildConfigs;
	
	public BuildManager(Indexable<BuildTarget> buildConfigs) {
		this.buildConfigs = buildConfigs;
	}
	
	@Override
	protected SimpleLogger getLog() {
		return log;
	}
	
	public Indexable<BuildTarget> getTargets(IProject project) {
		return getProjectInfo(project);
	}
	
}