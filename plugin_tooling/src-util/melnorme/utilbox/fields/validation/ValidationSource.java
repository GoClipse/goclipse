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
package melnorme.utilbox.fields.validation;

import melnorme.utilbox.core.fntypes.RunnableX;
import melnorme.utilbox.status.IStatusMessage;
import melnorme.utilbox.status.StatusException;

public interface ValidationSource {
	
	/**
	 * Provide a validation status.
	 * @return the validation {@link IStatusMessage}, or null if none.	
	 */
	public IStatusMessage getValidationStatus();
	
	
	default void validate() throws StatusException {
		throwValidation(getValidationStatus());
	}
	
	public static void throwValidation(IStatusMessage validationStatus) throws StatusException {
		if(validationStatus != null) {
			throw validationStatus.toStatusException();
		}
	}
	
	/* -----------------  ----------------- */
	
	public static ValidationSource fromRunnable(RunnableX<StatusException> validationRunnable) {
		return () -> validateFromRunnable(validationRunnable);
	}
	
	public static StatusException validateFromRunnable(RunnableX<StatusException> runnable) {
		try {
			runnable.run();
			return null;
		} catch(StatusException e) {
			return e;
		}
	}
	
	/* -----------------  ----------------- */
	
	public static IStatusMessage getValidationStatus(ValidationSource... sources) {
		for (ValidationSource validationSource : sources) {
			IStatusMessage validationStatus = validationSource.getValidationStatus();
			if(validationSource != null) {
				return validationStatus;
			}
		}
		return null;
	}
	
	public static IStatusMessage getHighestStatus(Iterable<? extends ValidationSource> validationSources) {
		IStatusMessage highestSE = null;
		for(ValidationSource validationSource : validationSources) {
			
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