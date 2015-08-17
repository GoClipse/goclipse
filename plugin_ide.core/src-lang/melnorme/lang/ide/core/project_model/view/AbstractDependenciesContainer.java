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
package melnorme.lang.ide.core.project_model.view;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;

public abstract class AbstractDependenciesContainer<BUNDLEINFO> extends AbstractBundleModelElement<IProject> {
	
	protected final BUNDLEINFO bundleInfo;
	protected final IBundleModelElement[] depElements;
	
	public AbstractDependenciesContainer(BUNDLEINFO bundleInfo, IProject project) {
		super(project);
		this.bundleInfo = assertNotNull(bundleInfo);
		depElements = createChildren();
	}
	
	protected abstract IBundleModelElement[] createChildren();
	
	@Override
	public BundleModelElementKind getElementType() {
		return BundleModelElementKind.DEP_CONTAINER;
	}
	
	public BUNDLEINFO getBundleInfo() {
		return bundleInfo;
	}
	
	public IProject getProject() {
		return getParent();
	}
	
	@Override
	public String getElementName() {
		return "{Dependencies}";
	}
	
	@Override
	public String getPathString() {
		return getProject().getFullPath().toPortableString() + "/" + getElementName();
	}
	
	@Override
	public boolean hasChildren() {
		return depElements.length > 0;
	}
	
	@Override
	public IBundleModelElement[] getChildren() {
		return depElements;
	}
	
}