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
package melnorme.lang.ide.ui.navigator;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.navigator.ElementContainer;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.HashcodeUtil;

public class BuildTargetsContainer extends ElementContainer<BuildTargetElement> {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	public final ProjectBuildInfo buildInfo;
	
	public BuildTargetsContainer(ProjectBuildInfo buildInfo) {
		super(toBuildTargetElements(buildInfo));
		this.buildInfo = buildInfo;
	}
	
	protected static ArrayList2<BuildTargetElement> toBuildTargetElements(ProjectBuildInfo buildInfo) {
		IProject project = buildInfo.getProject();
		assertNotNull(buildInfo);
		assertNotNull(project);
		Indexable<BuildTarget> buildTargets = buildInfo.getBuildTargets();
		return new ArrayList2<BuildTargetElement>()
				.addAll(buildTargets, buildTarget -> new BuildTargetElement(project, buildTarget));
	}
	
	public String getText() {
		return "Build Targets";
	}
	
	public IProject getProject() {
		return buildInfo.getProject();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTargetsContainer)) return false;
		
		BuildTargetsContainer other = (BuildTargetsContainer) obj;
		
		return areEqual(getProject(), other.getProject());
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(getProject());
	}
	
}