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

import melnorme.lang.ide.core.navigator.ElementContainer;
import melnorme.lang.ide.core.operations.BuildTarget;

public class BuildTargetElement extends ElementContainer<ElementContainer<?>> {
	
	protected final BuildTarget buildTarget;
	
	public BuildTargetElement(BuildTarget buildTarget) {
		super(null);
		this.buildTarget = assertNotNull(buildTarget);
	}
	
	public String getTargetName() {
		String targetName = buildTarget.getTargetName();
		return targetName == null ? "<default>" : targetName;
	}
	
}