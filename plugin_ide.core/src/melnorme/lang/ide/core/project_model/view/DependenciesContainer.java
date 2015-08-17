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

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import dtool.dub.DubBundle;
import dtool.dub.DubBundleDescription;
import melnorme.lang.ide.core.project_model.view.AbstractDependenciesContainer;
import melnorme.lang.ide.core.project_model.view.BundleErrorElement;
import melnorme.lang.ide.core.project_model.view.IBundleModelElement;
import melnorme.lang.tooling.bundle.DependencyRef;
import melnorme.utilbox.misc.ArrayUtil;
import mmrnmhrm.core.dub_model.DubBundleInfo;
import mmrnmhrm.core.workspace.viewmodel.DubDependencyElement;

public class DependenciesContainer extends AbstractDependenciesContainer<DubBundleInfo> {
	
	public DependenciesContainer(DubBundleInfo bundleInfo, IProject project) {
		super(bundleInfo, project);
	}
	
	protected DubBundleDescription getBundleDesc() {
		return bundleInfo.getBundleDesc();
	}
	
	@Override
	protected IBundleModelElement[] createChildren() {
		ArrayList<IBundleModelElement> newChildren = new ArrayList<>();
		
		if(getBundleDesc().isResolved()) {
			for (DubBundle dubBundle : getBundleDesc().getBundleDependencies()) {
				newChildren.add(new DubDependencyElement(this, dubBundle));
			}
		} else {
			for (DependencyRef dubBundleRef : bundleInfo.getMainBundle().getDependencyRefs()) {
				newChildren.add(new RawDependencyElement(this, dubBundleRef));
			}
		}
		if(getBundleDesc().getError() != null) {
			newChildren.add(new BundleErrorElement(this, getBundleDesc().getError().getExtendedMessage()));
		}
		return ArrayUtil.createFrom(newChildren, IBundleModelElement.class);
	}
	
}