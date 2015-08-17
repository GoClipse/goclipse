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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;

import melnorme.lang.tooling.LANG_SPECIFIC;

@LANG_SPECIFIC
public enum BundleModelElementKind {
	DEP_CONTAINER,
	DEP_REFERENCE,
	
	ERROR_ELEMENT,
	;
	
	
	public static interface BundleModelElementsSwitcher<RET> {
		
		default RET switchBundleElement(IBundleModelElement element) {
			switch (element.getElementType()) {
			case DEP_CONTAINER: return visitDepContainer((DependenciesContainer) element);
			case DEP_REFERENCE: return visitRawDepElement((RawDependencyElement) element);
			case ERROR_ELEMENT: return visitErrorElement((BundleErrorElement) element);
			}
			throw assertUnreachable();
		}
		
		public abstract RET visitDepContainer(DependenciesContainer element);
		public abstract RET visitRawDepElement(RawDependencyElement element);
		public abstract RET visitErrorElement(BundleErrorElement element);
		
	}
}