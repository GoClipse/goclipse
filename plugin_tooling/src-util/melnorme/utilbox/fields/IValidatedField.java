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
package melnorme.utilbox.fields;

import melnorme.lang.tooling.data.IFieldValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;

public interface IValidatedField<TYPE> {
	
	public TYPE getValidatedField() throws StatusException;
	
	default StatusLevel getValidationStatus() {
		try {
			getValidatedField();
			return StatusLevel.OK;
		} catch (StatusException se) {
			return se.getStatusLevel();
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class NullValidationSource<T> implements IValidatedField<T> {
		
		public static <T> NullValidationSource<T> create() {
			return new NullValidationSource<>();
		}
		
		@Override
		public T getValidatedField() throws StatusException {
			return null;
		}
	}
	
	public static class ValidatedField implements IValidatedField<Object> {
		
		public final IDomainField<String> field;
		public final IFieldValidator validator;
		
		public ValidatedField(IDomainField<String> field, IFieldValidator validator) {
			this.field = field;
			this.validator = validator;
		}
		
		@Override
		public Object getValidatedField() throws StatusException {
			String fieldValue = field.getFieldValue();
			return validator.getValidatedField(fieldValue);
		}
	}
	
	/* -----------------  ----------------- */
	
	static StatusException getHighestStatus(Iterable<IValidatedField<?>> validationSources) {
		StatusException highestSE = null;
		for(IValidatedField<?> validationSource : validationSources) {
			
			try {
				validationSource.getValidatedField();
			} catch (StatusException se) {
				if(highestSE == null || se.getStatusLevelOrdinal() > highestSE.getStatusLevelOrdinal()) {
					highestSE = se;
				}
			}
		}
		return highestSE;
	}
	
}