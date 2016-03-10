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