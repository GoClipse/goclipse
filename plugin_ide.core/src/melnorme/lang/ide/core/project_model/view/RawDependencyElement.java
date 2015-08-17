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

import melnorme.lang.ide.core.project_model.view.AbstractRawDependencyElement;
import melnorme.lang.tooling.bundle.DependencyRef;

public class RawDependencyElement extends AbstractRawDependencyElement<DependenciesContainer> {
	
	public RawDependencyElement(DependenciesContainer parent, DependencyRef dependencyRef) {
		super(parent, dependencyRef);
	}
	
}