/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.data;

import java.nio.file.Path;

import melnorme.utilbox.misc.Location;

public class LocationOrSinglePathValidator extends LocationValidator {
	
	public LocationOrSinglePathValidator() {
	}
	
	@Override
	protected Location validatePath(Path path) throws StatusException {
		if(!path.isAbsolute() && path.getNameCount() == 1) {
			return null; // special case allowed
		}
		
		return super.validatePath(path);
	}
	
}