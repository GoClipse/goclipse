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
package melnorme.lang.ide.core;

import melnorme.lang.tooling.LANG_SPECIFIC;

@LANG_SPECIFIC
public enum BundleModelElementKind {
	DEP_CONTAINER,
	DEP_REFERENCE,
	
	RESOLVED_DEP,
	STANDARD_LIB,
	
	ERROR_ELEMENT,
	
	DEP_SRC_FOLDER,
}