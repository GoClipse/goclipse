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

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.navigator.ElementContainer;
import melnorme.lang.ide.core.operations.BuildManager;
import melnorme.lang.ide.core.operations.BuildTarget;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class BuildTargetsContainer extends ElementContainer<BuildTargetElement> {
	
	protected final BuildManager buildManager = BuildManager.getInstance();
	protected final IProject project;
	
	public BuildTargetsContainer(IProject project) {
		this(project, BuildManager.getInstance().getTargets(project));
	}
	
	public BuildTargetsContainer(IProject project, Indexable<BuildTarget> buildTargets) {
		super(toBuildTargetElements(buildTargets));
		this.project = project;
	}
	
	protected static ArrayList2<BuildTargetElement> toBuildTargetElements(Indexable<BuildTarget> targets) {
		return new ArrayList2<BuildTargetElement>()
				.addAll(targets, buildTarget -> new BuildTargetElement(buildTarget));
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getText() {
		return "Build Targets";
	}
	
}