/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model.view;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.utilbox.misc.ArrayUtil;

public class DependenciesContainer extends AbstractDependenciesContainer<AbstractBundleInfo> {
	
	public DependenciesContainer(AbstractBundleInfo bundleInfo, IProject project) {
		super(bundleInfo, project);
	}
	
	@Override
	protected IBundleModelElement[] createChildren() {
		ArrayList<IBundleModelElement> newChildren = new ArrayList<>();
		assertFail();
		// Not applicable to Go. Although maybe we could refactor GOPATH element to fit this.
		return ArrayUtil.createFrom(newChildren, IBundleModelElement.class);
	}
	
}