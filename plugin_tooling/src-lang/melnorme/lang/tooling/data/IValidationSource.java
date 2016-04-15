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
package melnorme.lang.tooling.data;

public interface IValidationSource {
	
	public IStatusMessage getValidationStatus();
	
	default StatusLevel getValidationStatusLevel() {
		IStatusMessage se = getValidationStatus();
		return se == null ? StatusLevel.OK : se.getSeverity().toStatusLevel(); 
	}
	
	default void validate() throws StatusException {
		IStatusMessage validationStatus = getValidationStatus();
		if(validationStatus != null) {
			throw validationStatus.toStatusException();
		}
	}
	
	public static interface IValidationSourceX extends IValidationSource {
		
		@Override
		default IStatusMessage getValidationStatus() {
			try {
				validate();
				return null;
			} catch(StatusException e) {
				return e;
			}
		}
		
		@Override
		abstract void validate() throws StatusException;
		
	}
	
	/* -----------------  ----------------- */
	
	static IStatusMessage getHighestStatus(Iterable<? extends IValidationSource> validationSources) {
		IStatusMessage highestSE = null;
		for(IValidationSource validationSource : validationSources) {
			
			IStatusMessage se = validationSource.getValidationStatus();
			if(se != null) {
				if(highestSE == null || se.isHigherSeverity(highestSE)) {
					highestSE = se;
				}
			}
			
		}
		return highestSE;
	}
	
}