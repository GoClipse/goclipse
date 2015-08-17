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


import melnorme.lang.tooling.bundle.DependencyRef;

public abstract class AbstractRawDependencyElement<PARENT extends IBundleModelElement> 
		extends AbstractBundleModelElement<PARENT> {
	
	protected final DependencyRef dependencyRef;
	
	public AbstractRawDependencyElement(PARENT parent, DependencyRef dependencyRef) {
		super(parent);
		this.dependencyRef = dependencyRef;
	}
	
	public String getBundleName() {
		return dependencyRef.getBundleName();
	}
	
	public DependencyRef getDependencyRef() {
		return dependencyRef;
	}
	
	@Override
	public BundleModelElementKind getElementType() {
		return BundleModelElementKind.DEP_REFERENCE;
	}
	
	@Override
	public String getElementName() {
		return getBundleName();
	}
	
	@Override
	public String getPathString() {
		return getParent().getPathString() + "/@" + getElementName();
	}

}