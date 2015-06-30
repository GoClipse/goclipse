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
package melnorme.lang.tooling.data;

import melnorme.utilbox.misc.Location;

public class LANGUAGE_SDKLocationValidator extends SDKLocationValidator {
	
	public LANGUAGE_SDKLocationValidator() {
		super("SDK path:");
	}
	
	@Override
	protected String getSDKExecutable_append() {
		return "bin/ls"; // TODO: LANG 
	}
	
	@Override
	protected String getSDKExecutableErrorMessage(Location exeLocation) {
		return "Foo executable not found."; // TODO: LANG
	}
}