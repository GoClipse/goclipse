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
package melnorme.lang.tooling.data.validation;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.function.Supplier;

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;

public interface ValidationSource {
	
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
	
	/**
	 * Essentially the same as {@link ValidationSource}, but for implementers 
	 * wishing to implement the {@link #validate()} method instead of {@link #getValidationStatus()}.
	 */
	public static interface ValidationSourceX extends ValidationSource {
		
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
	
	public static class ValidatableField<SOURCE> implements ValidationSourceX {
		
		public final Supplier<SOURCE> property;
		public final Validator<SOURCE, ?> validator;
		
		public ValidatableField(Supplier<SOURCE> field, Validator<SOURCE, ?> validator) {
			this.property = assertNotNull(field);
			this.validator = assertNotNull(validator);
		}
		
		@Override
		public void validate() throws StatusException {
			validator.validateField(property.get());
		}
	}
	
	/* -----------------  ----------------- */
	
	static IStatusMessage getHighestStatus(Iterable<? extends ValidationSource> validationSources) {
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