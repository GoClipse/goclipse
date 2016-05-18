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
package melnorme.lang.tooling;

import melnorme.lang.tooling.ast.ISourceRepresentation;

public enum EProtection implements ISourceRepresentation {
	
    PRIVATE,
    PACKAGE,
    PROTECTED,
    PUBLIC,
    EXPORT,
    ;
    
	@Override
	public String getSourceValue() {
		return super.toString().toLowerCase();
	}
	
	@Override
	public String toString() {
		return getSourceValue();
	}
	
}