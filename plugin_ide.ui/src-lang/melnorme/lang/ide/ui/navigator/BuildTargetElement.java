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
package melnorme.lang.ide.ui.navigator;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.CollectionUtil;

public class BuildTargetElement extends ElementContainer<ElementContainer<?>> {
	
	protected final BuildTarget buildTarget;
	protected final IProject project;
	
	public BuildTargetElement(IProject project, BuildTarget buildTarget) {
		super(null);
		this.project = project;
		this.buildTarget = assertNotNull(buildTarget);
	}
	
	public String getTargetDisplayName() {
		String targetName = buildTarget.getTargetName();
		return targetName.isEmpty() ? "<default>" : targetName;
	}
	
	public BuildManager getBuildManager() {
		return BuildManager.getInstance();
	}
	
	public BuildTarget getBuildTarget() {
		return buildTarget;
	}
	
	public IProject getProject() {
		return project;
	}
	
	protected ProjectBuildInfo getBuildInfoOrNull() {
		if(getParent() instanceof BuildTargetsContainer) {
			BuildTargetsContainer buildTargetsContainer = (BuildTargetsContainer) getParent();
			return buildTargetsContainer.buildInfo;
		}
		return null;
	}
	
	public int getOrder() {
		ProjectBuildInfo buildInfo = getBuildInfoOrNull();
		if(buildInfo == null) {
			return 0;
		}
		return CollectionUtil.indexOf(buildInfo.getBuildTargets(), buildTarget);
	}
	
	/* -----------------  helpers  ----------------- */
	
	public ValidatedBuildTarget getValidatedBuildTarget() throws CommonException {
		return getBuildManager().getValidatedBuildTarget(project, buildTarget);
	}
	
}