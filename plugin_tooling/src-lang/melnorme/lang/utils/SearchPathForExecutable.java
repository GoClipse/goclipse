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
package melnorme.lang.utils;

import java.text.MessageFormat;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class SearchPathForExecutable extends SearchPathEnvOperation {
	
	protected final String exeName;
	
	protected Location exeLocation; 
	
	public SearchPathForExecutable(String exeName) {
		this.exeName = MiscUtil.OS_IS_WINDOWS ? exeName + ".exe" : exeName;
	}
	
	public void checkIsFound() throws CommonException {
		check("PATH");
	}
	
	public void check(String envVarName) throws CommonException {
		searchEnvironmentVar(envVarName);
		if(exeLocation == null) {
			throw new CommonException(
				MessageFormat.format("`{0}` not found on the {1} environment var.", exeName, envVarName));
		}
	}
	
	@Override
	protected void searchPathEntry(Location path) {
		if(exeLocation != null) {
			return; // Already found
		}
		
		Location possibleLocation = path.resolve_fromValid(exeName);
		if(possibleLocation.toFile().exists()) {
			exeLocation = possibleLocation;
		}
	}
	
	@Override
	protected void handleWarning(String message) {
		// Ignore
	}
	
}