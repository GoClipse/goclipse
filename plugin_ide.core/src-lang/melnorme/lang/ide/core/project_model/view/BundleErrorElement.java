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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.ide.core.BundleModelElementKind;

public class BundleErrorElement extends AbstractBundleModelElement<IBundleModelElement> {
	
	public final String errorDescription;
	
	public BundleErrorElement(IBundleModelElement parent, String errorDescription) {
		super(parent);
		this.errorDescription = assertNotNull(errorDescription);
	}
	
	@Override
	public BundleModelElementKind getElementType() {
		return BundleModelElementKind.ERROR_ELEMENT;
	}
	
	@Override
	public String getElementName() {
		return "<error>";
	}
	
	@Override
	public String getPathString() {
		return getParent().getPathString() + "/" + getElementName();
	}
}