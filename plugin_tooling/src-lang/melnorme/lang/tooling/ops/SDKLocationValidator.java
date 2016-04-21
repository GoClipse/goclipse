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
package melnorme.lang.tooling.ops;

import java.text.MessageFormat;

import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.validation.ValidationException;
import melnorme.lang.tooling.ops.util.LocationValidator;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public abstract class SDKLocationValidator extends LocationValidator {
	
	public SDKLocationValidator(String fieldNamePrefix) {
		super(fieldNamePrefix);
		directoryOnly = true;
	}
	
	@Override
	protected Location getValidatedField_rest(Location sdkLocation) throws ValidationException {
		Location sdkExecutableLocation = getSDKExecutableLocation(sdkLocation);
		
		if(!sdkExecutableLocation.toFile().exists()) {
			throw createException(Severity.WARNING, getSDKExecutableErrorMessage(sdkExecutableLocation));
		}
		return sdkExecutableLocation;
	}
	
	protected Location getSDKExecutableLocation(Location location) {
		String exeRelativePath = getSDKExecutable_append();
		if(MiscUtil.OS_IS_WINDOWS) {
			exeRelativePath += ".exe"; 
		}
		return location.resolve_fromValid(exeRelativePath);
	}
	
	protected abstract String getSDKExecutable_append();
	
	protected String getSDKExecutableErrorMessage(Location exeLocation) {
		return MessageFormat.format("`{0}` executable not found.", exeLocation);
	}
	
}